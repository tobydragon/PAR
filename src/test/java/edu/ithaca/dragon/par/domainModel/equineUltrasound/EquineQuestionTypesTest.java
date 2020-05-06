package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EquineQuestionTypesTest {
    @Test
    public void getTypesForLevelTest(){ //will start failing once other branches are merged
        //1
        assertEquals(Arrays.asList(EquineQuestionTypes.plane.toString()), EquineQuestionTypes.getTypesForLevel(1));

        //2
        assertEquals(Arrays.asList(EquineQuestionTypes.plane.toString(), EquineQuestionTypes.structure.toString()), EquineQuestionTypes.getTypesForLevel(2));

        //3
        assertEquals(Arrays.asList(EquineQuestionTypes.structure.toString()), EquineQuestionTypes.getTypesForLevel(3));

        //4
        assertEquals(Arrays.asList(EquineQuestionTypes.structure.toString(),EquineQuestionTypes.attachment.toString()), EquineQuestionTypes.getTypesForLevel(4));

        //5
        assertEquals(Arrays.asList(EquineQuestionTypes.attachment.toString()), EquineQuestionTypes.getTypesForLevel(5));

        //6
        assertEquals(Arrays.asList(EquineQuestionTypes.attachment.toString(),EquineQuestionTypes.zone.toString()), EquineQuestionTypes.getTypesForLevel(6));

        //7
        assertEquals(Arrays.asList(EquineQuestionTypes.zone.toString()), EquineQuestionTypes.getTypesForLevel(7));

        //too high
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(17));

        //too high, border
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(8));

        //too low
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(-4));

        //too low, border
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(0));
    }
}
