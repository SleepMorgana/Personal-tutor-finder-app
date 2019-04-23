package com.example.toto.users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphabetik.Alphabetik;
import com.example.toto.subjects.Subject;
import com.example.toto.subjects.SubjectManager;
import com.example.toto.utils.CheckboxArrayAdapter;
import com.example.toto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserProfileEditActivity extends AppCompatActivity {

    private User user;
    private ImageView profile_picture;
    private final int RESULT_LOAD_IMAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        ImageView upload_profile_picture = findViewById(R.id.upload_pic_button_id);


        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        //Current user
        user = UserManager.getUserInstance().getUser();

        //User action: uploading a new profile picture
        upload_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_picture = findViewById(R.id.profile_picture_edit_id);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

        //Render the user's identity
        updateUserIdentity(user);

        SubjectManager.listSubjects(new OnSuccessListener<QuerySnapshot>() {
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
                final CheckboxArrayAdapter adapter = new CheckboxArrayAdapter(UserProfileEditActivity.this, all_app_subjects, mapping_user_subjects);
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

                //User wants to save his/her changes
                FloatingActionButton fab_save = (FloatingActionButton) findViewById (R.id.fab_save_id);
                fab_save.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View view) {
                        List<String> checked_subjects = new ArrayList<>();
                        Map<String, Boolean> updatedUserSubjectMap = adapter.getSubject_map();
                        for (Map.Entry<String, Boolean> entry : updatedUserSubjectMap.entrySet()) {
                            if (entry.getValue()) {
                                checked_subjects.add(entry.getKey());
                            }
                        }

                        Log.d("CECILE", checked_subjects.toString());
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CECILE", "FATAL");
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profile_picture.setImageBitmap(selectedImage);
                //Rescale image
                profile_picture.getLayoutParams().height = (int) getResources().getDimension(R.dimen.profile_pic_width);
                profile_picture.getLayoutParams().height = (int) getResources().getDimension(R.dimen.profile_pic_height);

                //Update profile picture attribute in the user object
                user.setProfile_picture(selectedImage);

                //Save the new current user's profile picture to Firestore
                uploadImage(imageUri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UserProfileEditActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(UserProfileEditActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /**
     * Uploading a file (in this project a profile picture previously picked from the phone's Photos or Gallery app)
     * Reference: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
     * @param filePath LOcal Uri of the profile picture
     */
    private void uploadImage(Uri filePath) {

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/profile_picture_"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileEditActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileEditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
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
            ImageView profile_pic_view = (ImageView) findViewById(R.id.profile_picture_edit_id);
            profile_pic_view.setImageBitmap(populated_user.getProfile_picture());
        }

        //Update navigation menu with the logged-in user's info
        //Username
        TextView text_view = findViewById(R.id.username_profile_id);
        text_view.setText(populated_user.getUsername());
        //Email
        text_view = findViewById(R.id.email_profile_id);
        text_view.setText(populated_user.getEmail());

        // Title of the list depends on the role of the user
        TextView subject_list_title = findViewById(R.id.subject_list_name_edit);
        switch (populated_user.getRole()) {
            case STUDENT:
                subject_list_title.setText("What are your learning needs?");
                break;
            case TUTOR:
                subject_list_title.setText("What subjects do you wish to tutor?");
                break;
        }
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
}
