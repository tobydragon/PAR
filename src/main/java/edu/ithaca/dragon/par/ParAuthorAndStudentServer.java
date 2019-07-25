package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.authorModel.ParAuthoringServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.springio.JsonSpringAuthorDatastore;
import edu.ithaca.dragon.par.io.springio.JsonSpringStudentModelDatastore;
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

    ParAuthorAndStudentServer() throws IOException {
        super();
        try {
            parServer = new ParServer(new JsonSpringStudentModelDatastore("localData/currentQuestionPool.json","author/DemoQuestionPool.json", "localData/students"));
            parAuthoringServer = new ParAuthoringServer(new JsonSpringAuthorDatastore("localData/currentAuthoredQuestions.json", "author/AuthorQuestionsDefault.json",
                    "localData/currentAuthorQuestionTemplates.json", "author/AuthorQuestionTemplatesDefault.json", "localData/currentAuthorModel.json" ));
        }
        catch(IOException e){
            throw new RuntimeException("Server can't start without all necessary files loaded: ", e);
        }
    }

    //----------- Student methods  --------------//

    public ImageTask nextImageTask( String userId) throws IOException {
        return parServer.nextImageTask(userId);
    }

    public void imageTaskResponseSubmitted( ImageTaskResponse response) throws IOException {
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

    public void transferAuthoredQuestionsToStudentServer(){

    }

}
