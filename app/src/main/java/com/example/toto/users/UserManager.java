package com.example.toto.users;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toto.ForgottenPasswordActivity;
import com.example.toto.interfaces.DatabaseHelper;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Observer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

//Use the UserManager to manage the state of the loggedin user
public class UserManager {
    private static UserController currentUser;
    private static final DatabaseHelper<User> userDb = new UserDatabaseHelper();

    private static void setCurrentUser(final User user, final OnSuccessListener listener) throws RuntimeException {
        if (user == null)
            throw new RuntimeException("setCurrentUser user is null");
        //retrieve info from users collection
        userDb.getById(user.getId(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        currentUser = new UserController(new User(document));
                    } else {
                        currentUser = new UserController(user);
                        //save the current user in the db, since it wasn't recorded yet
                        userDb.upsert(user);
                        Log.d("", "No such document");
                    }
                    listener.onSuccess(currentUser);
                } else {
                    Log.d("", "user query by Id failed: ", task.getException());
                }
            }
        });
    }

    //When sign up or signIn the setCurrentUser method will be called to create the currentUser.
    //Use UnsupportedOperationException for fatal errors,  Use InstantiationException for warnings
    public static void signupUser(FirebaseAuth firebaseAuth, String passwrd, String email, final String username, final Role role,
                                  Activity ctx, final OnSuccessListener successListener, final OnFailureListener failureListener) throws RuntimeException {
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseAuth finalFirebaseAuth = firebaseAuth;
        firebaseAuth.createUserWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finalFirebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //verification email was sent
                                                User user = new User(finalFirebaseAuth.getCurrentUser());
                                                user.setUsername(username);
                                                user.setRole(role);
                                                setCurrentUser(user, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        if (successListener != null)
                                                            successListener.onSuccess(currentUser.getUser());
                                                    }
                                                });
                                            }else{
                                                //problems with sending verification email
                                                if (failureListener != null)
                                                    failureListener.onFailure(new UnsupportedOperationException("error sending the email verification"));
                                            }
                                        }
                                    });
                        }else{
                            if (failureListener != null)
                                failureListener.onFailure(new UnsupportedOperationException("error creating new database user"));
                        }
                    }
                });

    }

    //Use UnsupportedOperationException for fatal errors
    //Use InstantiationException for warnings
    //TODO Users with ADMIN should be able to login without email verification
    public static void signinUser(FirebaseAuth firebaseAuth, String passwrd, String email,
                                  Activity ctx, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseAuth finalFirebaseAuth = firebaseAuth;
        firebaseAuth.signInWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user was retrieved and logged in
                            User user = new User(finalFirebaseAuth.getCurrentUser());
                            //ATTENTION: setCurrentUser is an async operation, so immedietly after call
                            //currentUser might be still null
                            setCurrentUser(user, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    if (!finalFirebaseAuth.getCurrentUser().isEmailVerified() && !currentUser.getUser().getRole().equals(Role.ADMIN)){
                                        //check if email was verified
                                        if (failureListener != null)
                                            failureListener.onFailure(new InstantiationException("warning email has not been verified yet"));
                                        return;
                                    }

                                    if (successListener != null)
                                        successListener.onSuccess(currentUser.getUser());
                                }
                            });

                        }else{
                            if (failureListener != null)
                                failureListener.onFailure(new UnsupportedOperationException("error during user sign in process"));
                        }
                    }
                });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
        currentUser = null;
    }

    public static void registerObserver(Observer observer) throws RuntimeException {
        if (observer == null) {
            throw new RuntimeException("Couldn't register observer for the current user");
        }
        currentUser.registerUserObserver(observer);
    }

    public static void resetPassword(FirebaseAuth firebaseAuth, final EditText email_input_field, final Context context) {
        String email = email_input_field.getText().toString().toLowerCase().trim();

        try {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Email address associated with an existing account
                            if (task.isSuccessful()) {
                                Util.printToast(context, "Password reset email sent to " +
                                    email_input_field.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG);
                                    email_input_field.setText(""); //Clear input field: visual feedback to user in addition to the toast msg
                                //Email address not associated with an existing account
                            } else {
                                Util.printToast(context, task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });

        /*An illedgal argument exception is thrown when the email address which firebases checks to eventually
          send a reset email is not filled ("Given String is empty or null")*/
        } catch (java.lang.IllegalArgumentException e) {
            Util.printToast(context, "Please, enter your email address",Toast.LENGTH_LONG);
        }
    }

    //Just for testing, callback methods are difficult to unit test
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Future<User> signupUser(FirebaseAuth firebaseAuth, String passwrd, String email, final String username, final Role role,
                                           Executor ctx) {
        final CompletableFuture<User> completableFuture = new CompletableFuture<>();
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseAuth finalFirebaseAuth = firebaseAuth;
        firebaseAuth.createUserWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(finalFirebaseAuth.getCurrentUser());
                            user.setUsername(username);
                            user.setRole(role);
                            setCurrentUser(user,null);
                            completableFuture.complete(user);
                        }else{
                            completableFuture.complete(null);
                        }
                    }
                });

        return completableFuture;
    }
}
