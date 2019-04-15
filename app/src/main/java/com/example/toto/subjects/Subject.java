package com.example.toto.subjects;

import com.example.toto.interfaces.Storable;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

//In the case of subject the name
public class Subject implements Storable {
    private String id;
    private String name;

    public Subject(DocumentSnapshot subject){
        id = subject.getId();
        name = (String) subject.getData().get("Name");
    }

    public Subject(String name){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> marshal() {
        return null;
    }
}
