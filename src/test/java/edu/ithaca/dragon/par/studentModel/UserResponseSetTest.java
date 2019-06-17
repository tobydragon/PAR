package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;


public class UserResponseSetTest {

    @Test
    public void getResponseSizeTest() {
        Question q1 = new Question("Test Q1", "What is your name?", 1, "King Arthur",
                Arrays.asList("Bob","Cindy","Joey","King Arthur"), "../static/images/equine02.jpg");
        UserResponse response1 = new UserResponse("kandace", q1,"hey");
        UserResponseSet respSet=new UserResponseSet("kandace");
        respSet.addResponse(response1);


        Question q2 = new Question("Test Q2", "What is your quest?", 3, "To seek the Holy Grail",
                Arrays.asList("To Graduate","To make an ITS","To seek the Holy Grail"), "../static/images/equine02.jpg");
        UserResponse response2 = new UserResponse("kandace", q2,"hey");
        respSet.addResponse(response2);


        Question q3 = new Question("Test Q3","What is the airspeed velocity of an unladen swallow?", 10, "African or European?",
                Arrays.asList("10 m/s", "20 m/s", "30 m/s","African or European?"), "../static/images/equine02.jpg");
        UserResponse response3 = new UserResponse("kandace", q3,"hey");
        respSet.addResponse(response3);
        assertEquals(respSet.getUserResponsesSize(),3);
    }


    public static double OK_DOUBLE_MARGIN = (double) 0.00001;
    @Test
    public void getScoreTest(){

        Question q1 = new Question("Test Q1", "What is your name?", 1, "King Arthur",
                Arrays.asList("Bob","Cindy","Joey","King Arthur"), "../static/images/equine02.jpg");
        UserResponse response1 = new UserResponse("kandace", q1,"King Arthur");
        UserResponseSet respSet=new UserResponseSet("kandace");
        respSet.addResponse(response1);


        Question q2 = new Question("Test Q2", "What is your quest?", 3, "To seek the Holy Grail",
                Arrays.asList("To Graduate","To make an ITS","To seek the Holy Grail"),"../static/images/equine02.jpg");
        UserResponse response2 = new UserResponse("kandace", q2,"To seek the Holy Grail");
        respSet.addResponse(response2);


        Question q3 = new Question("Test Q3","What is the airspeed velocity of an unladen swallow?", 10, "African or European?",
                Arrays.asList("10 m/s", "20 m/s", "30 m/s","African or European?"), "../static/images/equine02.jpg");
        UserResponse response3 = new UserResponse("kandace", q3,"African r European?");
        respSet.addResponse(response3);

        assertEquals(66.6666666,respSet.CalcScore(),OK_DOUBLE_MARGIN);
    }
}
