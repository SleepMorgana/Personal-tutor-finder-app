package com.example.toto.sessions;

import android.support.annotation.NonNull;

import com.example.toto.users.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class SessionManager {
    private final static SessionDatabaseHelper db = new SessionDatabaseHelper();

    public static void retrieveSessionById(String id, OnCompleteListener listener){
        if (id == null || id.equals(""))
            return;
        db.getById(id,listener);
    }

    public static void addNewSession(Session session,@NonNull OnSuccessListener<Void> success, @NonNull OnFailureListener error){
        if (session==null)
            return;
        db.upsert(session, success, error);
        //TODO add session to both sender and target
        //sender
        UserManager.addSession(session);
        //target
        UserManager.addSession(session.getTarget(),session,error);
    }
}
