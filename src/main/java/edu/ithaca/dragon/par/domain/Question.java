package edu.ithaca.dragon.par.domain;

import java.util.List;

public class Question {

    private String id;
    private int difficulty;
    private String questionText;
    private String correctAnswer;
    private List<String> answers;

    public Question(){}

    public Question(String idIn, String questionTextIn, int difficultyIn, String correctAnswerIn, List<String> answersIn){
        this.id = idIn;
        this.questionText = questionTextIn;
        this.difficulty = difficultyIn;
        this.correctAnswer = correctAnswerIn;
        this.answers = answersIn;
    }

    public String getId() {return id;}
    public void setId(String idIn) {id = idIn;}

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String textIn) {questionText = textIn;}

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficultyIn) { difficulty = difficultyIn;}

    public String getCorrectAnswer() {return correctAnswer;}
    public void setCorrectAnswer(String correctAnswerIn) {correctAnswer = correctAnswerIn;}

    public List<String> getAnswers() {return answers;}
    public void setAnswers(List<String> answersIn) {answers = answersIn;}

    @Override
    public String toString() {
        return "Question [id=" + id + ", text=" + questionText + ", difficulty=" + difficulty + "]";
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!Question.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        Question other = (Question) otherObj;
        return this.getId().equals(other.getId())
                && this.getQuestionText().equals(other.getQuestionText())
                && this.getDifficulty() == (other.getDifficulty())
                && this.getCorrectAnswer().equals(other.getCorrectAnswer())
                && this.getAnswers().equals(other.getAnswers());
    }
}
