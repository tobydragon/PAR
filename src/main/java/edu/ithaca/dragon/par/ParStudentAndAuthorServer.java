package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.AuthorServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.AuthorDatastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponseOOP;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.util.DataUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParStudentAndAuthorServer {

    private AuthorServer authorServer;
    private StudentModelDatastore studentModelDatastore;
    private static final int idealQuestionCountPerTypeForAnalysis = 4;

    public ParStudentAndAuthorServer(StudentModelDatastore studentModelDatastore, AuthorDatastore authorDatastore) throws IOException {
            this.studentModelDatastore = studentModelDatastore;
            authorServer = new AuthorServer(authorDatastore);
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        if (idealQuestionCountPerTypeForAnalysis <= studentModelDatastore.getMinQuestionCountPerType()){
            return TaskGenerator.findLevelAndMakeTask(studentModelDatastore.getStudentModel(userId), idealQuestionCountPerTypeForAnalysis);
        }
        else {
            return TaskGenerator.findLevelAndMakeTask(studentModelDatastore.getStudentModel(userId), studentModelDatastore.getMinQuestionCountPerType());
        }
    }

    public void submitImageTaskResponse( ImageTaskResponseOOP response) throws IOException {
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
