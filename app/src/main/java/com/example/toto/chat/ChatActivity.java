package com.example.toto.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.toto.R;
import com.example.toto.queue.messages.RxAbstractMessage;
import com.example.toto.users.UserManager;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        MessagesList messagesList = (MessagesList) findViewById(R.id.messagesList);
        MessagesListAdapter<RxAbstractMessage> adapter =
                new MessagesListAdapter<>(UserManager.getUserInstance().getUser().getId(), null);
        messagesList.setAdapter(adapter);
    }
}
