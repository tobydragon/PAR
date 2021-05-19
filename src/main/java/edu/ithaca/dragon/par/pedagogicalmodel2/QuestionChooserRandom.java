package edu.ithaca.dragon.par.pedagogicalmodel2;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasource;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasource;

import java.util.List;
import java.util.Random;

public class QuestionChooserRandom implements QuestionChooser{
    Random rand = new Random();

    @Override
    public Question chooseQuestion(String studentId, DomainDatasource domainDatasource, StudentModelDatasource studentModelDatasource) {
        List<Question> allQuestions =  domainDatasource.getAllQuestions();
        return allQuestions.get((int)(Math.random()*allQuestions.size()));
    }
}
