package edu.ithaca.dragon.par.pedagogicalmodel2;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;

public interface QuestionChooser {

    Question chooseQuestion(DomainDatasource domainDatasource);
}
