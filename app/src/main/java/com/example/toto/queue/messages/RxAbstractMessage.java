package com.example.toto.queue.messages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class RxAbstractMessage implements IMessage {
    protected String type = null;
    protected String sender = null;
    protected String target = null;

    public RxAbstractMessage(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        type = (jsonObject.get("Type")!=null)?jsonObject.get("Type").getAsString():null;
        sender = (jsonObject.get("Sender")!=null)?jsonObject.get("Sender").getAsString():null;
        target = (jsonObject.get("Target")!=null)?jsonObject.get("Target").getAsString():null;
    }

    public RxAbstractMessage(RxAbstractMessage msg) {
        type = msg.getType();
    }

    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public String getSender() {
        return sender;
    }

    public JsonObject jsonify(){
        JsonObject object = new JsonObject();
        if (type!=null)
            object.add("Type",new JsonPrimitive(type));
        if (target!=null)
            object.add("Target",new JsonPrimitive(target));
        if (sender!=null)
            object.add("Sender",new JsonPrimitive(sender));
        return object;
    }
//TODO finish implementation
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return null;
    }
}
