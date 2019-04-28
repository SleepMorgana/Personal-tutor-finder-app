package com.example.toto.queue.channelRcv;

import com.example.toto.queue.Address;
import com.example.toto.queue.channelTransmission.ChannelSingleton;
import com.example.toto.users.UserManager;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public abstract class AbstractQueueServiceHandler implements Runnable {
    protected final static String QUEUE_NAME = UserManager.getUserInstance().getUser().getId();
    protected Channel channel=null;

    protected abstract Consumer getConsumer();

    @Override
    public void run() {
        try {
            channel = ChannelSingleton.getInstance();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicConsume(QUEUE_NAME, true, getConsumer());
        }catch (IOException e){
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
