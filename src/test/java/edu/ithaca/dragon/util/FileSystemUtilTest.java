package edu.ithaca.dragon.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemUtilTest {

    @Test
    void findAllFileNamesAndAddPathsToNamesInDirTest() throws IOException {
        List<String> names = FileSystemUtil.findAllFileNamesInDir("src/test/resources/author/students", "json");
        assertEquals(6,names.size());
        List<String> paths = FileSystemUtil.addPathToFilenames("src/test/resources/author/students", names);
        assertEquals(6,paths.size());

        names = FileSystemUtil.findAllFileNamesInDir("src/test/resources/images", "jpg");
        assertEquals(10,names.size());
        paths = FileSystemUtil.addPathToFilenames("./images", names);
        assertEquals(10,paths.size());
//        assertEquals("./images/demoEquine14.jpg", paths.get(0));
    }
}