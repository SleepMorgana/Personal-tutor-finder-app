package com.example.toto.users;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Observer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class UserController {
    private User user;

    public UserController(User user) {
        this.user = user;
    }

    public void registerUserObserver(Observer observer) throws RuntimeException {
        if (observer == null) {
            throw new RuntimeException("Couldn't register observer for the ");
        }
        user.addObserver(observer);
    }
}