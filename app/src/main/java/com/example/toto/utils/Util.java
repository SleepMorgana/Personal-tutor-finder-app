package com.example.toto.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

public class Util {
    public static final String TAG = "TUTOR-FINDER";
    public static final int NB_UPCOMING_SESSION = 5;

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

    public static Dialog makeDialog(String title, String msg, String positiveLabal, String negativeLabel, Context ctx,
                                    DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
        AlertDialog mDialog = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positiveLabal, positive)
                .setNegativeButton(negativeLabel, negative)
                .create();

        return mDialog;
    }

    public static Dialog makeInputDialog(String title, String msg, String positiveLabal, String negativeLabel, Context ctx,
                                    EditText text, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
        AlertDialog mDialog = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(msg)
                .setView(text)
                .setPositiveButton(positiveLabal, positive)
                .setNegativeButton(negativeLabel, negative)
                .create();

        return mDialog;
    }
}
