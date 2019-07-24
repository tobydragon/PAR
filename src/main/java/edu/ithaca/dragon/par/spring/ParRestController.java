package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.equineUltrasound.EquineQuestionTypes;
import edu.ithaca.dragon.par.authorModel.ParAuthoringServer;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.springio.JsonSpringAuthorDatastore;
import edu.ithaca.dragon.par.io.springio.JsonSpringStudentModelDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.ImageTaskSettings;
import edu.ithaca.dragon.par.pedagogicalModel.PageSettings;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonSpringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ParRestController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private ParAuthoringServer parAuthoringServer;
    private ParServer parServer;

    ParRestController(){
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

    @GetMapping("/")
    public String index() {
        return "Greetings from PAR API!";
    }

    @GetMapping("/getPageSettings")
    public PageSettings getPageSettings(@RequestParam String userId) throws IOException  {
        if(userId.equals("author")){
            return JsonSpringUtil.fromClassPathJson("author/AuthorPageSettingsExample.json", PageSettings.class);
        }
        return JsonSpringUtil.fromClassPathJson("author/PageSettingsExample.json", PageSettings.class);
    }

    @GetMapping("/getImageTaskSettings")
    public ImageTaskSettings getImageTaskSettings(@RequestParam String userId) throws IOException  {
        if(userId.equals("author")){
            return JsonSpringUtil.fromClassPathJson("author/AuthorSettingsExample.json", ImageTaskSettings.class);
        }
        return JsonSpringUtil.fromClassPathJson("author/SettingsExample.json", ImageTaskSettings.class);
    }

    @GetMapping("/nextImageTask")
    public ImageTask nextImageTask(@RequestParam String userId) throws IOException {
        return parServer.nextImageTask(userId);
    }

    @PostMapping("/recordResponse")
    public ResponseEntity<String> recordResponse(@RequestBody ImageTaskResponse response) {
        try {
            parServer.imageTaskResponseSubmitted(response, response.getUserId());
            return ResponseEntity.ok().body("ok");
        } catch (Exception e){
            logger.warn(e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String userId) throws IOException{
        parServer.logout(userId);
    }

    @GetMapping("/calcScore")
    public String calcScore(@RequestParam String userId) throws IOException {
        return DataUtil.format(parServer.calcScore(userId));
    }

    @GetMapping("/calcScoreByType")
    public Map<String, Double> calcScoreByType(@RequestParam String userId) throws IOException{
        return parServer.calcScoreByType(userId);
    }
    @GetMapping("/knowledgeBase")
    public Map<EquineQuestionTypes,String> knowledgeBaseEstimate(@RequestParam String userId)throws IOException {
        return parServer.knowledgeBaseEstimate(userId);
    }
    @GetMapping("/nextAuthorImageTask")
    public ImageTask nextAuthorImageTask() throws IOException {
        return parAuthoringServer.nextImageTaskTemplate();
    }

    @PostMapping("/submitAuthorImageTaskResponse")
    public ResponseEntity<String> submitAuthorImageTaskResponse(@RequestBody ImageTaskResponse response) {
        try {
            parAuthoringServer.imageTaskResponseSubmitted(response);
            return ResponseEntity.ok().body("ok");
        } catch (Exception e){
            logger.warn(e);
            return ResponseEntity.notFound().build();
        }
    }
}
