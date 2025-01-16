package com.example.Shcrible;

public class Massage {
    private String name;
    private String massage;
    public Massage(String name,String massage)
    {
        this.name=name;
        this.massage=massage;
    }
    public Massage()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
    public String toString()
    {
        return name+": "+massage;
    }
}
