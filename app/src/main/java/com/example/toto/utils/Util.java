package com.example.toto.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Util {
    public static final String TAG = "TUTOR-FINDER";

    public static void printToast(Context context, String msg, int duration){
        Toast.makeText(context, msg,
                duration).show();
    }

    public static ProgressDialog makeProgressDialog(String title, String msg, Context ctx){
        ProgressDialog mDialog = new ProgressDialog(ctx);
        //signin progress bar
        mDialog.setTitle(title);
        mDialog.setMessage(msg);
        mDialog.setIndeterminate(false);
        mDialog.setCancelable(true);

        return mDialog;
    }
}
