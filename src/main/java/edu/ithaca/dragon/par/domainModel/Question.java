package edu.ithaca.dragon.par.domainModel;

import edu.ithaca.dragon.util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {

    private String id;
    private String type;
    private String questionText;
    private String correctAnswer;
    private List<String> possibleAnswers;
    private String imageUrl;
    private List<Question> followupQuestions;
    private String feedback;

    public Question(){
        this.possibleAnswers = new ArrayList<>();
        this.followupQuestions = new ArrayList<>();
    }

    public Question(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn){
        this(idIn, questionTextIn, typeIn, correctAnswerIn, answersIn, imageUrlIn, new ArrayList<>(), null);
    }

    public Question(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn, String feedbackIn){
        this(idIn, questionTextIn, typeIn, correctAnswerIn, answersIn, imageUrlIn, new ArrayList<>(), feedbackIn);
    }

    public Question(String idIn, String questionTextIn, String typeIn, String correctAnswerIn, List<String> answersIn, String imageUrlIn, List<Question> followupQuestionsIn, String feedbackIn) {
        this.id = idIn;
        this.questionText = questionTextIn;
        this.type = typeIn;
        this.correctAnswer = correctAnswerIn;
        this.possibleAnswers = answersIn;
        this.imageUrl = imageUrlIn;
        this.followupQuestions = followupQuestionsIn;
        this.feedback = feedbackIn;
    }

    public Question(Question toCopy, String correctAnswer, List<Question> differentFollowups){
        this(toCopy.id, toCopy.questionText, toCopy.type, correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups, toCopy.feedback);
    }

    //used for AuthorServer.buildQuestionFromTemplate for custom questions
    public Question(Question toCopy, String questionText, String correctAnswer, List<Question> differentFollowups){
        this(toCopy.id, questionText, toCopy.type, correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups, toCopy.feedback);
    }

    public Question(Question toCopy, List<Question> differentFollowups){
        this(toCopy.id, toCopy.questionText, toCopy.type, toCopy.correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups, toCopy.feedback);
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

    public String getFeedback() {return feedback;}
    public void setFeedback(String feedback) {this.feedback = feedback;}


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
        if (!Objects.equals(this.getQuestionText(), other.getQuestionText())) {
            return false;
        }
        if (!Objects.equals(this.getCorrectAnswer(), other.getCorrectAnswer())) {
            return false;
        }
        return this.getId().equals(other.getId())
                && this.getType().equals(other.getType())
                && this.getPossibleAnswers().equals(other.getPossibleAnswers())
                && this.getImageUrl().equals(other.getImageUrl());
    }
}
