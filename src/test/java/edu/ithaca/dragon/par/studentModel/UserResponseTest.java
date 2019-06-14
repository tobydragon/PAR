package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

public class UserResponseTest {

    @Test
    public void checkResponseTest(){
        Question q1 = new Question("Test Q1", "What is your name?", 1, "King Arthur", Arrays.asList("Bob","Cindy","Damon","King Arthur"));
        UserResponse response=new UserResponse("kandace",q1,"hi");
        assertEquals(response.checkResponse(),false);
    }

}
