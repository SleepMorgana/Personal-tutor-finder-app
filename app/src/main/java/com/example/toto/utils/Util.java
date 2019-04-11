package com.example.toto.utils;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void printToast(Context context, String msg, int duration){
        Toast.makeText(context, msg,
                duration).show();
    }
}
