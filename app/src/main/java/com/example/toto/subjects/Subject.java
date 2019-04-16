package com.example.toto.subjects;

import com.example.toto.interfaces.Storable;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
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
        this.name = name;
    }

    public Subject(String id, String name){
        this.id = id;
        this.name = name;
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
        //the Id must be fetch from the instance,
        //in firestore document ids aren't in the map
        Map<String, Object> subject = new HashMap<>();
        subject.put("Name",name);
        return subject;
    }

    //This just asmaller representation of the subject class, {id:"",name:""}
    public class SubjectTuple extends Subject{
        public SubjectTuple(String id, String name){
            super(id,name);
        }

        @Override
        public Map<String, Object> marshal() {
            Map<String, Object> subject = new HashMap<>();
            subject.put("Name",name);
            return subject;
        }
    }
}
