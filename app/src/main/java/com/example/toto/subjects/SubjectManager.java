package com.example.toto.subjects;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

public class SubjectManager {
    private static final SubjectDatabaseHelper db = new SubjectDatabaseHelper();

    //create and updates
    public static void addNewSubject(Subject subject,@NonNull final OnSuccessListener<Void> successListener,@NonNull final OnFailureListener failureListener){
        if (subject==null){
            failureListener.onFailure(new NullPointerException("subject instance is null"));
            return;
        }
        db.upsert(subject, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //add code here if the method needs expanding
                successListener.onSuccess(aVoid);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //add code here
                failureListener.onFailure(e);
            }
        });
    }

    public static void removeSubject(Subject subject,@NonNull final OnSuccessListener<Void> successListener,@NonNull final OnFailureListener failureListener){
        if (subject==null){
            failureListener.onFailure(new NullPointerException("subject instance is null"));
            return;
        }
        //TODO A subject must also be deleted from user records
        db.deleteById(subject.getId(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //add user batch operations
                successListener.onSuccess(aVoid);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureListener.onFailure(e);
            }
        });
    }

    public static void listSubjects(@NonNull final OnSuccessListener<QuerySnapshot> successListener,@NonNull final OnFailureListener failureListener){
        db.getAll(successListener,failureListener);
    }

    public static void retrieveSubjectById(String id,@NonNull OnCompleteListener listener){
        if (id==null || id =="")
            return;
        db.getById(id,listener);
    }

    public static void retrieveSubjectsByName(String name, @NonNull final OnSuccessListener<QuerySnapshot> successListener,@NonNull final OnFailureListener failureListener){
        if (name==null || name=="")
            return;
        db.getByName(name,successListener,failureListener);
    }
}
