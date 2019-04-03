package com.example.toto.users;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDatabaseHelper {
    private static final String COLLECTION_NAME = "app_users";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "UserDatabaseHelper";

    public static void upsert(final User user){
        db.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(user.marshal())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + user.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void getUserById(String id, OnCompleteListener<DocumentSnapshot> callback){
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        docRef.get().addOnCompleteListener(callback);
    }
}
