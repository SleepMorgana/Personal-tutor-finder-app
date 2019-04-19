package com.example.toto.users;

import com.example.toto.interfaces.Storable;
import com.example.toto.sessions.Status;
import com.example.toto.subjects.Subject;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class User extends Observable implements Storable {
    //combination of FirebaseUser and user from the `users` collection in firestore
    private String username;
    private String email;
    private Role role;
    private String id;
    private Status status; // for tutors


    public User(DocumentSnapshot user){
        id = user.getId();
        email = (String) user.getData().get("Email");
        username = (String) user.getData().get("Username");
        role = Role.valueOf((String)user.getData().get("Role"));
        if (role.equals(Role.TUTOR))
            status = Status.valueOf((String)user.getData().get("Status"));
    }

    public User(String username,String email, Role role,String id, Status status){
        this.username = username;
        this.email = email;
        this.id = id;
        this.role = role;
        this.status = status;
    }

    //Only used at signIn and signUp time
    public User(FirebaseUser user){
        id = user.getUid();
        email = user.getEmail();
        username = user.getDisplayName();
    }

    public void setEmail(String email) {
        this.email = email;
        setChanged();
    }

    public void setRole(Role role) {
        this.role = role;
        setChanged();
    }

    public void setUsername(String username) {
        this.username = username;
        setChanged();
    }

    public void setStatus(Status status) {
        this.status = status;
        setChanged();
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, Object> marshal(){
        //the Id must be fetch from the instance, in firestore document ids aren't in the map
        Map<String, Object> user = new HashMap<>();
        user.put("Username",username);
        user.put("Email",email);
        user.put("Role",role.toString());
        if (role.equals(Role.TUTOR))
            user.put("Status", status.toString());

        return user;
    }

}
