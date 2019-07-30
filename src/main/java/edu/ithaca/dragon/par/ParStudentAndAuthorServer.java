package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.ParAuthoringServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.AuthorDatastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;
import edu.ithaca.dragon.util.DataUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParStudentAndAuthorServer {

    private ParAuthoringServer parAuthoringServer;
    private StudentModelDatastore studentModelDatastore;

    public ParStudentAndAuthorServer(StudentModelDatastore studentModelDatastore, AuthorDatastore authorDatastore) throws IOException {
            this.studentModelDatastore = studentModelDatastore;
            parAuthoringServer = new ParAuthoringServer(authorDatastore);
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        return TaskGenerator.makeTask(studentModelDatastore.getStudentModel(userId));
    }

    public void submitImageTaskResponse( ImageTaskResponse response) throws IOException {
            studentModelDatastore.imageTaskResponseSubmitted(response.getUserId(), response);
    }

    public void logout(String userId) throws IOException{
        studentModelDatastore.logout(userId);
    }

    public String calcOverallKnowledgeEstimate(String userId) throws IOException {
        return DataUtil.format(studentModelDatastore.getStudentModel(userId).knowledgeScore());
    }

    public Map<String, Double> calcKnowledgeEstimateByType(String userId) throws IOException{
        return studentModelDatastore.getStudentModel(userId).knowledgeScoreByType();
    }

    public Map<EquineQuestionTypes,String> calcKnowledgeEstimateStringsByType(String userId)throws IOException {
        return studentModelDatastore.getStudentModel(userId).generateKnowledgeBaseMap();
    }

    //----------- Author methods  --------------//

    public ImageTask nextAuthorImageTask() {
        return parAuthoringServer.nextImageTaskTemplate();
    }

    public void submitAuthorImageTaskResponse(ImageTaskResponse response) throws IOException{
            parAuthoringServer.imageTaskResponseSubmitted(response);
    }
    public List<ImageTask> authoredQuestions(){
        return parAuthoringServer.authoredQuestions();
    }


    //----------- Student-Author Interacting methods  --------------//

    public void transferAuthoredQuestionsToStudentServer() throws IOException{
        studentModelDatastore.addQuestions(parAuthoringServer.removeAllAuthoredQuestions());
    }

}
