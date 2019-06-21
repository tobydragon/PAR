package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.io.ImageTaskResponse;
import edu.ithaca.dragon.par.io.JsonDatastore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ParRestController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private ParServer parServer;

    ParRestController(){
        super();
        try {
            parServer = new ParServer(new QuestionPool(new JsonDatastore("src/test/resources/author/SampleQuestionsSameDifficulty.json")));
        }
        catch(IOException e){
            throw new RuntimeException("Server can't start without questionPool");
        }
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from PAR API!";
    }

    @GetMapping("/nextImageTask")
    public ImageTask nextImageTask() {
            return parServer.nextImageTask("Student");
    }

    @PostMapping("/recordResponse")
    public ResponseEntity<String> recordResponse(@RequestBody ImageTaskResponse response) {
        try {
            String updated = ParServer.sendNewImageTaskResponse(response);
            return ResponseEntity.ok().body(updated);
        } catch (Exception e){
            logger.warn(e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/calcScore")
    public double calcScore(@RequestParam String userId){
        return parServer.calcScore(userId);
    }

}
