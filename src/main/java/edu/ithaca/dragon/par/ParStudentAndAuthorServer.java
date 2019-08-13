package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.AuthorServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.*;
import edu.ithaca.dragon.par.pedagogicalModel.LevelTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGeneratorImp1;
import edu.ithaca.dragon.par.studentModel.StudentModel;
import edu.ithaca.dragon.par.studentModel.StudentReportCreator;
import edu.ithaca.dragon.util.DataUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParStudentAndAuthorServer {

    private AuthorServer authorServer;
    private StudentModelDatastore studentModelDatastore;
    private TaskGenerator taskGenerator;
    private static final int idealQuestionCountPerTypeForAnalysis = 4;

    public ParStudentAndAuthorServer(StudentModelDatastore studentModelDatastore, AuthorDatastore authorDatastore){
            this.studentModelDatastore = studentModelDatastore;
            authorServer = new AuthorServer(authorDatastore);
            taskGenerator = new LevelTaskGenerator(EquineQuestionTypes.makeLevelToTypesMap());
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            return taskGenerator.makeTask(studentModelDatastore.getStudentModel(userId), idealQuestionCountPerTypeForAnalysis);
        }
        else {
            return taskGenerator.makeTask(studentModelDatastore.getStudentModel(userId), studentModelDatastore.getMinQuestionCountPerType());
        }
    }

    public void submitImageTaskResponse( ImageTaskResponse response) throws IOException {
            studentModelDatastore.submitImageTaskResponse(response.getUserId(), response);
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

    public void submitAuthorImageTaskResponse(ImageTaskResponse response) throws IOException{
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
