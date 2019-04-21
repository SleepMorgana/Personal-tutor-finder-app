package com.example.toto.sessions;

import com.example.toto.interfaces.Storable;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Session extends Observable implements Storable {
    private String subject;//id
    private String sender;//id
    private String target;//id
    private Status status;
    private String id;
    //TODO add date list


    public Session(DocumentSnapshot session){
        id = session.getId();
        subject = (String) session.getData().get("Subject");
        sender = (String) session.getData().get("Sender");
        target = (String) session.getData().get("Target");
        status = Status.valueOf((String)session.getData().get("Status"));
    }

    public Session(String subject,String sender,String target, Status status, String id){
        this.subject = subject;
        this.sender = sender;
        this.id = id;
        this.target = target;
        this.status = status;
    }

    public void updateStatus(Status status){
        this.status = status;
        setChanged();
    }

    public Status getStatus() {
        return status;
    }

    public String getSender() {
        return sender;
    }

    public String getTarget() {
        return target;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> marshal() {
        //the Id must be fetch from the instance, in firestore document ids aren't in the map
        Map<String, Object> session = new HashMap<>();
        session.put("Subject",subject);
        session.put("Sender",sender);
        session.put("Target",target);
        session.put("Status",status.toString());

        return session;
    }
}
