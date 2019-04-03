package com.example.toto.users;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Observer;

public class UserController {
    private static User currentUser;
    private User user;

    public UserController(User user) {
        this.user = user;
    }

    private static void setCurrentUser(final User user) throws RuntimeException {
        if (user == null)
            throw new RuntimeException("setCurrentUser user is null");
        //TODO retrieve info from users collection
        UserDatabaseHelper.getUserById(user.getId(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("", "DocumentSnapshot data: " + document.getData());
                        currentUser = new User(document);
                    } else {
                        currentUser = user;
                        Log.d("", "No such document");
                    }
                    //TODO save the current user in the db
                    UserDatabaseHelper.upsert(currentUser);
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });
    }

    //When sign up or signIn the setCurrentUser method will be called , from the listener.
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
                                  Activity ctx, final OnCompleteListener<AuthResult> listener) throws RuntimeException {
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

    public static void registerStaticUserObserver(Observer observer) throws RuntimeException {
        if (observer == null) {
            throw new RuntimeException("Couldn't register observer");
        }
        currentUser.addObserver(observer);
    }

    public void registerUserObserver(Observer observer) throws RuntimeException {
        if (observer == null) {
            throw new RuntimeException("Couldn't register observer");
        }
        user.addObserver(observer);
    }
}