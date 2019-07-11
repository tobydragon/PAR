package edu.ithaca.dragon.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSystemUtil {

    public static List<String> addPathToFilenames(String dirPath, List<String> fileNames) throws IOException{
        List<String> filePaths = new ArrayList<>();
        for (String fileName : fileNames){
            filePaths.add(dirPath+"/"+fileName);
        }
        return filePaths;
    }

    public static List<String> findAllFileNamesInDir(String dirPath, String fileExtension) throws IOException{
        return findAllFileNamesInDir(new File(dirPath), fileExtension);
    }

    public static List<String> findAllFileNamesInDir(File directory, String fileExtension) throws IOException{
        //check if studentModelFilePath exists
        if(!directory.exists()) {
            throw new IOException("path " + directory.toString() + " cannot be found");
        }
        File[] allFiles = directory.listFiles();

        List<String> filenames = new ArrayList<>();
        for(File file : allFiles){
            if(file.isFile() && file.getName().endsWith(fileExtension)){
                filenames.add( file.getName());
            }
        }
        return filenames;
    }


}
