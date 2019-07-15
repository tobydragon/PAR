package edu.ithaca.dragon.par.domainModel;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String id;
    private String type;
    private String questionText;
    private String correctAnswer;
    private List<String> possibleAnswers;
    private String imageUrl;
    private List<Question> followupQuestions;

    public Question(){
        this.possibleAnswers = new ArrayList<>();
        this.followupQuestions = new ArrayList<>();
    }

    public Question(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn){
        this(idIn, questionTextIn, typeIn, correctAnswerIn, answersIn, imageUrlIn, new ArrayList<>());
    }

    public Question(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn, List<Question> followupQuestionsIn) {
        this.id = idIn;
        this.questionText = questionTextIn;
        this.type = typeIn;
        this.correctAnswer = correctAnswerIn;
        this.possibleAnswers = answersIn;
        this.imageUrl = imageUrlIn;
        this.followupQuestions = followupQuestionsIn;
    }

    public Question(Question toCopy, List<Question> differentFollowups){
        this(toCopy.id, toCopy.questionText, toCopy.type, toCopy.correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups);
    }

    public String getId() {return id;}
    public void setId(String idIn) {id = idIn;}

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String textIn) {questionText = textIn;}

    public String getType() { return type; }
    public void setType(String typeIn) { type = typeIn;}

    public String getCorrectAnswer() {return correctAnswer;}
    public void setCorrectAnswer(String correctAnswerIn) {correctAnswer = correctAnswerIn;}

    public List<String> getPossibleAnswers() {return possibleAnswers;}
    public void setPossibleAnswers(List<String> answersIn) { possibleAnswers = answersIn;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrlIn) {imageUrl =imageUrlIn; }

    public List<Question> getFollowupQuestions() {return followupQuestions;}
    public void setFollowupQuestions(List<Question> followupQuestionsIn) {
        followupQuestions = followupQuestionsIn; }

    @Override
    public String toString() {
        return "Question [id=" + id + ", text=" + questionText + ", type=" + type + "]";
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
                && this.getType().equals(other.getType())
                && this.getCorrectAnswer().equals(other.getCorrectAnswer())
                && this.getPossibleAnswers().equals(other.getPossibleAnswers())
                && this.getImageUrl().equals(other.getImageUrl());
    }
}
