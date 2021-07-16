package edu.ithaca.dragon.par.pedagogy;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.student.json.QuestionHistory;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);

        //new Student test
        StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");
        assertEquals("skyQ1",questionChooser.chooseQuestion(newStudentModel,domainDatasource).getId());


        //first concept competent test
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        assertEquals("mathQ1",questionChooser.chooseQuestion(studentModelFirstConceptCompetent,domainDatasource).getId());


        //first concept exemplary test
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        assertEquals("mathQ1",questionChooser.chooseQuestion( studentModelFirstConceptExemplary,domainDatasource).getId());

        //all concepts answered correctly at least once test
        StudentModelJson studentModelLastSeenTest = studentModelDatasource.getStudentModel("allConceptsCorrectAtLeastOnce");
        assertEquals("skyQ3",questionChooser.chooseQuestion(studentModelLastSeenTest, domainDatasource).getId());

        //all concepts exemplary all w/ same time seen (makes all concepts developing)
        StudentModelJson studentModelAllConceptsExemplary = studentModelDatasource.getStudentModel("AllConceptsExemplary");
        assertEquals("skyQ1",questionChooser.chooseQuestion(studentModelAllConceptsExemplary,domainDatasource).getId());

        
        StudentModelJson studentModelAllConceptsUnprepared = studentModelDatasource.getStudentModel("AllConceptsUnprepared");
        assertEquals("skyQ2",questionChooser.chooseQuestion(studentModelAllConceptsUnprepared, domainDatasource).getId());
        
        
        DomainDatasource domainDatasourceFQ = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestionsFollowUp.json");
        QuestionChooserByOrderedConcepts questionChooserFQ = new QuestionChooserByOrderedConcepts(domainDatasourceFQ.retrieveAllConcepts(),3);
        StudentModelJson firstConceptCompetentFQ= studentModelDatasource.getStudentModel("firstConceptCompetentFQ");
        //follow up question should be kept 
        Question qWithFQ1 = questionChooserFQ.chooseQuestion(firstConceptCompetentFQ, domainDatasourceFQ);
        assertEquals("skyQ3",qWithFQ1.getId());
        assertEquals(1,qWithFQ1.getFollowupQuestions().size());

        //follow up question shouldn't be kept
        StudentModelJson firstConceptDevelopingFQ = studentModelDatasource.getStudentModel("firstConceptDevelopingFQ");
        Question qWithFQ2 = questionChooserFQ.chooseQuestion(firstConceptDevelopingFQ, domainDatasourceFQ);
        assertEquals("skyQ3",qWithFQ2.getId());
        assertEquals(0,qWithFQ2.getFollowupQuestions().size());

    }

    @Test
    public void updateConceptScoresBasedOnPerformanceDataTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);

        //new student test
        List<ConceptRubricPair> conceptScores1 = new ArrayList<>();
        conceptScores1.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores1.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        

        StudentModelJson newStudent = studentModelDatasource.getStudentModel("newStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(conceptScores1,newStudent, domainDatasource);
        
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(0).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(1).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(2).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(3).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(4).getScore());



        // first concept exemplary test

        questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);

        List<ConceptRubricPair> conceptScores2 = new ArrayList<>();
        conceptScores2.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores2.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        
        
        
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(conceptScores2, studentModelFirstConceptExemplary, domainDatasource);
        
        assertEquals(OrderedConceptRubric.EXEMPLARY,conceptScores2.get(0).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(1).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(2).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(3).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(4).getScore());

        // Does follow up question affect concept score
        List<ConceptRubricPair> conceptScores3 = new ArrayList<>();
        conceptScores3.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores3.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores3.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores3.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores3.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        
        
        // followup question results in exception thrown for invalid question id
        StudentModelJson firstConceptFQAnswered = studentModelDatasource.getStudentModel("firstConceptFQAnswered");
        QuestionChooserByOrderedConcepts questionChooser1 = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
        assertThrows(IllegalArgumentException.class, () -> questionChooser1.updateConceptScoresBasedOnPerformanceData(conceptScores3, firstConceptFQAnswered, domainDatasource));

        // assertEquals(OrderedConceptRubric.COMPETENT,conceptScores3.get(0).getScore());
        // assertEquals(OrderedConceptRubric.DEVELOPING,conceptScores3.get(1).getScore());
        // assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores3.get(2).getScore());
        // assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores3.get(3).getScore());
        // assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores3.get(4).getScore());


    }

    @Test
    public void updateConceptScoresBasedOnComparativeResultsTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);

        List<ConceptRubricPair> conceptScores1 = new ArrayList<>();
        conceptScores1.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores1.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));

        StudentModelJson newStudent = studentModelDatasource.getStudentModel("newStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(conceptScores1, newStudent, domainDatasource);

        conceptScores1 = questionChooser.updateConceptScoresBasedOnComparativeResults(conceptScores1);
        
        assertEquals(OrderedConceptRubric.DEVELOPING,conceptScores1.get(0).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(1).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(2).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(3).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores1.get(4).getScore());



        questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);

        List<ConceptRubricPair> conceptScores2 = new ArrayList<>();
        conceptScores2.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores2.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores2.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        

        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionChooser.updateConceptScoresBasedOnPerformanceData(conceptScores2, studentModelFirstConceptExemplary, domainDatasource);
        conceptScores2 = questionChooser.updateConceptScoresBasedOnComparativeResults(conceptScores2);

        assertEquals(OrderedConceptRubric.EXEMPLARY,conceptScores2.get(0).getScore());
        assertEquals(OrderedConceptRubric.DEVELOPING,conceptScores2.get(1).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(2).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(3).getScore());
        assertEquals(OrderedConceptRubric.UNPREPARED,conceptScores2.get(4).getScore());

    }

    @Test
    public void calcUnpreparedTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
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
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
        
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
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
        
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
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
        
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
        assertEquals(0,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("math", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("major", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("year", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserByOrderedConcepts.retrieveQuestionsFromStudentModelByConcept("google", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());


    }

    @Test
    public void checkChooserConceptIdsAreInDomainTest() throws IOException{

        List<String> conceptsExtra = new ArrayList<>();
        conceptsExtra.add("sky");
        conceptsExtra.add("math");
        conceptsExtra.add("major");
        conceptsExtra.add("year");
        conceptsExtra.add("google");
        conceptsExtra.add("extra");
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(conceptsExtra,3);
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        assertFalse(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));

        // Returns true for case when chooser doesn't have all the domain's concepts 
        List<String> conceptsRemoved = new ArrayList<>();
        conceptsRemoved.add("math");
        conceptsRemoved.add("major");
        conceptsRemoved.add("year");
        conceptsRemoved.add("google");
        questionChooser = new QuestionChooserByOrderedConcepts(conceptsRemoved,3);
        assertTrue(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));


        List<String> conceptsExact = new ArrayList<>();
        conceptsExact.add("sky");
        conceptsExact.add("math");
        conceptsExact.add("major");
        conceptsExact.add("year");
        conceptsExact.add("google");
        questionChooser = new QuestionChooserByOrderedConcepts(conceptsExact,3);
        assertTrue(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));

        List<String> noConcepts = new ArrayList<>();
        QuestionChooserByOrderedConcepts questionChooserThrows = new QuestionChooserByOrderedConcepts(noConcepts,3);
        assertThrows(RuntimeException.class, () -> questionChooserThrows.checkAllChooserConceptIdsAreInDomain(domainDatasource));


    }

    @Test
    public void hasUnpreparedFollowUpQuestionsTest() throws IOException{
        
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestionsFollowUp.json");
        QuestionChooserByOrderedConcepts questionChooser = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),3);
        
        List<ConceptRubricPair> conceptScores1 = new ArrayList<>();
        conceptScores1.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores1.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));

        // Question w/ no followup
        assertFalse(questionChooser.hasUnpreparedFollowUpQuestions(conceptScores1,domainDatasource.getQuestion("skyQ1")));

        // Question w/ followup unprepared
        assertTrue(questionChooser.hasUnpreparedFollowUpQuestions(conceptScores1, domainDatasource.getQuestion("skyQ3")));
        
        // Question w/ followup prepared
        conceptScores1.set(1,new ConceptRubricPair("math",OrderedConceptRubric.DEVELOPING));
        assertFalse(questionChooser.hasUnpreparedFollowUpQuestions(conceptScores1, domainDatasource.getQuestion("skyQ3")));

    }

    @Test
    public void findScoreByConceptTest(){
        List<ConceptRubricPair> conceptScores1 = new ArrayList<>();
        conceptScores1.add(new ConceptRubricPair("sky",OrderedConceptRubric.DEVELOPING));
        conceptScores1.add(new ConceptRubricPair("math",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("major",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("year",OrderedConceptRubric.UNPREPARED));
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));

        // first concept
        assertEquals(OrderedConceptRubric.DEVELOPING,QuestionChooserByOrderedConcepts.findScoreByConcept(conceptScores1, "sky"));
        
        // last concept
        assertEquals(OrderedConceptRubric.UNPREPARED,QuestionChooserByOrderedConcepts.findScoreByConcept(conceptScores1, "google"));

        // duplicate concept
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        assertThrows(RuntimeException.class, () -> QuestionChooserByOrderedConcepts.findScoreByConcept(conceptScores1, "google"));

        // concept not in conceptScores
        assertThrows(RuntimeException.class, () -> QuestionChooserByOrderedConcepts.findScoreByConcept(conceptScores1, "notFound"));


    }
    
}
