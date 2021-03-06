package edu.ithaca.dragon.par.domainModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.JsonQuestionPoolDatastore;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    public static boolean checkIfListsHaveSameQuestionObjects(List<Question> list1, List<Question> list2){
        if(list1.size() != list2.size()){
            return false;
        }
        for(int i = 0; i < list1.size(); i++){
            if(list1.get(i) != list2.get(i))
                return false;
        }
        //lists are the same size and have the same content
        return true;
    }

    @Test
    public void checkIfListsHaveSameQuestionObjectsTest() throws IOException{
        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());

        //The Lists are exactly the same
        List<Question> list1a = qp.getAllQuestions();
        List<Question> list1b = qp.getAllQuestions();
        assertEquals(true, checkIfListsHaveSameQuestionObjects(list1a,list1b));

        //the Lists are different size
        QuestionPool qp2 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        List<Question> list1c = qp2.getAllQuestions();
        assertEquals(false, checkIfListsHaveSameQuestionObjects(list1a,list1c));

        //The content of the Questions are the same, but they are different objects
        QuestionPool qp3 = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        List<Question> list1d = qp3.getAllQuestions();
        assertEquals(false, checkIfListsHaveSameQuestionObjects(list1a, list1d));
    }

    @Test
    public void toJsonAndBackTest() throws IOException {
        Question planeQ = new Question("PlaneQ1", "On which plane is the ultrasound taken?", EquineQuestionTypes.plane.toString(),
                "Lateral", Arrays.asList("Transverse", "Lateral"), "../static/images/equine02.jpg");
        Question structureQ = new Question("StructureQ1", "What structure is in the near field?", EquineQuestionTypes.structure.toString(),
                "bone", Arrays.asList("bone", "ligament", "tumor", "tendon"), "../static/images/equine02.jpg");
        Question zoneQ = new Question("ZoneQ1", "In what zone is this ultrasound taken?", EquineQuestionTypes.zone.toString(),
                "3c", Arrays.asList("1a", "1b", "2a", "2b", "2c", "3a", "3b", "3c"), "../static/images/equine02.jpg");
        List<Question> questionsToFile = Arrays.asList(planeQ, structureQ, zoneQ);

        JsonUtil.toJsonFile("src/test/resources/autoGenerated/QuestionTest-toJsonAndBackTest.json", questionsToFile);
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/autoGenerated/QuestionTest-toJsonAndBackTest.json", Question.class);

        assertEquals(questionsToFile.size(), questionsFromFile.size());
        for (int i=0; i< questionsFromFile.size(); i++){
            assertEquals(questionsToFile.get(i), questionsFromFile.get(i));
        }
        Path path = Paths.get("src/test/resources/autoGenerated/QuestionTest-toJsonAndBackTest.json");
        assertTrue(Files.deleteIfExists(path));
    }

    @Test
    public void equalsTest(){
        Question planeQ = new Question("PlaneQ1", "On which plane is the ultrasound taken?", EquineQuestionTypes.plane.toString(),
                "Lateral", Arrays.asList("Transverse", "Lateral"), "../static/images/equine02.jpg");
        assertEquals(planeQ, planeQ);
        assertNotEquals(planeQ, "Hello");

        Question planeQsame = new Question("PlaneQ1", "On which plane is the ultrasound taken?", EquineQuestionTypes.plane.toString(),
                "Lateral", Arrays.asList("Transverse", "Lateral"), "../static/images/equine02.jpg");
        assertEquals(planeQ, planeQsame);

        Question planeQdiffAns = new Question("PlaneQ1", "On which plane is the ultrasound taken?", EquineQuestionTypes.plane.toString(),
                "Lateral", Arrays.asList("Transverse", "Lateral", "Other Answer"), "../static/images/equine02.jpg");
        assertNotEquals(planeQ, planeQdiffAns);

        Question customQ = new Question("cust1", null, EquineQuestionTypes.structure.toString(), null, Arrays.asList("Yes", "No"), "..customURL");
        Question customQ2 = new Question("cust1", null, EquineQuestionTypes.structure.toString(), null, Arrays.asList("Yes", "No"), "..customURL");
        Question customQWithQText = new Question("cust1", "Is this a custom Question?", EquineQuestionTypes.structure.toString(), "Yes", Arrays.asList("Yes", "No"), "..customURL");
        assertEquals(customQ, customQ2);
        assertNotEquals(customQ, customQWithQText);
    }

    @Test
    public void toAndFromJsonTestWithFollowUpQuestions() throws IOException {
        //Set up a list of Questions
        //StructureQ1 has 3 followupCounts questions
        //one of those questions has a follow up
        //PlaneQ1 does not have any follow up questions
        Question bonusQuestion = new Question("BonusQ1", "This is a followupCounts to a followupCounts question!", "Plane", "Nice!", Arrays.asList("Nice!", "Sweet!", "Cool!"), "../static/images/equine02.jpg");
        Question attach1 = new Question("AttachQ1", "Which attachment is this?", "Attachment",
                "Type1", Arrays.asList("Type1", "Type2", "Type3"), "../static/images/equine02.jpg");
        Question attach2 = new Question("AttachQ2", "Which attachment is this?", "Attachment",
                "Type3", Arrays.asList("Type1", "Type2", "Type3"), "../static/images/equine02.jpg");
        Question attach3 = new Question("AttachQ1", "Which attachment is this?", "Attachment",
                "Type2", Arrays.asList("Type1", "Type2", "Type3"), "../static/images/equine02.jpg", Arrays.asList(bonusQuestion));
        List<Question> attachmentQuestions = Arrays.asList(attach1, attach2, attach3);
        Question myQ = new Question("StructureQ1", "What kind of structure is this?", EquineQuestionTypes.structure.toString(), "Structure1", Arrays.asList("Structure1", "Structure2", "Structure3"), "../static/images/equine02.jpg", attachmentQuestions);
        Question myQ2 = new Question("PlaneQ1", "What plane is this?", EquineQuestionTypes.plane.toString(), "Transverse", Arrays.asList("Transverse", "Longitudinal"), "../static/images/equine02.jpg");
        List<Question> hardcodedQuestions = Arrays.asList(myQ, myQ2);

        //Write to file
        JsonUtil.toJsonFile("src/test/resources/autoGenerated/questionsWithFollowup.json", hardcodedQuestions);

        //Load back in
        List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/autoGenerated/questionsWithFollowup.json", Question.class);

        assertEquals(2, questionsFromFile.size());
        Question questionWithFollowups = questionsFromFile.get(0);
        assertEquals(3, questionWithFollowups.getFollowupQuestions().size());
        Question questionWithoutFollowups = questionsFromFile.get(1);
        assertEquals(0, questionWithoutFollowups.getFollowupQuestions().size());

    }

    @AfterEach
    public void cleanUp() throws IOException{
        Path path1 = Paths.get("src/test/resources/autoGenerated/questionsWithFollowup.json");
        Files.deleteIfExists(path1);
    }

    @Test
    public void takeOldQuestionPoolAndAddInFollowUpQuestion() throws IOException{
        //This test was written when the followUpQuestion property of the Question class was added
        //The purpose of this test is to check if old data file can be used with this change

        QuestionPool qp = new QuestionPool(new JsonQuestionPoolDatastore("src/test/resources/author/questionPools/SampleQuestionPool.json").getAllQuestions());
        JsonUtil.toJsonFile("src/test/resources/autoGenerated/SampleQuestionPoolToJson.json", qp);

        Path path1 = Paths.get("src/test/resources/autoGenerated/SampleQuestionPoolToJson.json");
        Files.deleteIfExists(path1);

    }
}
