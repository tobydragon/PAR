package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
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

}
