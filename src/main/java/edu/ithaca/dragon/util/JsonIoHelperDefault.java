package edu.ithaca.dragon.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class JsonIoHelperDefault implements JsonIoHelper {

    @Override
    public File getReadAndWriteFile(String filename) {
        return new File(filename);
    }

    @Override
    public InputStream getInputStream(String filename) throws FileNotFoundException {
        return new FileInputStream(new File(filename));
    }
}
