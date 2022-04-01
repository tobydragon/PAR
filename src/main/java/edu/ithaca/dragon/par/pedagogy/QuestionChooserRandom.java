package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;

import java.util.List;
import java.util.Random;

public class QuestionChooserRandom implements QuestionChooser{
    Random rand = new Random();

    @Override
    public Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource) {
        List<Question> allQuestions =  domainDatasource.getAllQuestions();
        return allQuestions.get((int)(Math.random()*allQuestions.size()));
    }
}