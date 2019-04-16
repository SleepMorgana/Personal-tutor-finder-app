package com.example.toto.users;


import com.example.toto.interfaces.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDatabaseHelper extends DatabaseHelper<User> {
    private static final String COLLECTION_NAME = "app_users";
    private static final String TAG = "UserDatabaseHelper";

    public UserDatabaseHelper() {
        super(COLLECTION_NAME,TAG);
    }

    @Override
    public void getById(String id, OnCompleteListener<DocumentSnapshot> callback) {
        super.getById(id, callback);
    }

    @Override
    public void getAll(OnSuccessListener<QuerySnapshot> successListener, OnFailureListener failureListener) {
        //Probably not a good idea in the case of users, need a more limiting query methods
    }
}
