package com.hfad.mathquiz.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.hfad.mathquiz.models.NewScore;

public class NewScoreView extends android.support.v7.widget.AppCompatTextView {

    private NewScore newScore;

    public NewScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(NewScore newScore) {
        this.newScore = newScore;
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if(newScore == null)
            return;
        this.setTextSize(newScore.getCurrentSize());
    }

}
