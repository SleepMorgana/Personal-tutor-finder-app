package com.example.toto.users;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.toto.interfaces.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private static void setCurrentUser(final User user) throws RuntimeException {
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
                } else {
                    Log.d("", "user query by Id failed: ", task.getException());
                }
            }
        });
    }

    //When sign up or signIn the setCurrentUser method will be called to create the currentUser.
    public static void signupUser(FirebaseAuth firebaseAuth, String passwrd, String email, final String username, final Role role,
                                  Activity ctx, final OnCompleteListener<AuthResult> listener) throws RuntimeException {
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
                            setCurrentUser(user);
                        }
                        if (listener != null)
                            listener.onComplete(task);
                    }
                });

    }

    public static void signinUser(FirebaseAuth firebaseAuth, String passwrd, String email,
                                  Activity ctx, final OnCompleteListener<AuthResult> listener) {
        if (firebaseAuth == null)
            firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseAuth finalFirebaseAuth = firebaseAuth;
        firebaseAuth.signInWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(finalFirebaseAuth.getCurrentUser());
                            setCurrentUser(user);
                        }
                        if (listener != null)
                            listener.onComplete(task);
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

    //Just for testing, callback methods are difficult to unit test
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Future<User> signupUserFuture(FirebaseAuth firebaseAuth, String passwrd, String email, final String username, final Role role,
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
                            setCurrentUser(user);
                            completableFuture.complete(user);
                        }else{
                            completableFuture.complete(null);
                        }
                    }
                });

        return completableFuture;
    }
}
