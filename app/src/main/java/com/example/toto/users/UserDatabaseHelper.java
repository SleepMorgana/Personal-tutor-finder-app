package com.example.toto.users;


import com.example.toto.interfaces.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDatabaseHelper extends DatabaseHelper<User> {
    private static final String COLLECTION_NAME = "app_users";
    private static final String TAG = "UserDatabaseHelper";

    public UserDatabaseHelper() {
        super(COLLECTION_NAME,TAG);
    }

    @Override
    public void upsert(final User user) {
        super.upsert(user);
    }

    @Override
    public void getById(String id, OnCompleteListener<DocumentSnapshot> callback) {
        super.getById(id, callback);
    }
}
