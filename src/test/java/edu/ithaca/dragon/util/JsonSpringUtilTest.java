package edu.ithaca.dragon.util;

import edu.ithaca.dragon.par.io.ImageTaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JsonSpringUtilTest {

    //using ImageTask because it is a simple object, could be anything

    @Test
    public void toJsonAndFromResourceTest(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("SampleResponse.json");
        ImageTaskResponse responseOut = new ImageTaskResponse("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        JsonSpringUtil.toFileSystemJson(tempFile.toString(), responseOut);
        ImageTaskResponse responseIn = JsonSpringUtil.fromFileSystemJson(tempFile.toString(), ImageTaskResponse.class);
        assertEquals(responseOut, responseIn);
    }

    @Test
    public void fromFileSystemOrDefaultToClassPathJsonTest(@TempDir Path tempDir) throws IOException{
        Path aCurrentResponsePath = tempDir.resolve("aCurrentResponse.json");
        ImageTaskResponse aCurrentResponse = new ImageTaskResponse("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        JsonSpringUtil.toFileSystemJson(aCurrentResponsePath.toString(), aCurrentResponse);

        String aDefaultResponsePath = "author/SampleResponse.json";
        ImageTaskResponse aDefaultResponse = JsonSpringUtil.fromClassPathJson(aDefaultResponsePath, ImageTaskResponse.class);

        Path aMissingCurrentResponsePath = tempDir.resolve("aMissingResponse.json");

        //test that it gets one that already exists
        ImageTaskResponse responseIn = JsonSpringUtil.fromFileSystemOrCopyFromDefaultClassPathJson(aCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponse.class);
        assertEquals(aCurrentResponse, responseIn);

        //check that it returns the default if it doesn't exist
        responseIn = JsonSpringUtil.fromFileSystemOrCopyFromDefaultClassPathJson(aMissingCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponse.class);
        assertEquals(aDefaultResponse, responseIn);
        //make sure it actually wrote a copy of the default to the missing location
        responseIn = JsonSpringUtil.fromFileSystemJson(aMissingCurrentResponsePath.toString(), ImageTaskResponse.class);
        assertEquals(aDefaultResponse, responseIn);

        //make sure it would throw an exception if both paths are bad
        assertThrows(IOException.class, () -> JsonSpringUtil.fromFileSystemOrCopyFromDefaultClassPathJson("bad file path", "bad default path", ImageTaskResponse.class) );
    }



}