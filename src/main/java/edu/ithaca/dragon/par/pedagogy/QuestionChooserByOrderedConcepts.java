package edu.ithaca.dragon.par.pedagogy;

import java.util.List;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.StudentModelInfo;



public class QuestionChooserByOrderedConcepts {
    //Each concept(type) has an average of last <window-size> questions which gets bucketed
    //rubric-style: unprepared,developing,competent,exemplary
    //having too few questions answered puts you automatically into unprepared
    //concepts are ordered
    //unprepared on next topic can get turned to developing by competent grade on prior
    //exemplary on the prior topic can get turned into competent by current topic
    //only ask questions on topics that are developing or competent

    //Find questions of the right type(s) that have the min seen count.
    //If a minSeen question has the same URL as last question, take that, otherwise go for first minSeen

    public QuestionChooserByOrderedConcepts(){}

    public Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){
        //Algorithm: 
        // 1. get all questions from domain datasource
        // 2. select the first concept from the list of questions to be developing, the rest are unprepared
        // 3. (Once competent, the next concept will be developing)
        // 4. (Once exemplary, the next concept will be updated to competent)
        // 5. from the developing and competent buckets, the question asked will be chosen based on least recently seen
        // *Test case for when first two topics are exemplary and the next 
        return new Question();
    }

    public List<Question> calcUnprepared(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){}
    public List<Question> calcDeveloping(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){}
    public List<Question> calcCompetent(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){}
    public List<Question> calcExemplary(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){}
}
