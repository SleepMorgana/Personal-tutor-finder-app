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
import android.widget.Toast;

import com.example.toto.R;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.utils.DoubleActionListViewAdapter;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AdminMainActivity extends AppCompatActivity implements Observer {
    private FrameLayout mContent;
    private LayoutInflater mInflater ;
    private boolean mPageFlag = true;// false=tutorPage
    private AdminMainActivity mActivity = this;
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
        final ListView listView  = (ListView) mContent.findViewById(R.id.admin_tutor_listview);


        fetchTutorData(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> tutors = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    User user = new User(doc);
                    user.addObserver(mActivity);
                    tutors.add(user);
                    listView.setAdapter(new DoubleActionListViewAdapter(getBaseContext(), tutors, true,
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //o = element selected
                                    User user = (User) o;
                                    UserManager.acceptTutorRequest(user, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //OK
                                            Util.printToast(mActivity,"Tutor request was accepted",Toast.LENGTH_SHORT);
                                        }
                                    }, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error
                                            Util.printToast(mActivity,"There was an error accepting tutor request",Toast.LENGTH_SHORT);
                                        }
                                    });
                                }
                            }, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            User user = (User) o;
                            UserManager.declineTutorRequest(user, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //OK
                                    Util.printToast(mActivity,"Tutor request was declined",Toast.LENGTH_SHORT);
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Util.printToast(mActivity,"There was an error declining tutor request",Toast.LENGTH_SHORT);
                                }
                            });
                        }
                    }));
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Util.printToast(mActivity,"There were issues loading the tutors list", Toast.LENGTH_SHORT);
            }
        });


        mPageFlag=false;
    }

    //Pending tutors
    private void fetchTutorData(OnSuccessListener<QuerySnapshot> success, OnFailureListener error){
        UserManager.retrievePendingTutors(success, error);
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

    @Override
    public void update(Observable o, Object arg) {
        //Update UI

        //Update tutor page, TODO: still doesn't refreshes
        if (o instanceof User){
            mPageFlag = true;
            setTutorPage();
            //instead of fetching online maybe just update the local list
        }

    }
}
