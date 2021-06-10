package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.json.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionChooserInOrder  implements  QuestionChooser{

    public ArrayList<String> questionIdsInOrder;

    public QuestionChooserInOrder(){}

    public QuestionChooserInOrder(List<String> questionIdsInOrder){
        this.questionIdsInOrder = new ArrayList<>(questionIdsInOrder);
    }

    @Override
    public Question chooseQuestion(StudentModel studentModel, DomainDatasource domainDatasource) {
        String questionId = studentModel.findQuestionSeenLeastRecently(questionIdsInOrder);
        return domainDatasource.getQuestion(questionId);
    }
}
