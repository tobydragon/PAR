package edu.ithaca.dragon.par.deprecated.io;

public class QuestionSeenJson {
    public String userId;
    public String questionId;

    public QuestionSeenJson(){}

    @Override
    public String toString() {
        return "QuestionSeenJson{" +
                "userId='" + userId + '\'' +
                ", questionId='" + questionId + '\'' +
                '}';
    }
}
