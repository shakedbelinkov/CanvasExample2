package com.example.Shcrible;

import java.util.ArrayList;

public class GameRoom {
    private int playerNum;//max players on this game room
    private int roundNum;//number of rounds
    private int roundTime;//time for a round



    private int counterOfPlayers=0;//number of player now
    private ArrayList<String> uIDs;//list of UID
    private ArrayList<String> names;//list of names
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
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setCounterOfPlayers(int counterOfPlayers) {
        this.counterOfPlayers = counterOfPlayers;
    }



    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
    public int getRoundNum() {
        return roundNum;
    }
    public int getCounterOfPlayers()
    {
        return this.counterOfPlayers;
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
    public int getPlayerNum()
    {
        return this.playerNum;
    }
    public ArrayList<String> getUIDs()
    {
        return this.uIDs;
    }
    public ArrayList<String> getNames()
    {
        return this.names;
    }


    public void setUIDs(ArrayList<String> uIDs)
    {
        this.uIDs=uIDs;
    }
    public boolean AddUser(String uId,String name)
    {
        //add the user to the lists
        if (this.counterOfPlayers<playerNum) {
            this.uIDs.add(uId);
            this.names.add(name);
            this.counterOfPlayers++;
            return true;
        }
        return false;
    }
    public boolean deleteUser(String uId)
    {
        //add the user to the lists
        int index=999;
        for (int i=0;i<counterOfPlayers;i++)
            if (uIDs.get(i)==uId)
                index=i;
        if (index==999)
            return false;
        this.uIDs.remove(index);
        this.names.remove(index);
        this.counterOfPlayers--;
        return true;
    }


}
