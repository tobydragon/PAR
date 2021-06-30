package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.QuestionHistory;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

class QuestionChooserByOrderedConceptsTest{

    //level 1: sky
    //level 2: math
    //level 3: major
    //level 4: year
    //level 5: google
    @Test
    public void chooseQuestionTest() throws IOException{
    
    DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
    StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
    // QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.getAllConcepts());

    //new Student test
    StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");
    //assertEquals("skyQ1",questionChooser.chooseQuestion(newStudentModel,domainDatasource).getId());

    

    // assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScoring.get("sky"));
    // assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScoring.get("math"));
    // assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScoring.get("major"));
    // assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScoring.get("year"));
    // assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScoring.get("google"));
    

    


    //first concept competent test

    StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
    Collection<QuestionHistory> firstConceptCompetentQuestionHistories = studentModelFirstConceptCompetent.getQuestionHistories();
    
    // assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptCompetent,domainDatasource).getId());

    // assertTrue(questionChooser.calcCompetent("sky", firstConceptCompetentQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcDeveloping("math",firstConceptCompetentQuestionHistories,domainDatasource));
    // assertTrue(questionChooser.calcUnprepared("major", firstConceptCompetentQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcUnprepared("year", firstConceptCompetentQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcUnprepared("google", firstConceptCompetentQuestionHistories, domainDatasource));
    
    

    //first concept exemplary test

    StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
    Collection<QuestionHistory> firstConceptExemplaryQuestionHistories = studentModelFirstConceptExemplary.getQuestionHistories();
   

    // assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptExemplary,domainDatasource).getId());

    // assertTrue(questionChooser.calcExemplary("sky", firstConceptExemplaryQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcCompetent("math", firstConceptExemplaryQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcDeveloping("major", firstConceptExemplaryQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcUnprepared("year", firstConceptExemplaryQuestionHistories, domainDatasource));
    // assertTrue(questionChooser.calcUnprepared("google", firstConceptExemplaryQuestionHistories, domainDatasource));

    
    
    

    }
}
