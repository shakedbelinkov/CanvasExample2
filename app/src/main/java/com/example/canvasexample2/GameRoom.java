package com.example.canvasexample2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameRoom {
    private int playerNum;
    private int roundNum;
    private int roundTime;
    private ArrayList<String> uIDs;
    private ArrayList<String> names;
    //add name array
    public GameRoom(int playerNum,int roundNum,int roundTime)
    {
        this.playerNum=playerNum;
        this.roundNum=roundNum;
        this.roundTime=roundTime;
        this.uIDs=new ArrayList<>();
        this.names=new ArrayList<>();
    }

    public GameRoom() {
    }
    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }
    public int getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(int roundTime) {
        this.roundTime = roundTime;
    }
    public int getPlayer()
    {
        return this.playerNum;
    }
    public ArrayList<String> getUIDs()
    {
        return this.uIDs;
    }
    public ArrayList<String> getNames()
    {
        return this.uIDs;
    }

    public void setPlayer(int playerNum)
    {
        this.playerNum=playerNum;
    }
    public void setUIDs(ArrayList<String> uIDs)
    {
        this.uIDs=uIDs;
    }
    public void AddUser(String uId)
    {
        if (this.uIDs.size()<playerNum)
            this.uIDs.add(uId);
    }


}
