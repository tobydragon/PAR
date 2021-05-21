package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;

import java.util.ArrayList;
import java.util.List;

public class QuestionChooserInOrder  implements  QuestionChooser{

    public ArrayList<String> questionIdsInOrder;

    public QuestionChooserInOrder(){}

    public QuestionChooserInOrder(List<String> questionIdsInOrder){
        this.questionIdsInOrder = new ArrayList<>(questionIdsInOrder);
    }

    @Override
    public Question chooseQuestion( String studentId, DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource) {
        String questionId = studentModelDatasource.findQuestionLeastSeen(studentId, questionIdsInOrder);
        return domainDatasource.getQuestion(questionId);
    }
}
