package edu.ithaca.dragon.par.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {

    public String id;
    public String type;
    public String questionText;
    public String correctAnswer;
    public List<String> possibleAnswers;
    public String imageUrl;
    public List<Question> followupQuestions;

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
        //TODO: This should be a resourceId (or a list of them), rather than an imageURl
        this.imageUrl = imageUrlIn;
        this.followupQuestions = followupQuestionsIn;
    }

    public Question(Question toCopy, String correctAnswer, List<Question> differentFollowups){
        this(toCopy.id, toCopy.questionText, toCopy.type, correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups);
    }

    //used for AuthorServer.buildQuestionFromTemplate for custom questions
    public Question(Question toCopy, String questionText, String correctAnswer, List<Question> differentFollowups){
        this(toCopy.id, questionText, toCopy.type, correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups);
    }

    public Question(Question toCopy, List<Question> differentFollowups){
        this(toCopy.id, toCopy.questionText, toCopy.type, toCopy.correctAnswer, new ArrayList<>(toCopy.possibleAnswers), toCopy.imageUrl, differentFollowups);
    }

    public String getType(){
        return type;
    }

    public String getId(){
        return id;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<Question> getFollowupQuestions() {
        return followupQuestions;
    }

    public void setFollowupQuestions(List<Question> newFollowupQuestions){
        this.followupQuestions = newFollowupQuestions;
    }

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
        if (!Objects.equals(this.questionText, other.questionText)) {
            return false;
        }
        if (!Objects.equals(this.correctAnswer, other.correctAnswer)) {
            return false;
        }
        return this.id.equals(other.id)
                && this.type.equals(other.type)
                && this.possibleAnswers.equals(other.possibleAnswers)
                && this.imageUrl.equals(other.imageUrl);
    }
}
