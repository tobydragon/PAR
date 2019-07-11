package edu.ithaca.dragon.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonSpringUtil {

    public static void toFileSystemJson(String relativePath, Object objectToSerialize) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new FileSystemResource(relativePath).getFile();
        if (!file.exists()){
            file.createNewFile();
        }
        mapper.writeValue(file, objectToSerialize);
    }



    public static <T> T fromFileSystemJson(String relativePath, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(new FileSystemResource(relativePath).getFile(), classToBeCreated);
    }

    // loads from the resources folder, rather than user.dir
    public static <T> T fromClassPathJson(String relativePath, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(new ClassPathResource(relativePath).getInputStream(), classToBeCreated);
    }

    public static <T> T fromFileSystemOrCopyFromDefaultClassPathJson(String fileSystemLocation, String classPathLocation, Class<? extends T> classToBeCreated) throws IOException{
        try {
            return fromFileSystemJson(fileSystemLocation, classToBeCreated);
        }
        catch (Exception e){
            T defaultObject = fromClassPathJson(classPathLocation, classToBeCreated);
            toFileSystemJson(fileSystemLocation, defaultObject);
            return fromFileSystemJson(fileSystemLocation, classToBeCreated);
        }
    }


    public static <T> List<T> listFromFileSystemJson(String relativePath, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //found this fix here: https://stackoverflow.com/questions/11659844/jackson-deserialize-generic-class-variable
        return  mapper.readValue(new FileSystemResource(relativePath).getFile(), mapper.getTypeFactory().constructParametricType(List.class, classToBeCreated));
    }

    public static <T> List<T> listFromClassPathJson(String relativePath, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //found this fix here: https://stackoverflow.com/questions/11659844/jackson-deserialize-generic-class-variable
        return  mapper.readValue(new ClassPathResource(relativePath).getInputStream(), mapper.getTypeFactory().constructParametricType(List.class, classToBeCreated));
    }

    public static <T> List<T> listFromFileSystemOrCopyFromDefaultClassPathJson(String fileSystemLocation, String classPathLocation, Class<? extends T> classToBeCreated) throws IOException{
        try {
            return listFromFileSystemJson(fileSystemLocation, classToBeCreated);
        }
        catch (Exception e){
            List<T> defaultObject = listFromClassPathJson(classPathLocation, classToBeCreated);
            toFileSystemJson(fileSystemLocation, defaultObject);
            return listFromFileSystemJson(fileSystemLocation, classToBeCreated);
        }
    }
}
