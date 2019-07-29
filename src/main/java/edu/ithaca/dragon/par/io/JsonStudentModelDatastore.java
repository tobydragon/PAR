package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.UserResponseSet;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonStudentModelDatastore extends JsonQuestionPoolDatastore implements StudentModelDatastore {

    private String studentModelFilePath;
    private Map<String, StudentModel> studentModelMap;
    JsonIoHelper jsonIoHelper;
    JsonIoUtil jsonIoUtil;

    public JsonStudentModelDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        this(questionFilePath, null, new JsonIoHelperDefault(), studentModelFilePath);
    }

    public JsonStudentModelDatastore(String questionFilePath, String defaultQuestionReadOnlyFilePath, JsonIoHelper jsonIoHelper, String studentModelFilePath) throws IOException {
        super(questionFilePath, defaultQuestionReadOnlyFilePath, jsonIoHelper);
        this.studentModelFilePath = studentModelFilePath;
        this.jsonIoHelper = jsonIoHelper;
        this.jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        studentModelMap = new HashMap<>();

        //TODO: set best possible window size
        if (isWindowSizeTooBig(UserResponseSet.windowSize, questionPool.getAllQuestions())) {
            throw new RuntimeException("The windowSize is too small for the given questionFile");
        }
    }

    private static StudentModel loadStudentModelFromFile(QuestionPool questionPool, String studentModelFilePath, String userId, JsonIoHelper jsonIoHelper, JsonIoUtil jsonIoUtil) throws IOException{
        String fullFileName = studentModelFilePath + "/" + userId + ".json";

        //check if file exists, return null if it doesn't
        File checkFile = jsonIoHelper.getReadAndWriteFile(fullFileName);
        if(!checkFile.exists()){
            return null;
        }

        StudentModelRecord SMR = jsonIoUtil.fromFile(fullFileName, StudentModelRecord.class);
        return SMR.buildStudentModel(questionPool);
    }

    @Override
    public StudentModel getStudentModel(String userId) throws IOException {
        return getOrCreateStudentModel(userId);
    }

    @Override
    public void imageTaskResponseSubmitted(String userId, ImageTaskResponse imageTaskResponse) throws IOException{
        StudentModel currentStudent = getStudentModel(userId);
        currentStudent.imageTaskResponseSubmitted(imageTaskResponse, questionPool);
        overwriteStudentFile(currentStudent, studentModelFilePath, jsonIoUtil);
    }

    @Override
    public void addQuestions(List<Question> questions) throws IOException {
        super.addQuestions(questions);
        //TODO: recalculate best possible window size
        Set<String> studentIds = studentModelMap.keySet();
        studentIds.addAll(getAllSavedStudentIds(studentModelFilePath));
        for (String studentId : studentIds){
            StudentModel currModel = getOrCreateStudentModel(studentId);
            for (Question question : questions){
                currModel.addQuestion(question);
            }
            //TODO: set studentModel's window size
            overwriteStudentFile(currModel, studentModelFilePath, jsonIoUtil);
        }
    }

    public static List<String> getAllSavedStudentIds(String studentModelFilePath) throws IOException{
        List<String> files = FileSystemUtil.findAllFileNamesInDir(studentModelFilePath, "json");
        for (int i = 0; i < files.size(); i++){
            files.set(i, files.get(i).replace(".json", ""));
        }
        return files;
    }

    public void logout(String userId) throws IOException{
        studentModelMap.remove(userId);
    }

    /**
     * @param userId
     * @return a StudentModel corresponding to the given userId that is also in the studentModelMap
     * @post if there was no corresponding StudentModel, a new on will be created and added to the map
     */
    public StudentModel getOrCreateStudentModel(String userId) throws IOException {
        //try to find the student in the map
        StudentModel studentModel = studentModelMap.get(userId);

        //if the student wasn't in the map, try to load from file
        if (studentModel == null) {
            studentModel = loadStudentModelFromFile(questionPool, studentModelFilePath, userId, jsonIoHelper, jsonIoUtil);
            //the student didn't have a file, create a new student
            if (studentModel == null) {
                studentModel = new StudentModel(userId, getAllQuestions());
            }
            //either way if the object was created, add it to the map
            studentModelMap.put(userId, studentModel);
        }
        return studentModel;
    }

    private static void overwriteStudentFile(StudentModel currentStudent, String studentModelFilePath, JsonIoUtil jsonIoUtil) throws IOException{
        String fullFilePath = studentModelFilePath + "/" + currentStudent.getUserId() + ".json";
        jsonIoUtil.toFile(fullFilePath, new StudentModelRecord(currentStudent));
    }

    public static boolean isWindowSizeTooBig(int desiredWindowSize, List<Question> allQuestions){
        if(desiredWindowSize<0){
            return true;
        }
        //create parallel arrays of types and count of times seen
        List<String> enumNames = Stream.of(EquineQuestionTypes.values()).map(Enum::name).collect(Collectors.toList());
        List<Integer> typeCounts = new ArrayList<>();

        //initialize typeCounts with 0s
        for(int i=0; i<enumNames.size(); i++){
            typeCounts.add(0);
        }

        isWindowSizeTooBig(enumNames, typeCounts, allQuestions);

        //check if the typecounts are high enough
        for(int i = 0; i<typeCounts.size();i++){
            if(typeCounts.get(i) < desiredWindowSize)
                return true;
        }
        return false;
    }

    public static void isWindowSizeTooBig(List<String> enumNames, List<Integer> typeCounts, List<Question> questionList){
        for(Question currQuestion : questionList) {
            isWindowSizeTooBig(enumNames, typeCounts, currQuestion.getFollowupQuestions());

            for (int i = 0; i < enumNames.size(); i++) {
                if (enumNames.get(i).equals(currQuestion.getType())) {
                    typeCounts.set(i, typeCounts.get(i) + 1);
                }
            }
        }
    }
}
