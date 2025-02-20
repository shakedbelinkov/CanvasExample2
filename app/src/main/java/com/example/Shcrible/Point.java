package com.example.Shcrible;

public class Point {
    private String name;
    private int points;
    public Point(String name,int points)
    {
        this.name=name;
        this.points=points;
    }
    public Point(){}

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
}
