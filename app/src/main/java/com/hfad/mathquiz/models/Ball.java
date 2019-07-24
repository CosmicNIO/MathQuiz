package com.hfad.mathquiz.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {

    private Paint ballColor;
    private float x,y;
    private boolean isShooting;
    private int ballViewHeight;
    private float[] leftAnsViewDimens;
    private float[] rightAnsViewDimens;
    private float leftAnsViewHeight;
    private float rightAnsViewHeight;
    private float velocity;
    private CollisionListener collisionListener;

    public Ball(CollisionListener collisionListener) {
        ballColor = new Paint();
        ballColor.setColor(Color.parseColor("#FFBC52"));
        this.collisionListener = collisionListener;
    }

    public void moveX(int x) {
        this.x = x;
    }

    public void decreaseY() {
        y -= velocity;
        if(y <= 0) {
            reset();
        }
    }

    public void shoot(float velocity) {
        this.velocity = velocity*-1/20;
        isShooting = true;
    }

    public void reset() {
        isShooting = false;
        y = ballViewHeight - 200;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, 40, ballColor);
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void detectCollision() {
        if((x >= leftAnsViewDimens[0] &&  x <= leftAnsViewDimens[1]) && y <= leftAnsViewHeight) {
            reset();
            collisionListener.onCollision("left");
        }
        if((x >= rightAnsViewDimens[0] &&  x <= rightAnsViewDimens[1]) && y <=
                rightAnsViewHeight) {
            reset();
            collisionListener.onCollision("right");
        }
    }

    public void setBallViewHeight(int ballViewHeight) {
        this.ballViewHeight = ballViewHeight;
    }

    public void setLeftAnsViewDimens(float[] leftAnsViewDimens) {
        this.leftAnsViewDimens = leftAnsViewDimens;
    }

    public void setRightAnsViewDimens(float[] rightAnsViewDimens) {
        this.rightAnsViewDimens = rightAnsViewDimens;
    }

    public void setLeftAnsViewHeight(float leftAnsViewHeight) {
        this.leftAnsViewHeight = leftAnsViewHeight;
    }

    public void setRightAnsViewHeight(float rightAnsViewHeight) {
        this.rightAnsViewHeight = rightAnsViewHeight;
    }
}
