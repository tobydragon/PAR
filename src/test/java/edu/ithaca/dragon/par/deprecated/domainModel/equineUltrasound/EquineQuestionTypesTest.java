package edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        //8
        assertEquals(Arrays.asList(EquineQuestionTypes.zone.toString()), EquineQuestionTypes.getTypesForLevel(8));

        //too high
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(17));

        //too high, border
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(9));

        //too low
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(-4));

        //too low, border
        assertThrows(IllegalArgumentException.class, ()-> EquineQuestionTypes.getTypesForLevel(0));
    }
}
