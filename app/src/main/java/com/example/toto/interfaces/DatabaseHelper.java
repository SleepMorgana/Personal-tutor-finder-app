package com.example.toto.interfaces;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DatabaseHelper<T extends Storable> {
    private static String COLLECTION_NAME;// collection where the entity is situated
    private static FirebaseFirestore db;
    private static String TAG;

    public DatabaseHelper(String collectionName, String tag){
        COLLECTION_NAME = collectionName;
        db = FirebaseFirestore.getInstance();
        TAG = tag;
    }

    //if the entity is present in the database update the record, else create new record
    public void upsert(final T obj){
        db.collection(COLLECTION_NAME)
                .document(obj.getId())
                .set(obj.marshal())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + obj.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    //get entity by document id
    public void getById(String id, OnCompleteListener<DocumentSnapshot> callback){
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        docRef.get().addOnCompleteListener(callback);
    }
}
