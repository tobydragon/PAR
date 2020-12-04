package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.AuthorServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.*;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.io.AuthorDatastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.LevelMessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGeneratorAttachment;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.StudentReportCreator;
import edu.ithaca.dragon.util.DataUtil;
import org.springframework.scheduling.config.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParStudentAndAuthorServer {

    private AuthorServer authorServer;
    private StudentModelDatastore studentModelDatastore;
    private CohortDatastore cohortDatastore;
    private static final int idealQuestionCountPerTypeForAnalysis = 4;

    public ParStudentAndAuthorServer(StudentModelDatastore studentModelDatastore, AuthorDatastore authorDatastore, CohortDatastore cohortDatastore){
            this.studentModelDatastore = studentModelDatastore;
            authorServer = new AuthorServer(authorDatastore);
            this.cohortDatastore = cohortDatastore;
        System.out.println();
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        ImageTask imageTask = null;
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            imageTask = cohortDatastore.getTaskGeneratorFromStudentID(userId).makeTask(studentModelDatastore.getStudentModel(userId), idealQuestionCountPerTypeForAnalysis);
        } else{
            imageTask = cohortDatastore.getTaskGeneratorFromStudentID(userId).makeTask(studentModelDatastore.getStudentModel(userId), studentModelDatastore.getMinQuestionCountPerType());
        }
        studentModelDatastore.increaseTimesSeen(userId, imageTask.getTaskQuestions());
        return imageTask;
    }

    public ImageTask getImageTask(String userId) throws IOException {
        ImageTask imageTask = null;
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            imageTask = cohortDatastore.getTaskGeneratorFromStudentID(userId).makeTask(studentModelDatastore.getStudentModel(userId), idealQuestionCountPerTypeForAnalysis);
        } else{
            imageTask = cohortDatastore.getTaskGeneratorFromStudentID(userId).makeTask(studentModelDatastore.getStudentModel(userId), studentModelDatastore.getMinQuestionCountPerType());
        }
        return imageTask;
    }

    public void updateTimesSeen(String userId, List<Question> questions) throws IOException{
        studentModelDatastore.increaseTimesSeen(userId, questions);
    }

    public String getMessage(String userId, ImageTask it) throws IOException{
        return cohortDatastore.getMessageGeneratorFromStudentID(userId).generateMessage(studentModelDatastore.getStudentModel(userId), it);
    }

    public void submitImageTaskResponse( ImageTaskResponseOOP response) throws IOException, IllegalArgumentException {
        String userId = response.getUserId();
        studentModelDatastore.submitImageTaskResponse(userId, response, idealQuestionCountPerTypeForAnalysis);
        TaskGenerator tg = cohortDatastore.getTaskGeneratorFromStudentID(userId);
        if (tg instanceof LevelTaskGeneratorAttachment){
            int level = LevelTaskGeneratorAttachment.calcLevel(studentModelDatastore.getStudentModel(response.getUserId()).calcKnowledgeEstimateByType(idealQuestionCountPerTypeForAnalysis));
            if (level < 1 || level > 6){
                throw new IllegalArgumentException("Invalid level calculated");
            }
            studentModelDatastore.getStudentModel(response.getUserId()).setCurrentLevel(level);
        } else{
            int level = LevelTaskGenerator.calcLevel(studentModelDatastore.getStudentModel(response.getUserId()).calcKnowledgeEstimateByType(idealQuestionCountPerTypeForAnalysis));
            if (level < 1 || level > 8){
                throw new IllegalArgumentException("Invalid level calculated");
            }
            studentModelDatastore.getStudentModel(response.getUserId()).setCurrentLevel(level);
        }

    }

    public void logout(String userId) throws IOException{
        studentModelDatastore.logout(userId);
    }

    public String calcOverallKnowledgeEstimate(String userId) throws IOException {
        return DataUtil.format(studentModelDatastore.getStudentModel(userId).calcKnowledgeEstimate());
    }

    public Map<String, Double> calcKnowledgeEstimateByType(String userId) throws IOException{
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            return studentModelDatastore.getStudentModel(userId).calcKnowledgeEstimateByType(idealQuestionCountPerTypeForAnalysis);
        }
        else {
            return studentModelDatastore.getStudentModel(userId).calcKnowledgeEstimateByType(studentModelDatastore.getMinQuestionCountPerType());
        }
    }

    public Map<EquineQuestionTypes,String> calcKnowledgeEstimateStringsByType(String userId)throws IOException {
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            return studentModelDatastore.getStudentModel(userId).calcKnowledgeEstimateStringsByType(idealQuestionCountPerTypeForAnalysis);
        }
        else {
            return studentModelDatastore.getStudentModel(userId).calcKnowledgeEstimateStringsByType(studentModelDatastore.getMinQuestionCountPerType());
        }
    }

    public TeacherReport buildTeacherReport()throws IOException{
        List<StudentModel> studentModels=new ArrayList<>(studentModelDatastore.getAllStudentModels());
        List<StudentReport> studentReports=new ArrayList<>();

        for(StudentModel studentModel:studentModels) {
            if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType())
                studentReports.add(StudentReportCreator.buildStudentReport(studentModel.getUserResponseSet(), studentModel.questionCountsByTypeMap(), idealQuestionCountPerTypeForAnalysis));
            else
                studentReports.add(StudentReportCreator.buildStudentReport(studentModel.getUserResponseSet(), studentModel.questionCountsByTypeMap(), studentModelDatastore.getMinQuestionCountPerType()));
        }
        return new TeacherReport(studentReports);
    }

    //----------- Author methods  --------------//

    public ImageTask nextAuthorImageTask() {
        return authorServer.nextImageTaskTemplate();
    }

    public void submitAuthorImageTaskResponse(ImageTaskResponseOOP response) throws IOException{
            authorServer.imageTaskResponseSubmitted(response);
    }
    public List<ImageTask> authoredQuestions(){
        return authorServer.authoredQuestions();
    }


    //----------- Student-Author Interacting methods  --------------//

    public void transferAuthoredQuestionsToStudentServer() throws IOException{
        studentModelDatastore.addQuestions(authorServer.removeAllAuthoredQuestions());
    }

}
