package com.example.toto.users.student;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alphabetik.Alphabetik;
import com.bumptech.glide.Glide;
import com.example.toto.R;
import com.example.toto.users.User;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class ViewTutorProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tutor_profile);

        //Enable the Up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar(); // Get a support ActionBar corresponding to this toolbar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        //Retrieve data sent from previous activity (i.e. tutor user selected in previous activity in this case)
        User selected_tutor = Objects.requireNonNull(getIntent().getExtras()).getParcelable("selected_tutor");

        //Retrieve and display the tutor's profile picture (if any) + display his username
        updateUserIdentity(Objects.requireNonNull(selected_tutor));

        //Render the tutor's subjects
        renderSubjects(selected_tutor);
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

    /**
     * Render the tutor's identity (i.e. username, profile picture)
     * NB: The user's email is not displayed here for privacy purposes. A student can communicate with a
     * tutor using the messaging system
     * @param tutor_user tutor user
     */
    private void updateUserIdentity(User tutor_user) {
        /*By default the profile picture is a gender-neutral avatar, unless he/she has uploaded his/her
        own profile picture which must then be displayed instead of the default avatar */
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/profile_picture_" +
                tutor_user.getId());

        // ImageView in your Activity
        final ImageView imageView = (ImageView) findViewById(R.id.profile_picture_view_id);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("CECILE", "Nor found");
            }
        });

        //Update the layout with the logged-in user's info
        //Username
        TextView text_view = findViewById(R.id.username_profile_id);
        text_view.setText(tutor_user.getUsername());
    }

    /**
     * Render the current user's list of subjects (tutoring subjects for a tutor)
     * @param populated_user tutor user
     */
    private void renderSubjects(User populated_user) {
        final ListView listView = findViewById(R.id.listView); //Listview implementation, with SORTED list of DATA
        Alphabetik alphabetik = findViewById(R.id.alphSectionIndex);
        //Alphabetically ordered list of learning needs (student) or tutoring subjects (tutors)
        final Pair<List<String>, String[]> orderedSubjects = populated_user.getOrderedSubjects();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderedSubjects.first);
        listView.setAdapter(adapter);

        //Set alphabet relevant with the subjects' names
        alphabetik.setAlphabet(orderedSubjects.second);

        alphabetik.onSectionIndexClickListener(new Alphabetik.SectionIndexClickListener() {
            @Override
            public void onItemClick(View view, int position, String character) {
                String info = " Position = " + position + " Char = " + character;
                Log.i("View: ", view + "," + info);
                //Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
                listView.smoothScrollToPosition(Util.getPositionFromData(character, orderedSubjects.first));
            }
        });
    }
}
