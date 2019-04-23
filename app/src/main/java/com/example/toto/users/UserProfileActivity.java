package com.example.toto.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alphabetik.Alphabetik;
import com.example.toto.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserProfileActivity extends AppCompatActivity{

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Current user
        User user = UserManager.getUserInstance().getUser();

        //Render the user's identity
        updateUserIdentity(user);

        //Render the user's subjects (learning needs for subjects vs tutoring subjects for tutors)
        renderSubjects(user);
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

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_tutor_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Edit button clicked, go to the edit profile activity
            case R.id.editprofile_button_id: //cf. menu folder
                Intent intent = new Intent(UserProfileActivity.this, UserProfileEditActivity.class);
                startActivity(intent);
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Render the current user's identity (i.e. username, email address, profile picture)
     * @param populated_user current logged-in user
     */
    private void updateUserIdentity(User populated_user) {
        /*By default the profile picture is a gender-neutral avatar, unless he/she has uploaded his/her
        own profile picture which must then be displayed instead of the default avatar */
        if (populated_user.getProfile_picture() != null) {
            ImageView profile_pic_view = (ImageView) findViewById(R.id.profile_picture_view_id);
            profile_pic_view.setImageBitmap(populated_user.getProfile_picture());
        }

        //Update navigation menu with the logged-in user's info
        //Username
        TextView text_view = findViewById(R.id.username_profile_id);
        text_view.setText(populated_user.getUsername());
        //Email
        text_view = findViewById(R.id.email_profile_id);
        text_view.setText(populated_user.getEmail());
    }

    /**
     * Render the current user's list of subjects (learning needs for a student, tutoring subjects for a tutor)
     * @param populated_user current logged-in user
     */
    private void renderSubjects(User populated_user) {
        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        final List<String> orderedSubjects = populated_user.getOrderedSubjects();

        // Title of the list depends on the role of the user
        TextView subject_list_title = findViewById(R.id.subject_list_name);
        switch (populated_user.getRole()) {
            case STUDENT:
                subject_list_title.setText("My learning needs:");
                break;
            case TUTOR:
                subject_list_title.setText("The subjects I can teach:");
                break;
        }

        // Display instructions on how to add subjects if the user's subject list is empty
        if (orderedSubjects.size() == 0) {
            TextView instructions = findViewById(R.id.subjects_instructions_id);
            instructions.setText("You don't have specified any subjects yet. Please edit your profile to add subjects");
            //Hide alphabet scroller on the right side
            View alphabetScroller = findViewById(R.id.alphSectionIndex);
            alphabetScroller.setVisibility(View.INVISIBLE);

        // Alphabetik implementation & ListView population
        } else {
            Alphabetik alphabetik = findViewById(R.id.alphSectionIndex);
            listView = findViewById(R.id.listView); //Listview implementation, with SORTED list of DATA
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderedSubjects);
            listView.setAdapter(adapter);

            //Set alphabet relevant with the subjects' names
            String[] alphabet = getCustomAlphabet(orderedSubjects);
            alphabetik.setAlphabet(alphabet);

            alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
                @Override
                public void onItemClick(View view, int position, String character) {
                    String info = " Position = " + position + " Char = " + character;
                    Log.i("View: ", view + "," + info);
                    //Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                    listView.smoothScrollToPosition(getPositionFromData(character, orderedSubjects));
                }
            });
        }
    }

    /**
     * Creates an ordered array of  unique letters corresponding to the letters used as first characters
     * in the items name
     * @param items List of items name
     * @return ordered array of  unique letters corresponding to the letters used as first characters
     * in the items name
     */
    private String[] getCustomAlphabet(List<String> items) {
        Set<String> first_letters = new HashSet<>();
        String[] res;

        for (String item:items) {
            first_letters.add(item.substring(0, 1).toUpperCase());
        }

        res = first_letters.toArray(new String[first_letters.size()]);
        Arrays.sort(res);

        return(res);
    }
}
