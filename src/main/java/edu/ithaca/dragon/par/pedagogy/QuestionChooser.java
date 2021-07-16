package edu.ithaca.dragon.par.pedagogy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property ="type")
@JsonSubTypes({@JsonSubTypes.Type(value = QuestionChooserRandom.class, name = "QuestionChooserRandom"),@JsonSubTypes.Type(value = QuestionChooserInOrder.class, name = "QuestionChooserInOrder"), @JsonSubTypes.Type(value=QuestionChooserByOrderedConcepts.class,name="QuestionChooserByOrderedConcepts")})
public interface QuestionChooser {
    Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource);
}