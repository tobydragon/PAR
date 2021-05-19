package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasourceJson;
import edu.ithaca.dragon.par.pedagogicalmodel2.QuestionChooserRandom;
import edu.ithaca.dragon.par.studentmodel2.StudentModelDatasourceInMemoryExample;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api2")
public class ParController {
    private ParServer parServer;

    public ParController()throws IOException {
        parServer = new ParServer(
                new DomainDatasourceJson("src/test/resources/author/questionPools/SampleQuestionPool.json"),
                StudentModelDatasourceInMemoryExample.createExample(),
                new QuestionChooserRandom()
        );
    }

    @GetMapping("/")
    public String greeting(){
        return "Hello from PAR api2";
    }

    @GetMapping("/getCurrentQuestion")
    public Question getCurrentQuestion(@RequestParam String userId){
        return parServer.getCurrentQuestion(userId);
    }

    @PostMapping("addTimeSeen")
    public void addTimeSeen(@RequestBody StudentAction studentAction){
        parServer.addTimeSeen(studentAction.studentId, studentAction.questionId);
    }

    @PostMapping("/addResponse")
    public void addResponse(@RequestBody StudentResponseAction studentResponseAction){
        parServer.addResponse(studentResponseAction.studentId, studentResponseAction.questionId, studentResponseAction.responseText);
    }
}
