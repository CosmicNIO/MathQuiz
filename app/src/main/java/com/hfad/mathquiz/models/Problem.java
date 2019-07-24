package com.hfad.mathquiz.models;

public class Problem {
    private int leftNum;
    private int rightNum;
    private Operation operation;
    private int answer;
    private int wrongAnswer;

    Problem(int leftNum, int rightNum, Operation operation, int answer, int wrongAnswer) {
        this.leftNum = leftNum;
        this.rightNum = rightNum;
        this.operation = operation;
        this.answer = answer;
        this.wrongAnswer = wrongAnswer;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public int getRightNum() {
        return rightNum;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getAnswer() {
        return answer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }
}
