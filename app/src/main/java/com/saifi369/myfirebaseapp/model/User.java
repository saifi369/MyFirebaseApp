package com.saifi369.myfirebaseapp.model;

public class User {

    private String uid;
    private String name;
    private int age;

    public User() {
    }

    public User(String uid, String name, int age) {
        this.uid = uid;
        this.name = name;
        this.age = age;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user= (User) obj;
            return uid.equals(user.getUid());
        } else return false;
    }

}
