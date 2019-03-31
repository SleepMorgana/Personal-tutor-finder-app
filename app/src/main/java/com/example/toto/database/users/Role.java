package com.example.toto.database.users;

enum Role {
    STUDENT("Student"),TUTOR("Tutor"),ADMIN("Admin");
    private String text;
    Role(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
