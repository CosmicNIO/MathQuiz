package com.hfad.mathquiz.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.hfad.mathquiz.models.Ball;

public class BallView extends View {

    private Ball ball;

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(ball == null)
            return;
        ball.draw(canvas);
    }
}
