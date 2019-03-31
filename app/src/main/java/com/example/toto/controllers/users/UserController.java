package com.example.toto.controllers.users;

import android.app.Activity;
import android.content.Context;

import com.example.toto.database.users.User;
import com.example.toto.database.users.UserDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Observable;
import java.util.Observer;

public class UserController{
    private static User currentUser;
    private static Observer updateListener = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            //Called When the there's a change in the currentUser
        }
    };
    //TODO add list of listeners

    public static void setCurrentUser(User user) throws RuntimeException{
        if (user==null)
            throw new RuntimeException("setCurrentUser user is null");
        //TODO retrieve info from users collection
        currentUser = UserDatabaseHelper.getUserById(user.getId());
        if (currentUser==null)
            throw new RuntimeException("couldn't retrieve the current user");

        currentUser.addObserver(updateListener);
        //TODO save the current user in the db
        UserDatabaseHelper.upsert(currentUser);
    }

    //When sign up or signIn the setCurrentUser method will be called , from the listener.
    public static void signupUser(FirebaseAuth firebaseAuth, String passwrd, String email, Activity ctx, OnCompleteListener<AuthResult> listener) throws RuntimeException{
        if (firebaseAuth==null)
            firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx,listener);

    }

    public static void signinUser(FirebaseAuth firebaseAuth, String passwrd, String email, Activity ctx, OnCompleteListener<AuthResult> listener) throws RuntimeException{
        if (firebaseAuth==null)
            firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, passwrd)
                .addOnCompleteListener(ctx,listener);
    }

    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

}
