package com.example.canvasexample2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameRoom {
    private int playerNum;
    private ArrayList<String> uIDs;
    //add name array
    public GameRoom(int playerNum)
    {
        this.playerNum=playerNum;
        this.uIDs=new ArrayList<>();
    }

    public GameRoom() {
    }

    public int getPlayer()
    {
        return this.playerNum;
    }
    public ArrayList<String> getUIDs()
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
