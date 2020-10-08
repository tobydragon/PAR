package edu.ithaca.dragon.par.pedagogicalModel;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LevelMessageInformationTest {


    @Test
    public void getAndSetPreviousLevelTest() throws IOException {
        LevelMessageInformation levMes = new LevelMessageInformation(1, 4);
        assertEquals(1, levMes.getPreviousLevel());

        //set once
        levMes.setPreviousLevel(5);
        assertEquals(5, levMes.getPreviousLevel());

        //invalid set- too high
        assertThrows(IllegalArgumentException.class, ()-> levMes.setPreviousLevel(19));
        assertEquals(5, levMes.getPreviousLevel());

        //invalid set- too high (border)
        assertThrows(IllegalArgumentException.class, ()-> levMes.setPreviousLevel(8));
        assertEquals(5, levMes.getPreviousLevel());

        //invalid set- too low
        assertThrows(IllegalArgumentException.class, ()-> levMes.setPreviousLevel(-3));
        assertEquals(5, levMes.getPreviousLevel());

        //invalid set- too low (border)
        assertThrows(IllegalArgumentException.class, ()-> levMes.setPreviousLevel(0));
        assertEquals(5, levMes.getPreviousLevel());

        //set again
        levMes.setPreviousLevel(6);
        assertEquals(6, levMes.getPreviousLevel());

        //new studentModel
        LevelMessageInformation levMes2 = new LevelMessageInformation();
        levMes2.setPreviousLevel(3);
        assertEquals(3, levMes2.getPreviousLevel());
        assertEquals(6, levMes.getPreviousLevel());

        //set old one again
        levMes.setPreviousLevel(1);
        assertEquals(3, levMes2.getPreviousLevel());
        assertEquals(1, levMes.getPreviousLevel());
    }

    @Test
    public void getAndSetCurrentLevelTest() throws IOException{
        LevelMessageInformation levmes = new LevelMessageInformation(2, 3);
        assertEquals(3, levmes.getCurrentLevel());

        //set once
        levmes.setCurrentLevel(5);
        assertEquals(5, levmes.getCurrentLevel());

        //invalid set- too high
        assertThrows(IllegalArgumentException.class, ()-> levmes.setCurrentLevel(19));
        assertEquals(5, levmes.getCurrentLevel());

        //invalid set- too high (border)
        assertThrows(IllegalArgumentException.class, ()-> levmes.setCurrentLevel(8));
        assertEquals(5, levmes.getCurrentLevel());

        //invalid set- too low
        assertThrows(IllegalArgumentException.class, ()-> levmes.setCurrentLevel(-3));
        assertEquals(5, levmes.getCurrentLevel());

        //invalid set- too low (border)
        assertThrows(IllegalArgumentException.class, ()-> levmes.setCurrentLevel(0));
        assertEquals(5, levmes.getCurrentLevel());

        //set again
        levmes.setCurrentLevel(6);
        assertEquals(6, levmes.getCurrentLevel());

        //new studentModel
        LevelMessageInformation levmes2 = new LevelMessageInformation();
        levmes2.setCurrentLevel(3);
        assertEquals(3, levmes2.getCurrentLevel());
        assertEquals(6, levmes.getCurrentLevel());

        //set old one again
        levmes.setCurrentLevel(1);
        assertEquals(3, levmes2.getCurrentLevel());
        assertEquals(1, levmes.getCurrentLevel());
    }
}
