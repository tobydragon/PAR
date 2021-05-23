package edu.ithaca.dragon.par.student.json;

import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelDatasourceJsonTest {

    @Test
    public void findQuestionSeenLeastRecentlyTest(){
        fail("todo");
    }

    @Test
    public void toAndFromJson(@TempDir Path tempDir) throws IOException {
        makeExampleInDir(tempDir.toString());
        assertEquals(10, FileSystemUtil.findAllFileNamesInDir(tempDir.toFile(), "json").size());
        StudentModel bart =  new JsonIoUtil(new JsonIoHelperDefault()).fromFile(tempDir.toString() + "/Bart.json", StudentModel.class);
        assertEquals(2, bart.questionHistories.size());
        assertEquals(1, bart.questionHistories.get("q3").timesSeen.size());
    }

    public static void makeExampleInDir(String directoryName){
        StudentModelDatasource example = new StudentModelDatasourceJson("example", directoryName.toString(), new JsonIoHelperDefault());
        example.createNewModelForId("Homer");
        example.addTimeSeen("Homer", "q1");
        example.addResponse("Homer", "q1", "I don't know");
        example.addTimeSeen("Homer", "q2");
        example.addResponse("Homer", "q2", "donuts");

        example.createNewModelForId("Bart");
        example.addTimeSeen("Bart", "q1");
        example.addResponse("Bart", "q1", "I don't know");
        example.addTimeSeen("Bart", "q3");
        example.addResponse("Bart", "q3", "skateboard");

        example.createNewModelForId("Lisa");
        example.addTimeSeen("Lisa", "q1");
        example.addResponse("Lisa", "q1", "1000");
        example.addTimeSeen("Lisa", "q2");
        example.addResponse("Lisa", "q2", "water");
        example.addTimeSeen("Lisa", "q3");
        example.addResponse("Lisa", "q3", "being well rested");

        List.of("r1", "r2", "r3", "o1", "o2", "o3", "o4").forEach(example::createNewModelForId);
    }

    public static void main(String[] args) throws IOException {
        String directory = "src/test/resources/autoGenerated/student";
        Files.createDirectories(Paths.get(directory));
        makeExampleInDir(directory);
    }

}