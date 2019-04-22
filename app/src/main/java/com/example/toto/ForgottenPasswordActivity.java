package com.example.toto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toto.users.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPasswordActivity extends AppCompatActivity {

    private static FirebaseAuth mAuth; // firebase authenticator
    Button reset_psswd_button;
    EditText input_email_edit_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        reset_psswd_button = findViewById(R.id.reset_psswd_id);
        mAuth = FirebaseAuth.getInstance(); //Initialize Firebase Auth
        final Context context = this;
        reset_psswd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_email_edit_field = (EditText) findViewById(R.id.input_email_id);

                UserManager.resetPassword(mAuth, input_email_edit_field, context);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
