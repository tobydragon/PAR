package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceSimple;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuestionChooserByOrderedConceptsTest{

    //level 1: sky
    //level 2: math
    //level 3: major
    //level 4: year
    //level 5: google

    public void chooseQuestionTest() throws IOException{
    DomainDatasource domainDatasource = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/SampleQuestions.json", Question.class));
    StudentModelInfo studentModel = new StudentModelJson();
    QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts();
    // assertEquals("generic0",questionChooser.chooseQuestion(StudentModelInfo.class,domainDatasource));
    }
}
