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

        //first concept exemplary, with one q unanswered test
        StudentModelJson studentModelFirstConceptCompetent = studentModelDatasource.getStudentModel("firstConceptCompetentStudent");
        assertEquals(numMathQs,questionChooser.chooseQuestions(studentModelFirstConceptCompetent,domainDatasource).size());


        //first concept exemplary test
        StudentModelJson studentModelFirstConceptExemplary = studentModelDatasource.getStudentModel("firstConceptExemplaryStudent");
        assertEquals(numMathQs,questionChooser.chooseQuestions( studentModelFirstConceptExemplary,domainDatasource).size());


        //all concepts exemplary all w/ same time seen (makes all concepts developing)
        StudentModelJson studentModelAllConceptsExemplary = studentModelDatasource.getStudentModel("AllConceptsExemplary");
        assertEquals(1,questionChooser.chooseQuestions(studentModelAllConceptsExemplary,domainDatasource).size());

    }

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
        QuestionChooserByOrderedConcepts questionChooser1 = new QuestionChooserByOrderedConcepts(domainDatasource.retrieveAllConcepts(),windowSize);
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
}
