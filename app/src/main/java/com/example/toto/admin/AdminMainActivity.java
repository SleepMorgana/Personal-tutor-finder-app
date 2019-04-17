package com.example.toto.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.toto.R;
import com.example.toto.users.Role;
import com.example.toto.users.User;
import com.example.toto.utils.ListableViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private FrameLayout mContent;
    private LayoutInflater mInflater ;
    private boolean mPageFlag = true;// false=tutorPage

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContent = (FrameLayout) findViewById(R.id.content);
        mInflater = (LayoutInflater) getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTutorPage();
    }

    private void setTutorPage(){
        if (!mPageFlag){
            return;
        }
        //TODO clean previous subject views
        mContent.addView(mInflater.inflate(R.layout.activity_admin_user_list,null));
        ListView listView  = (ListView) mContent.findViewById(R.id.admin_tutor_listview);

        //mock data
        List<User> tutors = new ArrayList<>();
        tutors.add(new User("tutor-1","tutor@example.com", Role.TUTOR, "id-1"));

        listView.setAdapter(new ListableViewAdapter<User>(getBaseContext(),tutors,true));
        mPageFlag=false;
    }

    private void setSubjectPage(){
        if (mPageFlag)
            return;
        //clean previous user views
        if (mContent.findViewById(R.id.admin_tutor_listview)!=null){
            clearContent();
        }

        mPageFlag=true;
    }

    private void clearContent(){
        mContent.removeAllViews();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tutor_page:
                    setTutorPage();

                    return true;
                case R.id.navigation_subject_page:
                    setSubjectPage();
                    return true;
            }
            return false;
        }

    };
}
