package com.example.toto.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.toto.interfaces.Storable;
import com.example.toto.sessions.Status;
import com.example.toto.sessions.Status;
import com.example.toto.subjects.Subject;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class User extends Observable implements Storable, Parcelable {
    //combination of FirebaseUser and user from the `users` collection in firestore
    private String username;
    private String email;
    private Role role;
    private String id;
    private Status status; // for tutors
    private Map<String,Subject> subjects = new HashMap<>();


    public User(DocumentSnapshot user){
        id = user.getId();
        email = (String) user.getData().get("Email");
        username = (String) user.getData().get("Username");
        role = Role.valueOf((String)user.getData().get("Role"));
        if (role.equals(Role.TUTOR))
            status = Status.valueOf((String)user.getData().get("Status"));
        subjects = (Map<String, Subject>) user.getData().get("Subjects");
        if (subjects == null)
            subjects = new HashMap<>();
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

    public void setSubjects(Map<String, Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Subject s){
        if (!subjects.containsKey(s.getId())){
            subjects.put(s.getId(),s);
            setChanged();
        }
    }

    public void removeSubject(Subject s){
        if (subjects.containsKey(s.getId())){
            subjects.remove(s.getId());
            setChanged();
        }
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

    public Map<String, Subject> getSubjects() {
        return subjects;
    }

    public Map<String, Object> marshal(){
        //the Id must be fetch from the instance, in firestore document ids aren't in the map
        Map<String, Object> user = new HashMap<>();
        user.put("Username",username);
        user.put("Email",email);
        user.put("Role",role.toString());
        if (role.equals(Role.TUTOR))
            user.put("Status", status.toString());
        user.put("Subjects",flatten(subjects));

        return user;
    }

    private Map<String,Object> flatten(Map<String,Subject> map){
        Map<String,Object> newMap = new HashMap<>();
        for(Map.Entry<String, Subject> entry : map.entrySet()) {
            newMap.put(entry.getKey(), ((Subject)entry.getValue()).marshal());
        }
        return newMap;
    }


    /**
     * describeContents method for a Parcelable class (in this project, such class(es) has(have) no
     * child classes)
     * @return 0. Parcelable class(es) in this project has(have) no child classes
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write an object to a parcel
     * @param dest The Parcel in which the object should be written
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.role.toString());
        dest.writeString(this.id);
        //dest.writeString(this.status.toString()); TODO instantiation to avoid null
    }

    /**
     * Regenerate the object from parcel
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Constructor that takes a parcel and construct a populated user object
     * @param in parcel
     */
    private User(Parcel in) {
        username = in.readString();
        email = in.readString();
        role = Role.valueOf(in.readString());
        this.id = in.readString();
        //this.status = Status.valueOf(in.readString()); TODO instantiation to avoid null
    }
}
