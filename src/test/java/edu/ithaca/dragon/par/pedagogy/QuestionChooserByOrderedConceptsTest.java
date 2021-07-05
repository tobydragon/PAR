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

    StudentModelJson studentModelLastSeenTest = studentModelDatasource.getStudentModel("allConceptsAnsweredCorrectlyOnce");

    assertEquals("skyQ3",questionChooser.chooseQuestion(studentModelLastSeenTest, domainDatasource).getId());

    }

    @Test
    public void updateConceptScoresBasedOnPerformanceDataTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());

        
        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

        StudentModelJson newStudent = studentModelDatasource.getStudentModel("newStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(newStudent, domainDatasource);
        
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());



        questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        
        
        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(studentModelFirstConceptExemplary, domainDatasource);
        
        assertEquals(OrderedConceptRubric.EXEMPLARY,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

    }

    @Test
    public void updateConceptScoresBasedOnComparativeResultsTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());

        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

        StudentModelJson newStudent = studentModelDatasource.getStudentModel("newStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(newStudent, domainDatasource);
        questionChooser.updateConceptScoresBasedOnComparativeResults();
        
        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());



        questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());

        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(studentModelFirstConceptExemplary, domainDatasource);
        questionChooser.updateConceptScoresBasedOnComparativeResults();
        
        assertEquals(OrderedConceptRubric.EXEMPLARY,questionChooser.conceptScores.get(0).getValue());
        assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(1).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
        assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());

    }

    @Test
    public void calcUnpreparedTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        
        // unprepared case
        boolean isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isUnprepared);
        
        // exemplary case
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);


        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        
        // developing case
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);
        
        
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        
        // competent case
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);

    }

    @Test
    public void calcDevelopingTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        
        // developing case
        boolean isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isDeveloping);


        // unprepared case
        isDeveloping = questionChooser.calcDeveloping("year",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);

        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        

        // exemplary case
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
        
        
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        
        // competent case
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
    }

    @Test
    public void calcCompetentTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();

        boolean isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isCompetent);

        isCompetent = questionChooser.calcCompetent("math", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("math", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();

        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);


        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();

        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);
        
    }

    @Test
    public void calcExemplaryTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        
        // exemplary first concept case
        boolean isExemplary = questionChooser.calcExemplary("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isExemplary);
        
        
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        // developing case
        isExemplary = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);


        // unprepared case
        isExemplary = questionChooser.calcExemplary("year", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);

        
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        // competent case
        isExemplary = questionChooser.calcExemplary("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);
    }

    @Test
    public void retrieveQuestionsFromStudentModelByConceptTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");

        assertEquals(0,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", newStudentModel.getQuestionHistories().values(), domainDatasource).size());

        StudentModelJson firstConceptExemplaryStudentModel = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        assertEquals(3,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());


    }
    
}
