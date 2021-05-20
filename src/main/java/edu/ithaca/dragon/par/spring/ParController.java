package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.util.JsonIoHelperSpring;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api2")
public class ParController {
    private final ParServer parServer;

    public ParController()throws IOException {
        parServer = new ParServer(
            new DomainDatasourceJson(
                "HorseUltrasound",
                "localData/currentQuestionPool.json",
                "author/defaultQuestionPool.json",
                new JsonIoHelperSpring()
            ),
            new StudentModelDatasourceJson(
                "allStudents",
                "localData/student",
                new JsonIoHelperSpring()
            ),
            new CohortDatasourceJson(
                "allCohorts",
                "localData/currentCohorts.json",
                "author/defaultCohorts.json",
                new JsonIoHelperSpring()
            )
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

    @PostMapping("/addTimeSeen")
    public void addTimeSeen(@RequestBody StudentAction studentAction){
        parServer.addTimeSeen(studentAction.studentId, studentAction.questionId);
    }

    @PostMapping("/addResponse")
    public void addResponse(@RequestBody StudentResponseAction studentResponseAction){
        parServer.addResponse(studentResponseAction.studentId, studentResponseAction.questionId, studentResponseAction.responseText);
    }
}
