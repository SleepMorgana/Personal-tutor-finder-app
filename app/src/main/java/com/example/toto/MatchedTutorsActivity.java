package com.example.toto;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alphabetik.Alphabetik;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatchedTutorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_tutors_activiyt);

        //Enable the Up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar(); // Get a support ActionBar corresponding to this toolbar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        //Get data sent from previous activity
        List<String> subjects_id_searched = getIntent().getExtras().getStringArrayList("subjects_id");

        //Retrieve tutors teaching at least one of subjects from subjects_id_searched
        getMatchedTutors(subjects_id_searched, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Map<User, List<String>> matched_tutors = (Map<User, List<String>>) o;
                final List<String> tutors_with_matched_subjects = new ArrayList<>();

                for (Map.Entry<User, List<String>> entry : matched_tutors.entrySet()) {
                    tutors_with_matched_subjects.add(entry.getKey().getUsername() + ". Matched subjects: " + entry.getValue().toString());
                }

                //To ontinue here //TODO
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MatchedTutorsActivity.this, android.R.layout.simple_list_item_1, tutors_with_matched_subjects);
                final ListView listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

                //Set alphabet relevant with the subjects' names
                Alphabetik alphabetik = findViewById(R.id.alphSectionIndex);
                String[] alphabet = getCustomAlphabet(tutors_with_matched_subjects);
                alphabetik.setAlphabet(alphabet);

                alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                    @Override
                    public void onItemClick(View view, int position, String character) {
                        String info = " Position = " + position + " Char = " + character;
                        Log.i("View: ", view + "," + info);
                        //Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                        listView.smoothScrollToPosition(getPositionFromData(character, tutors_with_matched_subjects));
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Util.printToast(MatchedTutorsActivity.this,"There were issues loading" +
                        " the list of tutors with subjects. Try again later or contact the administrator", Toast.LENGTH_SHORT);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Map<User, List<String>> getMatchedTutors(final List<String> subject_id, final OnSuccessListener successListener, OnFailureListener failureListener) {
        final Map<User, List<String>> res = new HashMap<>();

        UserManager.retrieveTutorsWithSubjects(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Only retain tutors that match the user criteria (subjects in the user
                // learning needs pr subjects selected by the user)
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    User user = new User(doc);

                    List<String> matched_subjects = new ArrayList();

                    // Iterate over the list of subjects in search criteria
                    for (String subject_id_item:subject_id) {
                        if (user.getSubjects().containsKey(subject_id_item)) {
                            matched_subjects.add(user.getSubjects().get(subject_id_item).getName());
                        }
                    }

                    res.put(user, matched_subjects);
                }
                successListener.onSuccess(res);
            }
        },failureListener);

        return res;
    }

    private String[] getCustomAlphabet(List<String> items) {
        Set<String> first_letters = new HashSet<>();
        String[] res;

        for (String item:items) {
            first_letters.add(item.substring(0, 1).toUpperCase());
        }

        res = first_letters.toArray(new String[0]);
        Arrays.sort(res);

        return res;
    }

    private int getPositionFromData(String character, List<String> orderedData) {
        int position = 0;
        for (String s : orderedData) {
            String letter = "" + s.charAt(0);
            if (letter.equals("" + character)) {
                return position;
            }
            position++;
        }
        return 0;
    }
}
