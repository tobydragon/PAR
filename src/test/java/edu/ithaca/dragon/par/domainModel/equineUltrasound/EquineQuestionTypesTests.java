package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.io.JsonStudentModelDatastore;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EquineQuestionTypesTests {


    @Test
    public void isChildTypeTest(){
        //should pass, child type
        String attachment = "attachment";
        assertEquals(true, EquineQuestionTypes.isChildQuestionType(attachment));

        //should fail, not child type
        String wrong = "structure";
        assertEquals(false, EquineQuestionTypes.isChildQuestionType(wrong));

        //should fail, not any type
        String notType = "banana tree";
        assertEquals(false, EquineQuestionTypes.isChildQuestionType(notType));
    }

    @Test
    public void getParentQuestionCountswithChildren() throws IOException {
        //param: List<QuestionCount>> questionTypesListMap
        StudentModelDatastore studentModelDatastore = new JsonStudentModelDatastore("src/test/resources/author/questionPools/testFullQP.json", "src/test/resources/author/students");
        StudentModel studentModel = studentModelDatastore.getStudentModel("followupTestStudent");

        Map<String,List<QuestionCount>> questionTypesListMap = studentModel.questionCountsByTypeMap();

        //children, should pass
        List<QuestionCount> typeQuestionsStructure = questionTypesListMap.get("structure");
        List<QuestionCount> parents = EquineQuestionTypes.getParentQuestionCountsWithChildren(typeQuestionsStructure);
        assertEquals(15, parents.size());

        //no children, should fail
        List<QuestionCount> typeQuestionsPlane = questionTypesListMap.get("plane");
        List<QuestionCount> notParents = EquineQuestionTypes.getParentQuestionCountsWithChildren(typeQuestionsPlane);
        assertEquals(0, notParents.size());

        //not real, should fail
        List<QuestionCount> typeQuestionsBananaTree = questionTypesListMap.get("banana tree");
        List<QuestionCount> bananas = EquineQuestionTypes.getParentQuestionCountsWithChildren(typeQuestionsPlane);
        assertEquals(0, bananas.size());


    }

}
