package com.example.toto.users.student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alphabetik.Alphabetik;
import com.example.toto.R;
import com.example.toto.subjects.Subject;
import com.example.toto.subjects.SubjectManager;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.utils.CheckboxArrayAdapter;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SearchTutorsActivity extends AppCompatActivity {

    private List<String> checked_subjects;
    private Pair<Map<String, Subject>, Map<String, Boolean>> pairOfMapSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tutors);

        Button search1 = (Button) findViewById(R.id.search_tutors_option1); //Search button to search for tutors based on the student's learning needs
        Button search2_custom = (Button) findViewById(R.id.search_tutors_option2); //Search button to search for tutors on the student's selected subjects

        //Enable the Up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar(); // Get a support ActionBar corresponding to this toolbar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        //Current user
        final User user = UserManager.getUserInstance().getUser();

        // Query all subjects available within the app
        SubjectManager.listSubjects(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final ArrayList<Subject> all_app_subjects = new ArrayList<>(); //List of all subjects available in the database
                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                    Subject subject = new Subject(snapshot);
                    all_app_subjects.add(subject);
                }

                //Handle the case where there is no subjects available within the app yet
                if (all_app_subjects.size() == 0) {
                    Util.printToast(SearchTutorsActivity.this,"There is no subject available yet. Try again later or contact the administrator", Toast.LENGTH_LONG);
                }

                checked_subjects = user.getOrderedSubjects().first;

                /* Populating two maps in a pair:
                   - First map (first elt in pair): Mapping subject names with the corresponding subject object.
                     Precondition: Subject names in the database are unique
                   - Second sorted map (second elt in pair): Mapping subject names with a boolean indicating whether the
                     subject designated by its names is associated with the current user or not
                     Precondition: Subject names in the database are unique
                      NB: Sorted map because the list of all subjects needs to be sorted for the alphabet scroller to work*/
                pairOfMapSubjects = Util.populateMappingUserSubject(checked_subjects, all_app_subjects);

                // Alphabetik implementation
                Alphabetik alphabetik = findViewById(R.id.alphSectionIndex);
                final ListView listView=(ListView)findViewById(R.id.listView);
                final CheckboxArrayAdapter adapter = new CheckboxArrayAdapter(SearchTutorsActivity.this,
                        pairOfMapSubjects.second.keySet().toArray(new String[0]),
                        pairOfMapSubjects.second);
                listView.setAdapter(adapter);
                listView.setItemsCanFocus(false);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //List allows multiple choices

                //Set alphabet relevant with the subjects' names
                String[] alphabet = Util.getCustomAlphabetSet(pairOfMapSubjects.second.keySet());
                alphabetik.setAlphabet(alphabet);

                alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                    @Override
                    public void onItemClick(View view, int position, String character) {
                        List<String> ordered_data = new ArrayList<>(pairOfMapSubjects.first.keySet());
                        Collections.sort(ordered_data);
                        listView.smoothScrollToPosition(Util.getPositionFromData(character, ordered_data));
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Util.printToast(SearchTutorsActivity.this,"Failing to get the list subjects available within the application. Try again later or contact the administrator",Toast.LENGTH_LONG);
                Log.i(Util.TAG, e.getMessage()); //For debugging
            }
        });

        //Search for tutors
        search1.setOnClickListener(new View.OnClickListener() { //Search option 1
            @Override
            public void onClick(View v) {
                //User search criteria = subjects he/she listed as learning needs
                List<String> subjects_id_criteria = new ArrayList<>();
                subjects_id_criteria.addAll(user.getSubjects().keySet());

                // Go to next activity where
                Intent intent = new Intent(SearchTutorsActivity.this, MatchedTutorsActivity.class);
                intent.putStringArrayListExtra("subjects_id", (ArrayList<String>) subjects_id_criteria);
                startActivity(intent);
            }
        });

        search2_custom.setOnClickListener(new View.OnClickListener() { //Search opton 1
            @Override
            public void onClick(View v) {
                //User search criteria = subjects he/she selected in this activity
                Intent intent = new Intent(SearchTutorsActivity.this, MatchedTutorsActivity.class);
                intent.putStringArrayListExtra("subjects_id", (ArrayList<String>) getSubjectsId(pairOfMapSubjects.first));
                startActivity(intent);
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

    private List<String> getSubjectsId(Map<String, Subject> map_name_subject) {
        List<String> res = new ArrayList<>();

        for (Map.Entry<String, Subject> entry:map_name_subject.entrySet()) {
            res.add(entry.getValue().getId());
        }
        return res;
    }
}
