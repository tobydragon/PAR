package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.cohort.CohortDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.json.StudentModel;
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

    public void addTimeSeen(String studentId, String questionId){
        logger.info("addTimeSeen for : "+studentId + ", " + questionId);
        studentModelDatasource.addTimeSeen(studentId, questionId);
    }

    public void addResponse(String studentId, String questionId, String newResponseText){
        logger.info("addResponse for : "+studentId + ", " + questionId +", "+ newResponseText);
        studentModelDatasource.addResponse(studentId, questionId, newResponseText);
    }

}
