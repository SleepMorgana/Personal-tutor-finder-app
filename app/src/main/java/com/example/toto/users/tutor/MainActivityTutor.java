package com.example.toto.users.tutor;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toto.R;
import com.example.toto.SignInSignUp;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.users.UserProfileActivity;
import com.example.toto.utils.DateListViewAdapter;
import com.example.toto.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//This is going to be used as the home activity of the application for tutors
public class MainActivityTutor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tutor);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        Button sign_out_button = findViewById(R.id.log_out_button_id); //Sign out button
        TextView info_sessions = (TextView) findViewById(R.id.intro_future_sessions_id);

        //Enable the Up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        user = UserManager.getUserInstance().getUser();

        /*By default the profile picture is a gender-neutral avatar. If the logged-in user doesn't have
        a profile picture associated to his/her profile, this must be displayed instead of the default avatar*/
        if (user.getProfile_picture() != null) {
            ImageView profile_pic_view = (ImageView) headerView.findViewById(R.id.profile_pic_id);
            profile_pic_view.setImageBitmap(user.getProfile_picture());
        }

        //Update navigation menu with the logged-in user's info
        //Username
        TextView text_view = headerView.findViewById(R.id.username_nav_id);
        text_view.setText(user.getUsername());
        //Email
        text_view = headerView.findViewById(R.id.email_navigation_id);
        text_view.setText(user.getEmail());

        //Everytime the user clicks on the header of the navbar, he/she is redirected to its profile page
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityTutor.this, UserProfileActivity.class);
                //Data sent: currently logged-in user
                intent.putExtra("myCurrentUser", user);
                startActivity(intent);
            }
        });

        //Sign out action
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.signOut();

                //Go back to sign in / sign out activity
                Intent intent = new Intent(MainActivityTutor.this, SignInSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        //Display N upcoming sessions
        renderNUpcommingSessions(info_sessions);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //When BACK BUTTON is pressed, the activity on the stack is restarted
        /*By default the profile picture is a gender-neutral avatar. If the logged-in user doesn't have
        a profile picture associated to his/her profile, this must be displayed instead of the default avatar*/
        if (user.getProfile_picture() != null) {
            ImageView profile_pic_view = (ImageView) headerView.findViewById(R.id.profile_pic_id);
            profile_pic_view.setImageBitmap(user.getProfile_picture());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Transform a list of dates on a list of 2-uple(Date in day/month/year, Time in HH:MM:SS)
     * @param dates_list list of dates
     * @return Corresponding list of 2-uple(Date in day/month/year, Time in HH:MM:SS)
     */
    private List<Pair<String, String>> transformListOfDates(List<Date> dates_list) {
        List<Pair<String, String>> res = new ArrayList<>();
        Pair<String, String> temp;

        for (Date d: dates_list) {
            temp = new Pair<>(new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH).format(d),
                    new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(d));

            res.add(temp);
        }

        return res;
    }

    /**
     * Render the N upcoming sessions" dates on screen (if any)
     * @param info_sessions text info about the N upcoming sessions
     */
    private void renderNUpcommingSessions(TextView info_sessions) {
        ListView listView;

        //Get up to N next future sessions
        List<Date> upcoming_sessions = user.getNUpcomingSessionDates(Util.NB_UPCOMING_SESSION);

        if (upcoming_sessions.size() == 0) { //the user has no upcoming sessions
            info_sessions.setText(R.string.no_upcoming_sessions_txt);
        } else {
            info_sessions.setText(R.string.upcoming_sessions_txt);

            //Transform List<Date> into List<Pair<String(ie Date), String(ie Time)>> to use DateListViewAdapter
            List<Pair<String, String>> upcoming_sessions_dates_list = transformListOfDates(upcoming_sessions);

            listView = findViewById(R.id.listView);
            listView.setAdapter(new DateListViewAdapter(getBaseContext(), upcoming_sessions_dates_list));
        }

    }
}
