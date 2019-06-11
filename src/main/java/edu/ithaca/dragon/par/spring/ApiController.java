package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.domain.Question;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    ParServer parServer;

    ApiController(){
        super();
        parServer = new ParServer();
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from PAR API!";
    }

    @RequestMapping("/nextQuestion")
    public Question nextQuestion() {
        return parServer.nextQuestion();
    }

}
