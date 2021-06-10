package edu.ithaca.dragon.par;

import java.util.List;

import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;

public class ParServer {
    private final DomainDatasource domainDatasource;
    private final StudentModelDatasource studentModelDatasource;
    private final CohortDatasource cohortDatasource;

    public ParServer(DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource, CohortDatasource cohortDatasource) {
        this.domainDatasource = domainDatasource;
        this.studentModelDatasource = studentModelDatasource;
        this.cohortDatasource = cohortDatasource;
    }

    public Question getCurrentQuestion(String studentId){
        return cohortDatasource.getQuestionChooser(studentId).chooseQuestion(studentId, domainDatasource, studentModelDatasource);
    }

    public List<String> getCohortIds(){
        return cohortDatasource.getCohortIds();
    }

    public void addTimeSeen(String studentId, String questionId){
        studentModelDatasource.addTimeSeen(studentId, questionId);
    }

    public void addResponse(String studentId, String questionId, String newResponseText){
        studentModelDatasource.addResponse(studentId, questionId, newResponseText);
    }
}
