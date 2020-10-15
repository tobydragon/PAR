package edu.ithaca.dragon.par.domainModel;

public class QuestionOrderedInfo {
    private final String questionID;
    private final boolean includesFollowup;

    public QuestionOrderedInfo(){
        this.questionID = null;
        this.includesFollowup = false;
    }

    public QuestionOrderedInfo(String questionID, boolean includesFollowup) {
        this.questionID = questionID;
        this.includesFollowup = includesFollowup;
    }

    public String getQuestionID() {
        return questionID;
    }

    public boolean isIncludesFollowup() {
        return includesFollowup;
    }
}
