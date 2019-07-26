package edu.ithaca.dragon.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsonIoHelperSpring implements JsonIoHelper {

    @Override
    public File getReadAndWriteFile(String filename) {
        return new FileSystemResource(filename).getFile();
    }

    @Override
    public InputStream getInputStream (String filename) throws IOException {
        return new ClassPathResource(filename).getInputStream();
    }
}
