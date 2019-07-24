package com.hfad.mathquiz.models;

import java.util.ArrayList;
import java.util.Random;

public class QuestionManager {

    private int numOfQuestions;
    private String difficulty;
    private ArrayList<Problem> problemsArray = new ArrayList<>();
    private int questionNumber;
    private String currentAnswer;
    private String progressText;
    private String answerLeftText;
    private String answerRightText;
    private String problemText;
    private boolean isGameOver;
    private int correctAnswersTally;

    public void initialize(int numOfQuestions, String difficulty) {
        this.numOfQuestions = numOfQuestions;
        this.difficulty = difficulty;
        for (int i = 0; i < numOfQuestions; ++i) {
            problemsArray.add(generateProblem());
        }
    }

    private Problem generateProblem() {
        int maxNum = 0;
        switch(difficulty) {
            case "Easy":
                maxNum = 20;
                break;
            case "Normal":
                maxNum = 40;
                break;
            case "Hard":
                maxNum = 60;
        }
        int leftNum = randNumInc(1, maxNum);
        int rightNum = randNumInc(1, maxNum);
        Operation[] operations = {Operation.MULTIPLY, Operation.ADD, Operation.SUBTRACT};
        Operation randomOp = operations[randNumInc(0, 2)];
        int answer = 0;
        switch (randomOp) {
            case MULTIPLY:
                answer = leftNum * rightNum;
                break;
            case ADD:
                answer = leftNum + rightNum;
                break;
            case SUBTRACT:
                answer = leftNum - rightNum;
                break;
        }
        int wrongAnswer;
        do {
            Operation randomOpTwo = operations[randNumInc(1, 2)];
            if(randomOpTwo == Operation.ADD)
                wrongAnswer = answer + randNumInc(1, 20);
            else
                wrongAnswer = answer - randNumInc(1, 20);
        } while (wrongAnswer == answer);
        return new Problem(leftNum, rightNum, randomOp, answer, wrongAnswer);
    }

    public boolean checkAnswer(String providedAnswer) {
        if(providedAnswer.equals(currentAnswer))
            correctAnswersTally++;
        return providedAnswer.equals(currentAnswer);
    }

    public void nextQuestion() {
        if (questionNumber < numOfQuestions) {
            progressText = "Question " + (questionNumber + 1) + " out of " + numOfQuestions;
            String leftNum = String.valueOf(problemsArray.get(questionNumber).getLeftNum());
            String rightNum = String.valueOf(problemsArray.get(questionNumber).getRightNum());
            String operator = "";
            switch (problemsArray.get(questionNumber).getOperation()) {
                case MULTIPLY:
                    operator = " X ";
                    break;
                case ADD:
                    operator = " + ";
                    break;
                case SUBTRACT:
                    operator = " - ";
                    break;
            }
            currentAnswer = String.valueOf(problemsArray.get(questionNumber).getAnswer());
            String wrongAnswer = String.valueOf(problemsArray.get(questionNumber).getWrongAnswer());
            String[] answers = {currentAnswer, wrongAnswer};
            int randNum = randNumInc(0, 1);
            answerLeftText = answers[randNum];
            answerRightText = answers[1 - randNum];
            problemText = leftNum + operator + rightNum + " = ?";
            questionNumber++;
        } else {
            isGameOver = true;
        }
    }

    private int randNumInc(int min, int max) {
        Random randGenerator = new Random();
        return randGenerator.nextInt(max - min + 1) + min;
    }

    public String getProgressText() {
        return progressText;
    }

    public String getAnswerLeftText() {
        return answerLeftText;
    }

    public String getAnswerRightText() {
        return answerRightText;
    }

    public String getProblemText() {
        return problemText;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getCorrectAnswersTally() {
        return correctAnswersTally;
    }
}
