package com.example.toto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toto.users.User;
import com.example.toto.users.UserManager;


public class StudentProfileActivity extends AppCompatActivity{

    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        //Showing back button
        if(getActionBar() != null) { //null check
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        }

        //Data sent from previous activity (i.e. currently logged-in user)
        Intent intent = getIntent();
        user = intent.getParcelableExtra("myCurrentUser");

        /*By default the profile picture is a gender-neutral avatar, unless he/she has uploaded his/her
        own profile picture which must then be displayed instead of the default avatar */
        UserManager.getProfilePicture((ImageView) findViewById(R.id.profile_picture_view_id), this);

        //Update navigation menu with the logged-in user's info
        //Username
        TextView text_view = findViewById(R.id.username_profile_id);
        text_view.setText(user.getUsername());
        //Email
        text_view = findViewById(R.id.email_profile_id);
        text_view.setText(user.getEmail());
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
            case R.id.editprofile_button_id:
                Intent intent = new Intent(StudentProfileActivity.this, StudentProfileEditActivity.class);
                //Sent data: currently logged-in user
                intent.putExtra("myCurrentUser", user);
                startActivity(intent);

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
