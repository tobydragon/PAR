package edu.ithaca.dragon.par.deprecated.io;

import edu.ithaca.dragon.par.authorModel.AuthorModel;
import edu.ithaca.dragon.par.deprecated.domainModel.QuestionPool;
import edu.ithaca.dragon.par.deprecated.studentModel.QuestionCount;

import java.util.ArrayList;
import java.util.List;

public class AuthorModelRecord {
    public String authorId;
    public List<QuestionCountRecord> questionCountRecords;

    public AuthorModelRecord(){
        this.questionCountRecords = new ArrayList<>();
    }

    public AuthorModelRecord(AuthorModel authorModel){
        this();
        this.authorId = authorModel.getAuthorId();
        for(QuestionCount questionCount : authorModel.getQuestionCountList()){
            questionCountRecords.add(new QuestionCountRecord(questionCount));
        }
    }

    public AuthorModel buildAuthorModel(QuestionPool questionPool){
        List<QuestionCount> questionCountList = new ArrayList<>();
        for(QuestionCountRecord questionCountRecord : questionCountRecords){
            questionCountList.add(questionCountRecord.buildQuestionCount(questionPool));
        }
        return new AuthorModel(authorId, questionCountList);
    }


}
