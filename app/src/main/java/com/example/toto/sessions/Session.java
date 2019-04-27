package com.example.toto.sessions;

import com.example.toto.interfaces.Storable;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class Session extends Observable implements Storable {
    private String subject;//id
    private String sender;//id
    private String target;//id
    private Status status;
    private String id;
    private List<Object> dates = new ArrayList<>();// list of timestamps


    public Session(DocumentSnapshot session){
        id = session.getId();
        subject = (String) session.getData().get("Subject");
        sender = (String) session.getData().get("Sender");
        target = (String) session.getData().get("Target");
        status = Status.valueOf((String)session.getData().get("Status"));
        dates = session.contains("Dates")? ((List<Object>) session.getData().get("Dates")): new ArrayList<>();
    }

    public Session(String subject,String sender,String target, Status status, String id){
        this.subject = subject;
        this.sender = sender;
        this.id = id;
        this.target = target;
        this.status = status;
    }

    public Session(String subject,String sender,String target, Status status){
        this.subject = subject;
        this.sender = sender;
        this.target = target;
        this.status = status;
    }

    public void updateStatus(Status status){
        this.status = status;
        setChanged();
    }

    public void addDate(Date date){
        //TODO
    }

    public void addDate(String timestamp){
        dates.add((Object)timestamp);
        setChanged();
    }

    public void setDates(List<Object> dates) {
        this.dates = dates;
        setChanged();
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Object> getDates() {
        return dates;
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
        session.put("Dates", (dates));

        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Session)
            return ((Session) o).id.equals(this.id);
        return false;
    }
}
