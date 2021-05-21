package edu.ithaca.dragon.util;

import edu.ithaca.dragon.par.deprecated.io.ImageTaskResponseOOP;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonIoUtilTest {

    //using ImageTask because it is a simple object, could be anything

    @Test
    public void toJsonAndFromResourceTest(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("SampleResponse.json");
        ImageTaskResponseOOP responseOut = new ImageTaskResponseOOP("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        new JsonIoUtil(new JsonIoHelperDefault()).toFile(tempFile.toString(), responseOut);
        ImageTaskResponseOOP responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFile(tempFile.toString(), ImageTaskResponseOOP.class);
        assertEquals(responseOut, responseIn);
    }

    @Test
    public void fromFileSystemOrDefaultToClassPathJsonTest(@TempDir Path tempDir) throws IOException{
        Path aCurrentResponsePath = tempDir.resolve("aCurrentResponse.json");
        ImageTaskResponseOOP aCurrentResponse = new ImageTaskResponseOOP("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        new JsonIoUtil(new JsonIoHelperDefault()).toFile(aCurrentResponsePath.toString(), aCurrentResponse);

        String aDefaultResponsePath = "src/test/resources/author/responses/SampleResponse.json";
        ImageTaskResponseOOP aDefaultResponse = new JsonIoUtil(new JsonIoHelperDefault()).fromReadOnlyFile(aDefaultResponsePath, ImageTaskResponseOOP.class);

        Path aMissingCurrentResponsePath = tempDir.resolve("aMissingResponse.json");

        //test that it gets one that already exists
        ImageTaskResponseOOP responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile(aCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponseOOP.class);
        assertEquals(aCurrentResponse, responseIn);

        //check that it returns the default if it doesn't exist
        responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile(aMissingCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponseOOP.class);
        assertEquals(aDefaultResponse, responseIn);
        //make sure it actually wrote a copy of the default to the missing location
        responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFile(aMissingCurrentResponsePath.toString(), ImageTaskResponseOOP.class);
        assertEquals(aDefaultResponse, responseIn);

        //make sure it would throw an exception if both paths are bad
        assertThrows(IOException.class, () -> new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile("bad file path", "bad default path", ImageTaskResponseOOP.class) );
    }
}
