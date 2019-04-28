package com.example.toto.queue.channelRcv;

import android.util.Log;

import com.example.toto.queue.messages.RxAbstractMessage;
import com.example.toto.utils.Util;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class QueueServiceHandler extends AbstractQueueServiceHandler{
    private List<OnQueueMessageArrive> messageListener = new ArrayList<>();

    public QueueServiceHandler(List<OnQueueMessageArrive> consumers){
        messageListener = consumers;
    }

    public QueueServiceHandler(){
    }

    @Override
    protected Consumer getConsumer() {
        //just create a Consumer, the connection is already started in the abstract class
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                Log.d(Util.TAG,"message ok");
                for (OnQueueMessageArrive l : messageListener)
                    l.messageReady(new RxAbstractMessage(message));
            }
        };
    }

    public void addObserver(OnQueueMessageArrive l){
        messageListener.add(l);
    }
}
