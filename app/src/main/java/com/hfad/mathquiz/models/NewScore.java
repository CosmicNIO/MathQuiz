package com.hfad.mathquiz.models;

public class NewScore {

    private int minSize, maxSize, currentSize;
    private boolean isIncreasing;

    public NewScore() {
        minSize = 24;
        maxSize = 36;
        currentSize = minSize;
        isIncreasing = true;
    }

    public void animate() {
        if(isIncreasing) {
            currentSize++;
            if(currentSize >= maxSize)
                isIncreasing = false;
        } else {
            currentSize--;
            if(currentSize <= minSize)
                isIncreasing = true;
        }
    }

    public int getCurrentSize() {
        return currentSize;
    }

}
