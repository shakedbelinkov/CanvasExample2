package com.example.Shcrible;

public class Draw {
    private float initialX;
    private float initialY;
    private float endX;
    private float endY;
    private int type;
    private int color;
    private int brushSize;
    public Draw(float initialX,float initialY,float endX,float endY,int type,int color)
    {
        this.initialX=initialX;
        this.initialY=initialY;
        this.endX=endX;
        this.endY=endY;
        this.type=type;
        this.color=color;
    }

    public float getInitialX() {
        return initialX;
    }

    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
