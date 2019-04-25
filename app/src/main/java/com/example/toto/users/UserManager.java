package com.example.toto.users;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toto.sessions.Session;
import com.example.toto.sessions.Status;
import com.example.toto.subjects.Subject;
import com.example.toto.subjects.SubjectManager;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.Observer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

//Use the UserManager to manage the state of the logged-in user
public class UserManager {
    private static UserController currentUser;
    private static UserDatabaseHelper userDb = null;

    //acts as an initializer
    private static void initCurrentUser(final User user,@NonNull final OnSuccessListener listener,@NonNull final OnFailureListener failureListener) throws RuntimeException {
        if (user == null)
            throw new RuntimeException("initialization user is null");
        //retrieve info from users collection
        getDbInstance().getById(user.getId(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TUTOR_APP", "DocumentSnapshot data: " + document.getData());
                        currentUser = new UserController(new User(document));
                        listener.onSuccess(currentUser);
                    } else {
                        currentUser = new UserController(user);
                        //save the current user in the db, since it wasn't recorded yet
                        getDbInstance().upsert(user, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.onSuccess(currentUser);
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                failureListener.onFailure(e);
                            }
                        });
                    }
                } else {
                    failureListener.onFailure(new InstantiationException("error during user initialization"));
                    Log.d("TUTOR_APP", "user query by Id failed: ", task.getException());
                }
            }
        });
    }

    //When sign up or signIn the initCurrentUser method will be called to create the currentUser.
    //Use UnsupportedOperationException for fatal errors,  Use InstantiationException for warnings
    public static void signupUser(FirebaseAuth firebaseAuth, String passwrd, String email, final String username, final Role role,
                                  Activity ctx,@NonNull final OnSuccessListener successListener,@NonNull final OnFailureListener failureListener) throws RuntimeException {
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

                                                if (role.equals(Role.TUTOR))
                                                    user.setStatus(Status.PENDING);

                                                initCurrentUser(user, new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        successListener.onSuccess(currentUser.getUser());
                                                    }
                                                },failureListener);
                                            }else{
                                                //problems with sending verification email
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
    public static void signinUser(FirebaseAuth firebaseAuth, String passwrd, String email,
                                  Activity ctx,@NonNull final OnSuccessListener successListener,@NonNull final OnFailureListener failureListener) {
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
                            //ATTENTION: initCurrentUser is an async operation, so immedietly after call
                            //currentUser might be still null
                            initCurrentUser(user, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //check if email wasn't verified unless the user is an admin
                                    if (!finalFirebaseAuth.getCurrentUser().isEmailVerified() &&
                                            !currentUser.getUser().getRole().equals(Role.ADMIN)){
                                        if (failureListener != null)
                                            failureListener.onFailure(new InstantiationException("warning email has not been verified yet"));
                                        return;
                                    }
                                    //check if tutor has been accepted
                                    if (currentUser.getUser().getRole().equals(Role.TUTOR)
                                            && currentUser.getUser().getStatus().equals(Status.PENDING)){
                                        failureListener.onFailure(new InstantiationException("your tutor registration request is still pending"));
                                        return;
                                    }

                                    if (currentUser.getUser().getRole().equals(Role.TUTOR)
                                            && currentUser.getUser().getStatus().equals(Status.DECLINED)){
                                        failureListener.onFailure(new InstantiationException("your tutor registration request has been declined"));
                                        return;
                                    }

                                    successListener.onSuccess(currentUser.getUser());
                                }
                            },failureListener);

                        }else{
                            failureListener.onFailure(new UnsupportedOperationException("error during user sign in process"));
                        }
                    }
                });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
        currentUser = null;
    }

    public static UserController getUserInstance(){
        if (currentUser!=null){
            return currentUser;
        }
        return currentUser;
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
        } catch (IllegalArgumentException e) {
            Util.printToast(context, "Please, enter your email address",Toast.LENGTH_LONG);
        }
    }

    /**
     * Load the profile picture of a currently logged-in user (if any) in the correct container. If the
     * user has not uploaded a profile picture, a gender neutral avatar is rendered instead (vy default)
     * @param view_container Container for the profile picture
     * @param activity_context Context of the current activity
     */
    public static void getProfilePicture(final ImageView view_container, final Context activity_context) {
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().
                child("images/profile_picture_"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(activity_context).load(uri).into(view_container);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // File not found. Do nothing, because a default gender-neutral avatar will be displayed instead
            }
        });
    }

    public static void addSession(@NonNull String userId, final Session session,@NonNull final OnFailureListener listener){
        if (session == null || userId.equals(""))
            return;

        getDbInstance().getById(userId, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    User user = new User(task.getResult());
                    addSession(user,session);
                }else{
                    listener.onFailure(new IllegalStateException());
                }
            }
        });
    }

    public static void addSession(User user, Session session){
        //TODO add code, update user struct with new session
    }

    //add session to current user
    public static void addSession(Session session){
        addSession(currentUser.getUser(), session);
    }

    //Add already existing subject to current user
    public static void addSubject(Subject subject, OnSuccessListener success, OnFailureListener error){
        currentUser.getUser().addSubject(subject);
        new UserDatabaseHelper().upsert(currentUser.getUser(),success,error);
    }

    /**
     * Add subjects to current user
     * @param subjects Subjects to be added to the current user
     * @param success Listener called when upsert task completed successfully
     * @param error Listener called when upsert task was not successfully completed
     */
    public static void addSubjects(Map<String,Subject> subjects, final OnSuccessListener success, final OnFailureListener error){
        currentUser.getUser().setSubjects(subjects);
        new UserDatabaseHelper().upsert(currentUser.getUser(),success,error);
    }

    //Remove subject from current user
    public static void removeSubject(Subject subject, OnSuccessListener success, OnFailureListener error){
        currentUser.getUser().removeSubject(subject);
        new UserDatabaseHelper().upsert(currentUser.getUser(),success,error);
    }

    //
    /*
        ADMIN COMMANDS
     */
    public static void acceptTutorRequest(User user, OnSuccessListener<Void> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        user.setStatus(Status.ACCEPTED);
        new UserDatabaseHelper().upsert(user, success, error);
    }

    public static void declineTutorRequest(User user, OnSuccessListener<Void> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        user.setStatus(Status.DECLINED);
        new UserDatabaseHelper().upsert(user, success, error);
    }

    public static void retrievePendingTutors(OnSuccessListener<QuerySnapshot> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        getDbInstance().getPendingTutors(success,error);
    }

    //Add new subject to the subject collection
    public static void createSubject(Subject subject, OnSuccessListener<Void> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        SubjectManager.addNewSubject(subject, success, error);
    }

    //Remove subject from the database
    public static void deleteSubject(Subject subject, OnSuccessListener<Void> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        SubjectManager.removeSubject(subject, success, error);
    }

    //Update subject to the subject collection
    public static void updateSubject(Subject subject, OnSuccessListener<Void> success, OnFailureListener error){
        if (!currentUser.getUser().getRole().equals(Role.ADMIN)){
            //not admin, could throw an exception
            return;
        }
        SubjectManager.addNewSubject(subject, success, error);
    }

    private static UserDatabaseHelper getDbInstance(){
        if (userDb!=null)
            return userDb;
        userDb = new UserDatabaseHelper();
        return userDb;
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
                            //setCurrentUser(user,null,null);
                            completableFuture.complete(user);
                        }else{
                            completableFuture.complete(null);
                        }
                    }
                });

        return completableFuture;
    }
}