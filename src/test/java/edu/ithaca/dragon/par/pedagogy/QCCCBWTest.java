package edu.ithaca.dragon.par.pedagogy;

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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class QCCCBWTest {
    private int windowSize = 4;
    @Test
    public void chooseQuestionsTest() throws IOException{
    
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);

        StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");
        int numSkyQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("sky")).collect(Collectors.toList()).size();
        int numMathQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("math")).collect(Collectors.toList()).size();
        int numMajorQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("major")).collect(Collectors.toList()).size();
        int numYearQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("year")).collect(Collectors.toList()).size();
        int numGoogleQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("google")).collect(Collectors.toList()).size();
        assertEquals(numSkyQs,questionChooser.chooseQuestions(newStudentModel,domainDatasource).size());

        //first concept competent
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        assertEquals(numMathQs+1,questionChooser.chooseQuestions(studentModelFirstConceptCompetent,domainDatasource).size());


        //first concept exemplary test, with one q unanswered test
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        assertEquals(numMathQs,questionChooser.chooseQuestions( studentModelFirstConceptExemplary,domainDatasource).size());


        //all concepts exemplary all w/ same time seen (makes all concepts developing)
        StudentModelJson studentModelAllConceptsExemplary = studentModelDatasource.getStudentModel("AllConceptsExemplary");
        assertEquals(1,questionChooser.chooseQuestions(studentModelAllConceptsExemplary,domainDatasource).size());

    }

    @Test
    public void updateConceptScoresBasedOnPerformanceDataTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);

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

        questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);

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
        QuestionChooserChangeConceptByWindow questionChooser1 = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
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
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);

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



        questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);

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
    public void changeConceptTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/EquineUltrasoundSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        // window size is equal to 4
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        StudentModelJson competentConceptChange = studentModelDatasource.getStudentModel("changeConceptTest");

        int numStructureQs = domainDatasource.getAllQuestions().stream().filter(x -> x.getType().equals("structure")).collect(Collectors.toList()).size();
        List<Question> questions = questionChooser.chooseQuestions(competentConceptChange, domainDatasource);
        assertEquals(1+numStructureQs,questions.size());
        assertTrue(questions.stream().map(x->x.getId()).collect(Collectors.toList()).contains("StructureQ1"));
    }

    @Test
    public void calcUnpreparedTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        
        // unprepared case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        boolean isUnprepared = questionChooser.calcUnprepared(QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isUnprepared);
        
        // exemplary case
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);

        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        isUnprepared = questionChooser.calcUnprepared(QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isUnprepared);

    }

    @Test
    public void calcDevelopingTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        
        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        boolean isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isDeveloping);


        // unprepared case
        isDeveloping = questionChooser.calcDeveloping("year",QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);

        // exemplary case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        isDeveloping = questionChooser.calcDeveloping("sky",QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isDeveloping);
    }

    @Test
    public void calcCompetentTest() throws IOException{

        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        
        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        boolean isCompetent = questionChooser.calcCompetent("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        // assertTrue(isCompetent);

        // unprepared case
        isCompetent = questionChooser.calcCompetent("math", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("math", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        // exemplary case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isCompetent);

        //cusp of exemplary, but still competent
        StudentModelJson studentModelCloseToExemplary = studentModelDatasource.getStudentModel("closeToExemplaryStudent");
        questionHistories = studentModelCloseToExemplary.getQuestionHistories().values();
        isCompetent = questionChooser.calcCompetent("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isCompetent);
    }

    @Test
    public void calcExemplaryTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        
        // exemplary first concept case
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        Collection<QuestionHistory> questionHistories = studentModelFirstConceptExemplary.getQuestionHistories().values();
        boolean isExemplary = questionChooser.calcExemplary("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertTrue(isExemplary);
        
        // developing case
        StudentModelJson studentModelFirstConceptDeveloping = studentModelDatasource.getStudentModel("firstConceptDeveloping");
        questionHistories = studentModelFirstConceptDeveloping.getQuestionHistories().values();
        isExemplary = questionChooser.calcUnprepared(QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);


        // unprepared case
        isExemplary = questionChooser.calcExemplary("year", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("year", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);

        // competent case
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        questionHistories = studentModelFirstConceptCompetent.getQuestionHistories().values();
        isExemplary = questionChooser.calcExemplary("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);

        //cusp of exemplary, but still competent
        StudentModelJson studentModelCloseToExemplary = studentModelDatasource.getStudentModel("closeToExemplaryStudent");
        questionHistories = studentModelCloseToExemplary.getQuestionHistories().values();
        isExemplary = questionChooser.calcExemplary("sky", QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", questionHistories, domainDatasource), questionHistories, domainDatasource);
        assertFalse(isExemplary);
    }

    @Test
    public void retrieveQuestionsFromStudentModelByConceptTest() throws IOException{
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        StudentModelDatasourceJson studentModelDatasource = new StudentModelDatasourceJson("chooserExample", "src/test/resources/rewrite/questionChooserSampleStudents", new JsonIoHelperDefault());
        StudentModelJson newStudentModel = studentModelDatasource.getStudentModel("newStudent");

        assertEquals(0,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", newStudentModel.getQuestionHistories().values(), domainDatasource).size());

        StudentModelJson firstConceptExemplaryStudentModel = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        assertEquals(4,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("sky", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("math", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("major", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("year", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());
        assertEquals(0,QuestionChooserChangeConceptByWindow.retrieveQuestionsFromStudentModelByConcept("google", firstConceptExemplaryStudentModel.getQuestionHistories().values(), domainDatasource).size());


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
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(conceptsExtra,windowSize);
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestions.json");
        assertFalse(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));

        // Returns true for case when chooser doesn't have all the domain's concepts 
        List<String> conceptsRemoved = new ArrayList<>();
        conceptsRemoved.add("math");
        conceptsRemoved.add("major");
        conceptsRemoved.add("year");
        conceptsRemoved.add("google");
        questionChooser = new QuestionChooserChangeConceptByWindow(conceptsRemoved,windowSize);
        assertTrue(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));


        List<String> conceptsExact = new ArrayList<>();
        conceptsExact.add("sky");
        conceptsExact.add("math");
        conceptsExact.add("major");
        conceptsExact.add("year");
        conceptsExact.add("google");
        questionChooser = new QuestionChooserChangeConceptByWindow(conceptsExact,windowSize);
        assertTrue(questionChooser.checkAllChooserConceptIdsAreInDomain(domainDatasource));

        List<String> noConcepts = new ArrayList<>();
        QuestionChooserChangeConceptByWindow questionChooserThrows = new QuestionChooserChangeConceptByWindow(noConcepts,windowSize);
        assertThrows(RuntimeException.class, () -> questionChooserThrows.checkAllChooserConceptIdsAreInDomain(domainDatasource));


    }

    @Test
    public void hasUnpreparedFollowUpQuestionsTest() throws IOException{
        
        DomainDatasource domainDatasource = new DomainDatasourceJson("example","src/test/resources/rewrite/QuestionChooserSampleQuestionsFollowUp.json");
        QuestionChooserChangeConceptByWindow questionChooser = new QuestionChooserChangeConceptByWindow(domainDatasource.retrieveAllConcepts(),windowSize);
        
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
        assertEquals(OrderedConceptRubric.DEVELOPING,QuestionChooserChangeConceptByWindow.findScoreByConcept(conceptScores1, "sky"));
        
        // last concept
        assertEquals(OrderedConceptRubric.UNPREPARED,QuestionChooserChangeConceptByWindow.findScoreByConcept(conceptScores1, "google"));

        // duplicate concept
        conceptScores1.add(new ConceptRubricPair("google",OrderedConceptRubric.UNPREPARED));
        assertThrows(RuntimeException.class, () -> QuestionChooserChangeConceptByWindow.findScoreByConcept(conceptScores1, "google"));

        // concept not in conceptScores
        assertThrows(RuntimeException.class, () -> QuestionChooserChangeConceptByWindow.findScoreByConcept(conceptScores1, "notFound"));


    }
}
