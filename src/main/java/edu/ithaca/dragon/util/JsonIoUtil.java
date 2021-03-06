package edu.ithaca.dragon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonIoUtil {

    private JsonIoHelper jsonIoHelper;

    public JsonIoUtil(JsonIoHelper jsonIoHelper){
        this.jsonIoHelper = jsonIoHelper;
    }

    public String toString(Object objectToSerialize) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString( objectToSerialize);
    }

    public void toFile(String filename, Object objectToSerialize) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File fileTowWrite = jsonIoHelper.getReadAndWriteFile(filename);
        mapper.writeValue(fileTowWrite, objectToSerialize);
    }

    public  <T> T fromFile(String filename, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(jsonIoHelper.getReadAndWriteFile(filename), classToBeCreated);
    }

    public  <T> T fromReadOnlyFile(String filename, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(jsonIoHelper.getInputStream(filename), classToBeCreated);
    }

    public <T> List<T> listFromFile(String filename, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //found this fix here: https://stackoverflow.com/questions/11659844/jackson-deserialize-generic-class-variable
        return  mapper.readValue(jsonIoHelper.getReadAndWriteFile(filename), mapper.getTypeFactory().constructParametricType(List.class, classToBeCreated));
    }

    public <T> List<T> listfromReadOnlyFile(String filename, Class<? extends T> classToBeCreated) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //found this fix here: https://stackoverflow.com/questions/11659844/jackson-deserialize-generic-class-variable
        return  mapper.readValue(jsonIoHelper.getInputStream(filename), mapper.getTypeFactory().constructParametricType(List.class, classToBeCreated));
    }

    public  <T> T fromFileOrCopyFromReadOnlyFile(String filePath, String inputStreamPath, Class<? extends T> classToBeCreated) throws IOException {
        try {
            return fromFile(filePath, classToBeCreated);
        }
        catch (Exception e){
            T defaultObject = fromReadOnlyFile(inputStreamPath, classToBeCreated);
            toFile(filePath, defaultObject);
            return fromFile(filePath, classToBeCreated);
        }
    }

    public  <T> List<T> listFromFileOrCopyFromReadOnlyFile(String filePath, String inputStreamPath, Class<? extends T> classToBeCreated) throws IOException {
        try {
            return listFromFile(filePath, classToBeCreated);
        }
        catch (Exception e){
            List<T> defaultObject = listfromReadOnlyFile(inputStreamPath, classToBeCreated);
            toFile(filePath, defaultObject);
            return listFromFile(filePath, classToBeCreated);
        }
    }
}
