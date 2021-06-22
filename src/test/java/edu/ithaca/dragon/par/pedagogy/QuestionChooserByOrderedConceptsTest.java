package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceSimple;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
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
    @Test
    public void chooseQuestionTest() throws IOException{
    //new Student test
    DomainDatasource domainDatasource = new DomainDatasourceSimple(JsonUtil.listFromJsonFile("src/test/resources/rewrite/QuestionChooserSampleQuestions.json", Question.class));
    StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
    StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");
    QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts();
    assertEquals("skyQ1",questionChooser.chooseQuestion(newStudentModel,domainDatasource).getId());

    assertEquals("skyQ1",questionChooser.calcDeveloping(newStudentModel, domainDatasource).get(0).getId());
    assertEquals("skyQ2",questionChooser.calcDeveloping(newStudentModel, domainDatasource).get(1).getId());
    assertEquals("skyQ3",questionChooser.calcDeveloping(newStudentModel, domainDatasource).get(2).getId());


    //first concept competent test

    StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
    
    assertEquals("skyQ1",questionChooser.calcCompetent(studentModelFirstConceptCompetent, domainDatasource).get(0).getId());
    assertEquals("skyQ2",questionChooser.calcCompetent(studentModelFirstConceptCompetent, domainDatasource).get(1).getId());
    assertEquals("skyQ3",questionChooser.calcCompetent(studentModelFirstConceptCompetent, domainDatasource).get(2).getId());

    assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptCompetent,domainDatasource).getId());

    assertEquals("mathQ1",questionChooser.calcDeveloping(studentModelFirstConceptCompetent, domainDatasource).get(0).getId());
    assertEquals("mathQ2",questionChooser.calcDeveloping(studentModelFirstConceptCompetent, domainDatasource).get(1).getId());

    //first concept exemplary test

    StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");

    assertEquals("skyQ1",questionChooser.calcExemplary(studentModelFirstConceptExemplary, domainDatasource).get(0).getId());
    assertEquals("skyQ2",questionChooser.calcExemplary(studentModelFirstConceptExemplary, domainDatasource).get(1).getId());
    assertEquals("skyQ3",questionChooser.calcExemplary(studentModelFirstConceptExemplary, domainDatasource).get(2).getId());

    assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptExemplary,domainDatasource).getId());

    assertEquals("mathQ1",questionChooser.calcCompetent(studentModelFirstConceptExemplary, domainDatasource).get(0).getId());
    assertEquals("mathQ2",questionChooser.calcCompetent(studentModelFirstConceptExemplary, domainDatasource).get(1).getId());
    
    assertEquals("majorQ1",questionChooser.calcDeveloping(studentModelFirstConceptExemplary, domainDatasource).get(0).getId());
    assertEquals("majorQ2",questionChooser.calcDeveloping(studentModelFirstConceptExemplary, domainDatasource).get(1).getId());

    

    }
}
