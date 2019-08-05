package edu.ithaca.dragon.util;

import edu.ithaca.dragon.par.io.ImageTaskResponseImp1;
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
        ImageTaskResponseImp1 responseOut = new ImageTaskResponseImp1("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        new JsonIoUtil(new JsonIoHelperDefault()).toFile(tempFile.toString(), responseOut);
        ImageTaskResponseImp1 responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFile(tempFile.toString(), ImageTaskResponseImp1.class);
        assertEquals(responseOut, responseIn);
    }

    @Test
    public void fromFileSystemOrDefaultToClassPathJsonTest(@TempDir Path tempDir) throws IOException{
        Path aCurrentResponsePath = tempDir.resolve("aCurrentResponse.json");
        ImageTaskResponseImp1 aCurrentResponse = new ImageTaskResponseImp1("resp1", Arrays.asList("id1", "id2"), Arrays.asList("ans1", "ans2"));
        new JsonIoUtil(new JsonIoHelperDefault()).toFile(aCurrentResponsePath.toString(), aCurrentResponse);

        String aDefaultResponsePath = "src/test/resources/author/SampleResponse.json";
        ImageTaskResponseImp1 aDefaultResponse = new JsonIoUtil(new JsonIoHelperDefault()).fromReadOnlyFile(aDefaultResponsePath, ImageTaskResponseImp1.class);

        Path aMissingCurrentResponsePath = tempDir.resolve("aMissingResponse.json");

        //test that it gets one that already exists
        ImageTaskResponseImp1 responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile(aCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponseImp1.class);
        assertEquals(aCurrentResponse, responseIn);

        //check that it returns the default if it doesn't exist
        responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile(aMissingCurrentResponsePath.toString(), aDefaultResponsePath, ImageTaskResponseImp1.class);
        assertEquals(aDefaultResponse, responseIn);
        //make sure it actually wrote a copy of the default to the missing location
        responseIn = new JsonIoUtil(new JsonIoHelperDefault()).fromFile(aMissingCurrentResponsePath.toString(), ImageTaskResponseImp1.class);
        assertEquals(aDefaultResponse, responseIn);

        //make sure it would throw an exception if both paths are bad
        assertThrows(IOException.class, () -> new JsonIoUtil(new JsonIoHelperDefault()).fromFileOrCopyFromReadOnlyFile("bad file path", "bad default path", ImageTaskResponseImp1.class) );
    }
}
