package com.example.Shcrible;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String name;
    private String message;
    private Map<String,Object> date = new HashMap<>();

    //private FieldValue timestamp;
    private boolean isRight=false;
    public Message(String name,String message)
    {
        this.name=name;
        this.message=message;
        this.date.put("timestamp", FieldValue.serverTimestamp());
        //this.timestamp = FieldValue.serverTimestamp();

    }
    public Message()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String massage) {
        this.message = massage;
    }
    public String toString()
    {
        return name+": "+message;
    }

   /* public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue t) {
        this.timestamp = t;
    }*/

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
    public Map<String,Object> getDate() {
        return date;
    }

    public void setDate(Map<String,Object> d) {
        this.date = d;
    }
}
