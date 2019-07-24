package com.hfad.mathquiz.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class MathRainView extends View {

    private static final Random random = new Random();
    private Paint brush;
    private Point[] mathSymbols;
    private String[] operators;
    private static final int NUMBER_OF_SYMBOLS = 10;
    private static final int MAX_SYMBOL_FALL_RATE = 10;

    public MathRainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        brush = new Paint();
        brush.setColor(Color.parseColor("#FFBC52"));
        brush.setAlpha(100);
        brush.setStrokeWidth(3);
        brush.setTextSize(200);
    }

    private void setupMathSymbols() {
        mathSymbols = new Point[NUMBER_OF_SYMBOLS];
        operators = new String[NUMBER_OF_SYMBOLS];
        for(int i = 0; i < NUMBER_OF_SYMBOLS; ++i) {
            int x = 10 + random.nextInt(getWidth() - 10);
            int y = random.nextInt(getHeight());
            mathSymbols[i] = new Point(x, y);
            int randomOperator = random.nextInt(3 - 1 + 1) + 1;
            switch(randomOperator) {
                case 1:
                    operators[i] = "X";
                    break;
                case 2:
                    operators[i] = "+";
                    break;
                case 3:
                    operators[i] = "-";
                    break;
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        setupMathSymbols();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawMathSymbols(canvas);
        moveMathSymbols();
    }

    private void drawMathSymbols(Canvas canvas) {
        for(int i = 0; i < NUMBER_OF_SYMBOLS; ++i) {
            canvas.drawText(operators[i], mathSymbols[i].x, mathSymbols[i].y, brush);
        }
    }

    private void moveMathSymbols() {
        for(Point mathSymbol : mathSymbols) {
            mathSymbol.y += random.nextInt(MAX_SYMBOL_FALL_RATE);
            if(mathSymbol.y > getHeight()) {
                mathSymbol.y = 0;
            }
        }
    }
}
