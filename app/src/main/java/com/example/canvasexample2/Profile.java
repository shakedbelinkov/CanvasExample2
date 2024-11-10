package com.example.canvasexample2;

public class Profile {
    private String name;
    private String email;
    private int points;


    public Profile() {
    }

    public Profile(String name, String email, int points){
        this.name=name;
        this.email=email;
        this.points = points;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserData{" +"email='" + email + '\'' +", name='" + name +'}';
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
