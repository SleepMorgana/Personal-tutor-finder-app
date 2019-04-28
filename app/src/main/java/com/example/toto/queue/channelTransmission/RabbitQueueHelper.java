package com.example.toto.queue.channelTransmission;

import android.support.annotation.NonNull;

import com.example.toto.queue.Address;
import com.example.toto.queue.messages.RxAbstractMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitQueueHelper{
    private static boolean channelIsReady = false;

    public static void setChannelIsReady(boolean channelIsReady) {
        RabbitQueueHelper.channelIsReady = channelIsReady;
    }

    //data is the message body
    private static boolean sendMessage(String queueId, String data) throws IOException, TimeoutException {
        if (channelIsReady) {
            String QUEUE_NAME = queueId;
            Channel channel = ChannelSingleton.getInstance();
            channel.exchangeDeclare(QUEUE_NAME, "direct");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, data.getBytes());
            return true;
        }
        return false;
    }

    public static void sendMessage(final RxAbstractMessage message,
                                   final OnSuccessListener successListener, final OnFailureListener failureListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccessful = false;
                try {
                    isSuccessful = sendMessage(message.getTarget(), message.jsonify().getAsString());
                } catch (IOException e) {
                    failureListener.onFailure(e);
                } catch (TimeoutException e) {
                    failureListener.onFailure(e);
                }finally {
                    if (isSuccessful)
                        successListener.onSuccess(null);
                    else
                        failureListener.onFailure(new UnsupportedOperationException("unsuccessful delivery"));
                }
            }
        }).start();

    }

}
