package com.example.toto.subjects;

import com.example.toto.interfaces.DatabaseHelper;

public class SubjectDatabaseHelper extends DatabaseHelper<Subject> {
    private static final String COLLECTION_NAME = "app_subjects";
    private static final String TAG = "SubjectDatabaseHelper";

    public SubjectDatabaseHelper() {
        super(COLLECTION_NAME, TAG);
    }

}
