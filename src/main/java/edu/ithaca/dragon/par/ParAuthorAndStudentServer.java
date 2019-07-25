package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.ParAuthoringServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.AuthorDatastore;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.StudentModelDatastore;
import edu.ithaca.dragon.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public class ParAuthorAndStudentServer {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private ParAuthoringServer parAuthoringServer;
    private ParServer parServer;

    public ParAuthorAndStudentServer(StudentModelDatastore studentModelDatastore, AuthorDatastore authorDatastore) throws IOException {
            parServer = new ParServer(studentModelDatastore);
            parAuthoringServer = new ParAuthoringServer(authorDatastore);
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        return parServer.nextImageTask(userId);
    }

    public void submitImageTaskResponse( ImageTaskResponse response) throws IOException {
            parServer.imageTaskResponseSubmitted(response, response.getUserId());
    }

    public void logout(String userId) throws IOException{
        parServer.logout(userId);
    }

    public String calcOverallKnowledgeEstimate(String userId) throws IOException {
        return DataUtil.format(parServer.calcScore(userId));
    }

    public Map<String, Double> calcKnowledgeEstimateByType(String userId) throws IOException{
        return parServer.calcScoreByType(userId);
    }

    public Map<EquineQuestionTypes,String> calcKnowledgeEstimateStringsByType(String userId)throws IOException {
        return parServer.knowledgeBaseEstimate(userId);
    }

    //----------- Author methods  --------------//

    public ImageTask nextAuthorImageTask() throws IOException {
        return parAuthoringServer.nextImageTaskTemplate();
    }

    public ResponseEntity<String> submitAuthorImageTaskResponse(ImageTaskResponse response) {
        try {
            parAuthoringServer.imageTaskResponseSubmitted(response);
            return ResponseEntity.ok().body("ok");
        } catch (Exception e){
            logger.warn(e);
            return ResponseEntity.notFound().build();
        }
    }

    //----------- Student-Author Interacting methods  --------------//

    public void transferAuthoredQuestionsToStudentServer() throws IOException{
        parServer.addQuestions(parAuthoringServer.removeAllAuthoredQuestions());
    }

}
