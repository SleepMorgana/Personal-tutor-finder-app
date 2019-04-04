package com.example.toto;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

//This is going to be used as the home activity of the application
public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
