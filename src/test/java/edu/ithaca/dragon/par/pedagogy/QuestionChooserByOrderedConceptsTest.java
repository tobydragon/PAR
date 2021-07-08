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

    //all concepts answered correctly at least once test
    StudentModelJson studentModelLastSeenTest = studentModelDatasource.getStudentModel("allConceptsCorrectAtLeastOnce");

    assertEquals("skyQ3",questionChooser.chooseQuestion(studentModelLastSeenTest, domainDatasource).getId());

    assertEquals(OrderedConceptRubric.COMPETENT,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(4).getValue());

    //all concepts exemplary all w/ same time seen (makes all concepts developing)

    StudentModelJson studentModelAllConceptsExemplary = studentModelDatasource.getStudentModel("AllConceptsExemplary");
    assertEquals("skyQ1",questionChooser.chooseQuestion(studentModelAllConceptsExemplary,domainDatasource).getId());

    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(4).getValue());

    StudentModelJson studentModelAllConceptsUnprepared = studentModelDatasource.getStudentModel("AllConceptsUnprepared");
    assertEquals("skyQ2",questionChooser.chooseQuestion(studentModelAllConceptsUnprepared, domainDatasource).getId());
    
    assertEquals(OrderedConceptRubric.DEVELOPING,questionChooser.conceptScores.get(0).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(1).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(2).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(3).getValue());
    assertEquals(OrderedConceptRubric.UNPREPARED,questionChooser.conceptScores.get(4).getValue());
    }

    @Test
    public void updateConceptScoresBasedOnPerformanceDataTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());

        //new student test

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



        // first concept exemplary test

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
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        
        // unprepared case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        boolean isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isUnprepared);
        
        // exemplary case
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);

        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);

    }

    @Test
    public void calcDevelopingTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        
        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        boolean isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isDeveloping);


        // unprepared case
        isDeveloping = questionChooser.calcDeveloping("year",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);

        // exemplary case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
    }

    @Test
    public void calcCompetentTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        boolean isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isCompetent);

        // unprepared case
        isCompetent = questionChooser.calcCompetent("math", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("math", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        // exemplary case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);
        
    }

    @Test
    public void calcExemplaryTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts());
        
        // exemplary first concept case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        boolean isExemplary = questionChooser.calcExemplary("sky", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isExemplary);
        
        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isExemplary = questionChooser.calcUnprepared(QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);


        // unprepared case
        isExemplary = questionChooser.calcExemplary("year", QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);

        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
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
        assertEquals(2,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("math", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(2,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("major", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(2,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(2,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("google", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());


    }
    
}
