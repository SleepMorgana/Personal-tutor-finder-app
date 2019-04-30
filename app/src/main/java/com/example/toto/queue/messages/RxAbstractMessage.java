package com.example.toto.queue.messages;

import com.example.toto.sessions.Status;
import com.example.toto.users.Role;
import com.example.toto.users.User;
import com.example.toto.users.UserManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class RxAbstractMessage implements IMessage {
    protected String type = null;
    protected String sender = null;
    protected String target = null;
    protected String id = null;
    protected String body = null;
    protected String timestamp = null;

    public RxAbstractMessage(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        id = (jsonObject.has("Id"))?jsonObject.get("Id").getAsString():null;
        type = (jsonObject.has("Type"))?jsonObject.get("Type").getAsString():null;
        sender = (jsonObject.has("Sender"))?jsonObject.get("Sender").getAsString():null;
        target = (jsonObject.has("Target"))?jsonObject.get("Target").getAsString():null;
        body = (jsonObject.has("Body"))?jsonObject.get("Body").getAsString():null;
        timestamp = (jsonObject.has("TS"))?jsonObject.get("TS").getAsString():null;
    }

    public RxAbstractMessage(RxAbstractMessage msg) {
        type = msg.getType();
        id = msg.getId();
        sender = msg.getSender();
        target = msg.getTarget();
        body = msg.getText();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(msg.getCreatedAt());
        timestamp = ""+calendar.getTimeInMillis();
    }

    public RxAbstractMessage(String type, String sender, String target, String body) {
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();
        this.type = type;
        this.sender = sender;
        this.target = target;
        this.body = body;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
        if (id!=null)
            object.add("Id",new JsonPrimitive(id));
        if (body!=null)
            object.add("Body",new JsonPrimitive(body));
        if (timestamp!=null)
            object.add("TS",new JsonPrimitive(timestamp));
        return object;
    }
//TODO finish implementation
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return body;
    }

    @Override
    public IUser getUser() {
        //check if sender is current user
        if (UserManager.getUserInstance().getUser().getId().equals(sender))
            return UserManager.getUserInstance().getUser();
        //TODO retrieve user
        return new User("foo","email", Role.TUTOR,sender, Status.ACCEPTED);
    }

    @Override
    public Date getCreatedAt() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        return calendar.getTime();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RxAbstractMessage){
            if (((RxAbstractMessage)obj).getId().equals(this.id))
                return true;
        }
        return false;
    }
}
