package com.example.canvasexample2;

public class DBGameRoom {
    private GameRoom gameRoom;
    public DBGameRoom(GameRoom gameRoom)
    {
        this.gameRoom=gameRoom;
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }
}
