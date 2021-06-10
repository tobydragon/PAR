package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;

import java.util.ArrayList;
import java.util.List;

public class QuestionChooserInOrder  implements  QuestionChooser{

    public ArrayList<String> questionIdsInOrder;

    public QuestionChooserInOrder(){}

    public QuestionChooserInOrder(List<String> questionIdsInOrder){
        this.questionIdsInOrder = new ArrayList<>(questionIdsInOrder);
    }

    @Override
    public Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource) {
        String questionId = studentModelInfo.findQuestionSeenLeastRecently(questionIdsInOrder);
        return domainDatasource.getQuestion(questionId);
    }
}
