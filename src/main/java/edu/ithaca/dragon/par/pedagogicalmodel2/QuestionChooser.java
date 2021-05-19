package edu.ithaca.dragon.par.pedagogicalmodel2;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasource;

public interface QuestionChooser {
    Question chooseQuestion(String studentId, DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource);
}