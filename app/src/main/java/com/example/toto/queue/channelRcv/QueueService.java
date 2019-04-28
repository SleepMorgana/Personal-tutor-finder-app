package com.example.toto.queue.channelRcv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class QueueService extends Service {
    private static List<OnQueueMessageArrive> observers = new ArrayList<>();
    private static Runnable handleMessage = new QueueServiceHandler();

    public QueueService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread queueThread = new Thread(handleMessage);
        queueThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void addQueueMessageHandler(OnQueueMessageArrive listener){
        observers.add(listener);
        ((QueueServiceHandler)handleMessage).addObserver(listener);
    }
}
