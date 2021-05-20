package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;
import edu.ithaca.dragon.par.pedagogicalmodel2.QuestionChooser;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasource;

public class ParServer {

    //TODO: cohort needs to be kept here somehow, but complicated due to many-to-many relationship
    //When we store a cohort, we will need to use special json syntax for the questionChooser:
    // https://octoperf.com/blog/2018/02/01/polymorphism-with-jackson/#polymorphism
    private final DomainDatasource domainDatasource;
    private final StudentModelDatasource studentModelDatasource;
    private final QuestionChooser questionChooser;

    public ParServer(DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource, QuestionChooser questionChooser) {
        this.domainDatasource = domainDatasource;
        this.studentModelDatasource = studentModelDatasource;
        this.questionChooser = questionChooser;
    }

    public Question getCurrentQuestion(String studentId){
        return questionChooser.chooseQuestion(studentId, domainDatasource, studentModelDatasource);
    }

    public void addTimeSeen(String studentId, String questionId){
        studentModelDatasource.addTimeSeen(studentId, questionId);
    }

    public void addResponse(String studentId, String questionId, String newResponseText){
        studentModelDatasource.addResponse(studentId, questionId, newResponseText);
    }

}
