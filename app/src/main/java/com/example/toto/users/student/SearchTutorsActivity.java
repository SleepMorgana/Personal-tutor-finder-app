package com.example.toto.users.student;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alphabetik.Alphabetik;
import com.example.toto.R;
import com.example.toto.subjects.Subject;
import com.example.toto.subjects.SubjectManager;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.utils.CheckboxArrayAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchTutorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tutors);

        //Current user
        User user = UserManager.getUserInstance().getUser();

        /*SubjectManager.listSubjects(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final ArrayList<String> all_app_subjects = new ArrayList();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Subject subject = new Subject(snapshot);
                    all_app_subjects.add(subject.getName());
                }
                //continue
                //List<String> checked_subjects = user.getOrderedSubjects();
                List<String> checked_subjects = new ArrayList<>();
                checked_subjects.add("English Literature"); checked_subjects.add("Italian Language"); checked_subjects.add("C++ Programming");

                //Populating a map (all subjects -> is(un)checked) for a user
                Map<String, Boolean> mapping_user_subjects = populateMappingUserSubject(checked_subjects, all_app_subjects);

                Collections.sort(all_app_subjects); //Sort the list of all subjects (necessary for the alphabet scroller to work)

                // Alphabetik implementation
                Alphabetik alphabetik = findViewById(R.id.alphSectionIndex);
                final ListView listView=(ListView)findViewById(R.id.listView);
                final CheckboxArrayAdapter adapter = new CheckboxArrayAdapter(SearchTutorsActivity.this, all_app_subjects, mapping_user_subjects);
                listView.setAdapter(adapter);
                listView.setItemsCanFocus(false);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //List allows multiple choices

                //Set alphabet relevant with the subjects' names
                String[] alphabet = getCustomAlphabet(all_app_subjects);
                alphabetik.setAlphabet(alphabet);

                alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                    @Override
                    public void onItemClick(View view, int position, String character) {
                        listView.smoothScrollToPosition(getPositionFromData(character, all_app_subjects));
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CECILE", "FATAL");
            }
        });*/
    }

    private String[] getCustomAlphabet(List<String> items) {
        Set<String> first_letters = new HashSet<>();
        String[] res;

        for (String item:items) {
            first_letters.add(item.substring(0, 1).toUpperCase());
        }

        res = first_letters.toArray(new String[0]);
        Arrays.sort(res);

        return(res);
    }

    /**
     * Constructs a map (all subjects -> is(un)checked) for a user
     * @param user_subjects Array of subjects( names associated with a user
     * @param all_subjects List of all the subjects' names available in the app
     * @return a map (all subjects -> is(un)checked) for a user
     */
    private Map<String, Boolean> populateMappingUserSubject(List<String> user_subjects, List<String> all_subjects) {
        Map<String, Boolean> res = new HashMap<>();

        //Initial population of the map res: by default all the subjects
        for (String item:all_subjects) {
            res.put(item, false);
        }

        //Update res: set to true (i.e. checked) the subjects associated with the user
        for (String item:user_subjects) {
            res.put(item, true); //If key-value already exists, value is overwritten
        }

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
