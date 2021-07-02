package edu.ithaca.dragon.par.spring;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.comm.CreateStudentAction;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.util.JsonIoHelperSpring;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api2")
public class ParController {
    private final ParServer parServer;

    public ParController(ParServer parServer){
        this.parServer=parServer;
    }

    public ParController()throws IOException {
        //TODO: remove default users once there is a front-end way to create new users
        // List.of("r1", "r2", "r3", "o1", "o2", "o3", "o4").forEach((userId)-> {
        //     if (studentModelDatasourceJson.idIsAvailable(userId)){
        //         studentModelDatasourceJson.createNewModelForId(userId);
        //     }
        // });

        this(
            new ParServer(
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

                //TODO: remove default users from cohort datastores once there is a viable way to add students
                new CohortDatasourceJson(
                    "allCohorts",
                    "localData/currentCohorts.json",
                    "author/defaultCohorts.json",
                    new JsonIoHelperSpring()
                )
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


    @GetMapping("/isUserIdAvailable")
    public Boolean isUserIdAvailable(@RequestParam String idToCheck) {
        return parServer.isUserIdAvailable(idToCheck);
    }

    @GetMapping("/getCohortIds")
    public List<String> getCohortIds(){
        return parServer.getCohortIds();
    }

    @PostMapping("/addNewUser")
    public void addNewUser(@RequestBody CreateStudentAction studentAction){
        parServer.addNewUser(studentAction.studentId, studentAction.cohortId);

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
