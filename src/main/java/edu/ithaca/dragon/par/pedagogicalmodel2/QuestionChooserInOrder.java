package edu.ithaca.dragon.par.pedagogicalmodel2;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasource;

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
