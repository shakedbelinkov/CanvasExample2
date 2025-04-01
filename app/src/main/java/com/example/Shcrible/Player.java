package com.example.Shcrible;

public class Player {
    private String name;
    private int points;
    private int place;
    public Player(String name,int points)
    {
        this.name=name;
        this.points=points;
    }

    public Player(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
