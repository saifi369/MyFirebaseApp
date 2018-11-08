package com.saifi369.myfirebaseapp.model;

public class User {

    private String uid;
    private String name;
    private int age;
    private String city;
    private String profession;

    public User() {
    }

    public User(String uid, String name, int age, String city, String profession) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.city = city;
        this.profession = profession;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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
        if(obj instanceof User){
            User user= (User) obj;
            return this.uid.equals(user.getUid());
        }
        else
            return  false;
    }
}
