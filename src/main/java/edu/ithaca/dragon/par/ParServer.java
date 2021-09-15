package edu.ithaca.dragon.par;

import java.util.Collection;
import java.util.List;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.json.QuestionHistory;
import edu.ithaca.dragon.par.student.json.StudentModelJson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParServer {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final DomainDatasource domainDatasource;
    private final StudentModelDatasource studentModelDatasource;
    private final CohortDatasource cohortDatasource;

    public ParServer(DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource, CohortDatasource cohortDatasource) {
        this.domainDatasource = domainDatasource;
        this.studentModelDatasource = studentModelDatasource;
        this.cohortDatasource = cohortDatasource;
    }

    public Question getCurrentQuestion(String studentId){
        logger.info("Question for : "+studentId);
        return cohortDatasource.getQuestionChooser(studentId).chooseQuestion(studentModelDatasource.getStudentModel(studentId), domainDatasource);
    }

    public List<String> getCohortIds(){
        return cohortDatasource.getCohortIds();
    }

    public QuestionHistorySummary getQuestionHistorySummary(String studentId){    
        logger.info("QuestionHistorySummary for:" + studentId);    
        return new QuestionHistorySummary(studentModelDatasource.getStudentModel(studentId).getQuestionHistories().values(), domainDatasource);
    }

    public synchronized void addNewUser(String studentId, String cohortId){
        studentModelDatasource.createNewModelForId(studentId);
        cohortDatasource.addStudentToCohort(cohortId, studentId);

    }

    public synchronized void addTimeSeen(String studentId, String questionId){
        logger.info("addTimeSeen for : "+studentId + ", " + questionId);
        studentModelDatasource.addTimeSeen(studentId, questionId);
    }

    public void addResponse(String studentId, String questionId, String newResponseText){
        logger.info("addResponse for : "+studentId + ", " + questionId +", "+ newResponseText);
        studentModelDatasource.addResponse(studentId, questionId, newResponseText);
    }

    public boolean isUserIdAvailable(String studentId){
        logger.info("isUserIdAvailable for : "+studentId);
        return studentModelDatasource.idIsAvailable(studentId);
    }

}
