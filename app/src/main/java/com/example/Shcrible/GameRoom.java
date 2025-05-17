package com.example.Shcrible;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class GameRoom {
    private int playerNum;//max players on this game room
    private int roundNum;//number of rounds
    private int roundTime;//time for a round
    private boolean isStart=false;
    private String word="";
    private boolean isRoundStart=false;



    private int counterOfPlayers=0;//number of player now
    private ArrayList<String> uIDs;//list of UID
    private ArrayList<String> names;//list of names
    private ArrayList<Integer> points;
    //add name array
    public GameRoom(int playerNum,int roundNum,int roundTime)
    {
        this.playerNum=playerNum;
        this.roundNum=roundNum;
        this.roundTime=roundTime;
        this.uIDs=new ArrayList<>();
        this.names=new ArrayList<>();
        this.points=new ArrayList<>();
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
            this.points.add(0);
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
            if (uIDs.get(i).equals(uId))
                index=i;
        if (index==999)
            return false;
        this.uIDs.remove(index);
        this.names.remove(index);
        this.points.remove(index);
        this.counterOfPlayers--;
        return true;
    }

    public boolean getIsStart() {
        return isStart;
    }

    public void setIsStart(boolean start) {
        //Log.d("isStart", "setStart: "+start);
        isStart = start;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Integer> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Integer> points) {
        this.points = points;
    }
    public void updatePoint(int points,String name)
            //add to the points of "name" - "points"
    {
        int index=0;
        while (!name.equals(this.names.get(index)))
            index++;
        this.points.set(index,points);
    }
    public void orderListByPoints()
            //change the order of points and names by their points
    {
        for (int i=0;i<this.points.size()-1;i++)
        {
            if (this.points.get(i+1)>this.points.get(i))
            {
                int p=this.points.get(i);
                this.points.set(i,this.points.get(i+1));
                this.points.set(i+1,p);
                String name=this.names.get(i);
                this.names.set(i,this.names.get(i+1));
                this.names.set(i+1,name);
            }
        }
    }


    public boolean getIsRoundStart() {
        return isRoundStart;
    }

    public void setRoundStart(boolean roundStart) {
        isRoundStart = roundStart;
    }
}
