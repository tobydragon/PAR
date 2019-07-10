package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import edu.ithaca.dragon.par.pedagogicalModel.Settings;
import edu.ithaca.dragon.util.DataUtil;
import edu.ithaca.dragon.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ParRestController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private ParServer parServer;

    ParRestController(){
        super();
        try {
            parServer = new ParServer(new JsonDatastore("src/main/resources/author/DemoQuestionPool.json",
                    "src/main/resources/students"));
        }
        catch(IOException e){
            throw new RuntimeException("Server can't start without questionPool or studentRecord");
        }
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from PAR API!";
    }

    @GetMapping("/getSettings")
    public Settings getSettings() throws IOException  {
        return JsonUtil.fromJsonFile("src/main/resources/author/SettingsExample.json", Settings.class);
    }

    @GetMapping("/nextImageTask")
    public ImageTask nextImageTask(@RequestParam String userId) throws IOException {
            return parServer.nextImageTask(userId);
    }

    @PostMapping("/recordResponse")
    public ResponseEntity<String> recordResponse(@RequestBody ImageTaskResponse response) {
        try {
            System.out.println("response received: " + response.getUserId() + "  " + response.getResponseTexts());
            parServer.imageTaskResponseSubmitted(response, response.getUserId());
            return ResponseEntity.ok().body("ok");
        } catch (Exception e){
            logger.warn(e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String userId){
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

    @GetMapping("/getScoreStringByType")
    public Map<String,String> getScoreStringByType(@RequestParam String userId) throws IOException{
        return parServer.getScoreStringByType(userId);
    }
}
