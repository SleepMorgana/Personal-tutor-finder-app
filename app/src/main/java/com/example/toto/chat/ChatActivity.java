package com.example.toto.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toto.R;
import com.example.toto.queue.channelRcv.OnQueueMessageArrive;
import com.example.toto.queue.channelRcv.QueueService;
import com.example.toto.queue.channelTransmission.RabbitQueueHelper;
import com.example.toto.queue.messages.RxAbstractMessage;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.example.toto.utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class ChatActivity extends AppCompatActivity {
    public final static String chatTarget = "targetUser";
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        //Enable the Up button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar(); // Get a support ActionBar corresponding to this toolbar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        Intent userIntent = getIntent();
        final User target = userIntent.getParcelableExtra(chatTarget);


        final MessagesList messagesList = (MessagesList) findViewById(R.id.messagesList);
        final MessagesListAdapter<RxAbstractMessage> adapter =
                new MessagesListAdapter<>(UserManager.getUserInstance().getUser().getId(), null);
        messagesList.setAdapter(adapter);

        Util.startQueueService(this);

        RabbitQueueHelper.setChannelIsReady(true);
        QueueService.addQueueMessageHandler(new OnQueueMessageArrive() {
            @Override
            public void messageReady(RxAbstractMessage message) {
                //handle message depending on type
                //i.e update to s
                adapter.addToStart(message,true);
                Log.d(Util.TAG,"Message: "+message.getText());
            }
        });


        final EditText input = (EditText) findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = (input.getText()!=null)? input.getText().toString().trim() : "";
                RxAbstractMessage message = new RxAbstractMessage("message",
                        UserManager.getUserInstance().getUser().getId(),
                        target.getId(), msg);

                RabbitQueueHelper.sendMessage(message, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Message message1 = new Message();
                        message1.what = 1;
                        message1.obj = o;
                        mHandler.sendMessage(message1);

                        Log.d(Util.TAG,"Message: sent");
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Util.printToast(getApplicationContext(),
                                "There were issues sending the message, please try again.", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1)
                {
                    // Update view component
                    adapter.addToStart((RxAbstractMessage) msg.obj,true);
                    input.setText("");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Util.startQueueService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Util.stopQueueService(this);
    }

}
