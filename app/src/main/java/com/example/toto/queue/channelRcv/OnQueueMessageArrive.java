package com.example.toto.queue.channelRcv;


import com.example.toto.queue.messages.RxAbstractMessage;

public interface OnQueueMessageArrive {
    void messageReady(RxAbstractMessage message);
}
