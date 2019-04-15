package com.example.toto;

import com.example.toto.interfaces.DatabaseHelper;
import com.example.toto.users.Role;
import com.example.toto.users.User;
import com.example.toto.users.UserController;
import com.example.toto.users.UserDatabaseHelper;
import com.example.toto.users.UserManager;
import com.google.android.gms.internal.firebase_auth.zzao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(MockitoJUnitRunner.class)
public class UserPackageTest implements Executor {
    //private Context context = ApplicationProvider.getApplicationContext();
    private User mNewUser;
    @Mock
    FirebaseAuth mMockFirebaseAuth;
    @Mock
    FirebaseFirestore mMockFirebaseFirestore;
    @Mock
    DatabaseHelper<User> databaseHelper;

    @Before
    public void initMocks(){
        FirebaseApp.initializeApp(mock(Activity.class));
        mNewUser = new User("student-1","example@gmail.com", Role.STUDENT,"id-1");
        createFirebaseFirestoreMock(mNewUser);
        //databaseHelper = mock(UserDatabaseHelper.class);
        //createFirebaseAuthMock(mNewUser);
    }

    @Test
    public void test_Signup() {
        //create user
        //User user = new User("student-1","example@gmail.com", Role.STUDENT,"id-1");

        //SplashActivity activity = Robolectric.setupActivity(SplashActivity.class);

        //FirebaseApp.initializeApp(context, new FirebaseOptions.Builder().)
        //Activity activity = new TestActivity();
        //database instance
        //FirebaseAuth auth = FirebaseAuth.getInstance(FirebaseApp.getInstance());
        ///UserController.signupUser(null,"foobar1",mNewUser.getEmail(),mNewUser.getUsername(),mNewUser.getRole(),activity,null);
        Future<User> userFuture = UserManager.signupUser(mMockFirebaseAuth,"foobar1",mNewUser.getEmail(),mNewUser.getUsername(),mNewUser.getRole(), this);
        //FirebaseApp.clearInstancesForTest();
        try {
            assertEquals(userFuture.get().getEmail(),mNewUser.getEmail());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    private class TestActivity extends Activity{
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }

    public void createFirebaseAuthMock(final User user){
        when(FirebaseAuth.getInstance()).thenReturn(mMockFirebaseAuth);
        when(mMockFirebaseAuth.getCurrentUser()).thenReturn(new FirebaseUser() {
            @NonNull
            @Override
            public String getUid() {
                return user.getId();
            }

            @NonNull
            @Override
            public String getProviderId() {
                return null;
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }

            @android.support.annotation.Nullable
            @Override
            public List<String> getProviders() {
                return null;
            }

            @NonNull
            @Override
            public List<? extends UserInfo> getProviderData() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
                return null;
            }

            @Override
            public FirebaseUser zzn() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseApp zzo() {
                return null;
            }

            @android.support.annotation.Nullable
            @Override
            public String getDisplayName() {
                return user.getUsername();
            }

            @android.support.annotation.Nullable
            @Override
            public Uri getPhotoUrl() {
                return null;
            }

            @android.support.annotation.Nullable
            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @android.support.annotation.Nullable
            @Override
            public String getPhoneNumber() {
                return null;
            }

            @NonNull
            @Override
            public zzao zzp() {
                return null;
            }

            @Override
            public void zza(@NonNull zzao zzao) {

            }

            @NonNull
            @Override
            public String zzq() {
                return null;
            }

            @NonNull
            @Override
            public String zzr() {
                return null;
            }

            @android.support.annotation.Nullable
            @Override
            public FirebaseUserMetadata getMetadata() {
                return null;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }

            @Override
            public boolean isEmailVerified() {
                return false;
            }
        });
        when(mMockFirebaseAuth.createUserWithEmailAndPassword("foobar1",user.getEmail()))
                .thenReturn(new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @android.support.annotation.Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        });
    }

    public void createFirebaseFirestoreMock(final User user){
        when(FirebaseFirestore.getInstance()).thenReturn(mMockFirebaseFirestore);

        //when(databaseHelper.upsert(user))
    }
}
