package com.example.toto.queue.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MessageQueueStore extends Observable {
    private static List<RxAbstractMessage> messages = new ArrayList<>();
    private static MessageQueueStore instance = null;

    public MessageQueueStore(){
    }

    public List<RxAbstractMessage> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
            return messages;
        }
        return messages;
    }

    public void add(RxAbstractMessage message) {
        if (getMessages().contains(message))
            return;
        getMessages().add(message);
        notifyObservers(message);
        setChanged();
    }

    public static MessageQueueStore getInstance() {
        if (instance == null)
            return new MessageQueueStore();
        return instance;
    }
}
