package com.example.toto.database.users;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class User extends Observable {
    //combination of FirebaseUser and user from the `users` collection in firestore
    private String username;
    private String email;
    private Role role;
    private String id;


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
        Map<String, Object> user = new HashMap<>();
        user.put("Username",username);
        user.put("Email",email);
        user.put("Role",role.toString());

        return user;
    }

}
