package com.hfad.mathquiz.models;

public class Answer {

    private boolean direction;
    private int screenWidth;
    private int xPos;

    public Answer(boolean direction, int screenWidth) {
        this.direction = direction;
        this.screenWidth = screenWidth;
        if(direction)
            xPos = 0;
        else
            xPos = screenWidth;
    }

    public void animate() {
        if(direction) {
            xPos+= 10;
            if(xPos >= screenWidth)
                direction = false;
        } else {
            xPos-= 10;
            if(xPos <= 0)
                direction = true;
        }
    }

    public int getxPos() {
        return xPos;
    }

}
