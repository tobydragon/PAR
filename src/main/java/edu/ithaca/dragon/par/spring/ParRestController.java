package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ParRestController {

    ParServer parServer;

    ParRestController(){
        super();
        parServer = new ParServer();
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from PAR API!";
    }

    @GetMapping("/nextQuestion")
    public Question nextQuestion() {
        return parServer.nextQuestion();
    }

    @GetMapping("/nextImageTask")
    public List<Question> nextImageTask() {
        Logger logger = LogManager.getLogger(this.getClass());
        try {
            //change from sample questions to Image task once ImageTask has multiple questions
            //what it is currently is just for testing
            List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
            return questionsFromFile;
        }
        catch (IOException e){
            logger.error("sample questions not found", e);
            return null;
        }
    }

}
