package com.example.toto.sessions;

import com.example.toto.interfaces.DatabaseHelper;

public class SessionDatabaseHelper extends DatabaseHelper<Session> {
    private static final String COLLECTION_NAME = "app_sessions";
    private static final String TAG = "SessionDatabaseHelper";

    public SessionDatabaseHelper() {
        super(COLLECTION_NAME, TAG);
    }

    //TODO add query methods: getPendingSessions by user
}
