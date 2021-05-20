package edu.ithaca.dragon.par.pedagogy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property ="type")
@JsonSubTypes({@JsonSubTypes.Type(value = QuestionChooserRandom.class, name = "QuestionChooserRandom"),@JsonSubTypes.Type(value = QuestionChooserInOrder.class, name = "QuestionChooserInOrder") })
public interface QuestionChooser {
    Question chooseQuestion(String studentId, DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource);
}