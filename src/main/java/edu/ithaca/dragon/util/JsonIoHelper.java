package edu.ithaca.dragon.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface JsonIoHelper {
    File getReadAndWriteFile(String filename);
    InputStream getInputStream(String filename) throws IOException;
}
