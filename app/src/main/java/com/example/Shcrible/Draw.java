package com.example.Shcrible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Draw implements Serializable {
    private float initialX;
    private float initialY;
    private float endX;
    private float endY;
    private int type;
    private int color;
    private int brushSize;
    public Draw(float initialX,float initialY,float endX,float endY,int type,int color,int brushSize)
    {
        this.initialX=initialX;
        this.initialY=initialY;
        this.endX=endX;
        this.endY=endY;
        this.type=type;
        this.color=color;
        this.brushSize=brushSize;
    }

    public Draw() {
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
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


    public HashMap<String,Object> drawToHashmap()
            //for each draw in the arraylist create a dictionary - hashmap
    {
        HashMap<String,Object> map = new HashMap<>();
        map.put("initialX",initialX);
        map.put("initialY",initialY);
        map.put("endX",endX);
        map.put("endY",endY);
        map.put("type",type);
        map.put("color",color);
        map.put("brushSize",brushSize);
        return map;
    }
    public void hashmapToDraw(Map<String,Object> map)
    {
        // map to draw attributes
        Object o = map.get("initialX");
        this.initialX = (float)((double)map.get("initialX"));
        this.initialY = (float)((double)map.get("initialY"));
        this.endX = (float)((double)map.get("endX"));
        this.endY = (float)((double)map.get("endY"));
        this.type = (int)((long)map.get("type"));
        this.color=(int)((long)map.get("color"));
        this.brushSize=(int)((long)map.get("brushSize"));

    }
    @Override
    public String toString() {
        return "Draw{" +
                "initialX=" + initialX +
                ", initialY=" + initialY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", type=" + type +
                ", color=" + color +
                ", brushSize=" + brushSize +
                '}';
    }
}
