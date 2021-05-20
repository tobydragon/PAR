package edu.ithaca.dragon.par.pedagogicalmodel2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasource;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property ="type")
@JsonSubTypes({@JsonSubTypes.Type(value = QuestionChooserRandom.class, name = "QuestionChooserRandom"),@JsonSubTypes.Type(value = QuestionChooserInOrder.class, name = "QuestionChooserInOrder") })
public interface QuestionChooser {
    Question chooseQuestion(String studentId, DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource);
}