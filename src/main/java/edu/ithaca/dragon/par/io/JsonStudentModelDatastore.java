package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonStudentModelDatastore extends JsonQuestionPoolDatastore implements StudentModelDatastore {

    private String studentModelFilePath;
    private Map<String, StudentModel> studentModelMap;
    private int minQuestionsPerType;
    private JsonIoHelper jsonIoHelper;
    private JsonIoUtil jsonIoUtil;

    public JsonStudentModelDatastore(String questionFilePath, String studentModelFilePath) throws IOException {
        this(questionFilePath, null, new JsonIoHelperDefault(), studentModelFilePath);
    }

    public JsonStudentModelDatastore(String questionFilePath, String defaultQuestionReadOnlyFilePath, JsonIoHelper jsonIoHelper, String studentModelFilePath) throws IOException {
        super(questionFilePath, defaultQuestionReadOnlyFilePath, jsonIoHelper);
        this.studentModelFilePath = studentModelFilePath;
        this.jsonIoHelper = jsonIoHelper;
        this.jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        studentModelMap = new HashMap<>();
        minQuestionsPerType = calcMinQuestionCountPerType(questionPool.getAllQuestions());
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
    public List<StudentModel> getAllStudentModels()throws IOException{
        List<StudentModel> studentModels=new ArrayList<>();
        HashSet<String> studentIds = new HashSet<>(studentModelMap.keySet());
        studentIds.addAll(getAllSavedStudentIds(studentModelFilePath));
        for(String currId:studentIds){
            StudentModel studentModel=getOrCreateStudentModel(currId);
            studentModels.add(studentModel);
        }
        return studentModels;
    }

    @Override
    public void submitImageTaskResponse(String userId, ImageTaskResponseOOP imageTaskResponse, int questionCountPerTypeForAnalysis) throws IOException{
        StudentModel currentStudent = getStudentModel(userId);
        currentStudent.imageTaskResponseSubmitted(imageTaskResponse, questionPool, questionCountPerTypeForAnalysis);
        overwriteStudentFile(currentStudent, studentModelFilePath, jsonIoUtil);
    }

    @Override
    public void increaseTimesAttempted(String userId, List<Question> questions) throws IOException {
        StudentModel studentModel = getStudentModel(userId);
        for(Question currQuestion : questions){
            studentModel.increaseTimesAttempted(currQuestion.getId());
        }
        overwriteStudentFile(studentModel, studentModelFilePath, jsonIoUtil);
    }

    @Override
    public void increaseTimesAttemptedById(String userId, List<String> questionIds) throws IOException {
        StudentModel studentModel = getStudentModel(userId);
        for(String id : questionIds){
            studentModel.increaseTimesAttempted(id);
        }
        overwriteStudentFile(studentModel, studentModelFilePath, jsonIoUtil);
    }

    @Override
    public int getMinQuestionCountPerType() {
        return minQuestionsPerType;
    }

    @Override
    public void addQuestions(List<Question> questions) throws IOException {
        super.addQuestions(questions);
        minQuestionsPerType = calcMinQuestionCountPerType(questionPool.getAllQuestions());

        //TODO: test combinations of students in or not in memory / file
        List<StudentModel> studentModels=getAllStudentModels();
        for (StudentModel studentModel : studentModels){
            StudentModel currModel = getOrCreateStudentModel(studentModel.getUserId());
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

    public static int calcMinQuestionCountPerType(List<Question> allQuestions){
        Map<String, Integer> typeCounts = new HashMap<>();
        populateQuestionByTypeCountMap(allQuestions, typeCounts);
        return typeCounts.values().stream().mapToInt(v -> v).min().orElse(0);
    }

    public static void populateQuestionByTypeCountMap(List<Question> allQuestions,Map<String, Integer> typeCounts){
        for (Question question : allQuestions){
            populateQuestionByTypeCountMap(question.getFollowupQuestions(), typeCounts);
            Integer count = typeCounts.get(question.getType());
            if (count != null){
                typeCounts.put(question.getType(), count+1);
            }
            else {
                typeCounts.put(question.getType(), 1);
            }
        }
    }
}
