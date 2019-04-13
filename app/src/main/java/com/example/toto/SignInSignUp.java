package com.example.toto;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.toto.users.Role;
import com.example.toto.users.UserManager;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SignInSignUp extends AppCompatActivity {
    private static FirebaseAuth mAuth; // firebase authenticator
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);*/


        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        populateViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void startMainActivity(){
        // Go to home activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //user has already logged in recently
            //TODO go to next activity
            //startMainActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.signOut();
    }

    /**
     * Creates the fragments and sets it to ViewPager
     */
    private void populateViewPager() {
        TabDetails tab;
        tab = new TabDetails("Sign In", PlaceholderFragment.newInstance(R.layout.sign_in_fragment));
        mSectionsPagerAdapter.addFragment(tab);
        tab = new TabDetails("Sign Up", PlaceholderFragment.newInstance(R.layout.sign_up_fragment));
        mSectionsPagerAdapter.addFragment(tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         * TODO we may want to declare vars for the different error messages
         */
        private static final String ARG_LAYOUT = "layout";
        private final String TAG = "TUTOR_APP";
        private OnCompleteListener signinAction = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, TODO go to next activity
                    Log.d(TAG, "signInUserWithEmail:success");
                    startMainActivity();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInUserWithEmail:failure", task.getException());
                    Util.printToast(getActivity(), "Authentication failed.Please try again.",Toast.LENGTH_SHORT);
                }
            }
        };
        private OnCompleteListener signupAction = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign up success, TODO go to next activity
                    Log.d(TAG, "createUserWithEmail:success");
                    startMainActivity();
                    //in case an additional user record may be needed to store the username
                    //currentUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());
                } else {
                    // If sign up fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Util.printToast(getActivity(), "Sign up failed. Please try again.",Toast.LENGTH_SHORT);
                }
            }
        };

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given layout.
         */
        public static PlaceholderFragment newInstance(int layout) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_LAYOUT, layout);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            assert getArguments() != null;
            final View layout = inflater.inflate(getArguments().getInt(ARG_LAYOUT), container, false);
            int currentLayout = getArguments().getInt(ARG_LAYOUT);

            //sign in fragment
            if (currentLayout == R.layout.sign_in_fragment) {
                Button signIn = (Button) layout.findViewById(R.id.sign_in_button_id);
                signIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = ((EditText) layout.findViewById(R.id.input_sign_in_email)).getText().toString().toLowerCase().trim();
                        String password = ((EditText) layout.findViewById(R.id.passwd_input_sign_in_id)).getText().toString().trim();
                        Log.d(TAG, "SIGN_IN Clicked");
                        UserManager.signinUser(mAuth,password,email,getActivity(),signinAction);
                    }
                });

                Button passButton = (Button) layout.findViewById(R.id.forgotten_passwd_id);
                passButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "FORGOT_PSSWD Clicked");
                        Intent intent = new Intent(getActivity(), ForgottenPasswordActivity.class);
                        startActivity(intent);
                    }
                });
            }

            //sign up fragment
            if (currentLayout == R.layout.sign_up_fragment) {
                Button signUp = (Button) layout.findViewById(R.id.sign_up_button_id);
                RadioButton studentRadioButton = (RadioButton) layout.findViewById(R.id.radioButton_student_id);
                RadioButton tutorRadioButton = (RadioButton) layout.findViewById(R.id.radioButton_tutor_id);
                final Role[] role = {null};


                signUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String username = ((EditText) layout.findViewById(R.id.input_username_sign_up_id)).getText().toString().trim();
                        String email = ((EditText) layout.findViewById(R.id.input_email_sign_up_id)).getText().toString().toLowerCase().trim();
                        String password = ((EditText) layout.findViewById(R.id.input_passwd_sign_up_id)).getText().toString().trim();
                        String confirmPassword = ((EditText) layout.findViewById(R.id.input_confirm_passwd_sign_up_id)).getText().toString().trim();

                        Log.d(TAG, "SIGN_UP Clicked");

                        //check fields
                        if (username == "" || email == "" || password == "") {
                            Util.printToast(getActivity(), "Sign up failed: empty fields", Toast.LENGTH_SHORT);
                            return;
                        }

                        if (!password.equals(confirmPassword)) {
                            Util.printToast(getActivity(), "Sign up failed: password and confirmation mismatch", Toast.LENGTH_SHORT);
                            return;
                        }

                        if (role[0]==null){
                            Util.printToast(getActivity(), "Sign up failed: no role was selected",Toast.LENGTH_SHORT);
                            return;
                        }

                        UserManager.signupUser(mAuth,password,email,username, role[0],getActivity(),signupAction);
                    }
                });

                studentRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            role[0] = Role.STUDENT;
                            Log.d(TAG, "Student role was selected");
                        }
                    }
                });

                tutorRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            role[0] = Role.TUTOR;
                            Log.d(TAG, "Tutor role was selected");
                        }
                    }
                });
            }
            return layout;
        }

        private void startMainActivity(){
            // Go to home activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<TabDetails> tabs = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        private void addFragment(TabDetails tab) {
            tabs.add(tab);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getTabName();
        }
    }
}
