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
import java.util.ArrayList;
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
    QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());

    //new Student test
    StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");
    assertEquals("skyQ1",questionChooser.chooseQuestion(newStudentModel,domainDatasource).getId());

    

    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());
    

    


    //first concept competent test
    StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
    
    assertEquals("mathQ1",questionChooser.chooseQuestion(studentModelFirstConceptCompetent,domainDatasource).getId());

    assertEquals(OrderedConceptRubric.COMPETENT,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());
    
    

    //first concept exemplary test

    StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
    assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptExemplary,domainDatasource).getId());

    assertEquals(OrderedConceptRubric.EXEMPLARY,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

    
    
    

    }
}
