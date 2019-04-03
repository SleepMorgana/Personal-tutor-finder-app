package com.example.toto.users;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class User extends Observable {
    //combination of FirebaseUser and user from the `users` collection in firestore
    private String username;
    private String email;
    private Role role;
    private String id;


    public User(DocumentSnapshot user){
        id = user.getId();
        email = (String) user.getData().get("Email");
        username = (String) user.getData().get("Username");
        role = Role.valueOf((String)user.getData().get("Role"));
    }

    public User(String username,String email, Role role,String id){
        this.username = username;
        this.email = email;
        this.id = id;
        this.role = role;
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

    public Map<String, Object> marshal(){
        //the Id must be fetch from the instance, in firestore document ids aren't in the map
        Map<String, Object> user = new HashMap<>();
        user.put("Username",username);
        user.put("Email",email);
        user.put("Role",role.toString());

        return user;
    }

}
