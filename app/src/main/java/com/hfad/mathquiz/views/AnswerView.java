package com.hfad.mathquiz.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import com.hfad.mathquiz.models.Answer;

public class AnswerView extends android.support.v7.widget.AppCompatTextView {

    private Answer answer;

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(Answer answer) {
        this.answer = answer;
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if(answer == null)
            return;
        this.setX(answer.getxPos());
    }
}
