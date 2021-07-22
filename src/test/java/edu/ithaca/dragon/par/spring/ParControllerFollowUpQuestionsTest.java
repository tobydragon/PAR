package edu.ithaca.dragon.par.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.comm.CreateStudentAction;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)

public class ParControllerFollowUpQuestionsTest {

    private ParController parController;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort 
    private int port;

    private Path cohortFile;

    @BeforeEach
    public void initController(@TempDir Path tempDir) throws Exception {
        Path questionPoolFile = tempDir.resolve("current220QuestionPool.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/current220QuestionPool.json"), questionPoolFile);
        
        Path studentDirectory = tempDir.resolve("student");
        FileUtils.copyDirectory(new File("src/test/resources/rewrite/testServerData/student"),studentDirectory.toFile());
        this.cohortFile = tempDir.resolve("currentCohorts.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentCohorts.json"),cohortFile);

        this.parController = new ParController(
            new ParServer(
                new DomainDatasourceJson(
                "HorseUltrasound",
                questionPoolFile.toString(),
                "src/test/resources/rewrite/testServerData/current220QuestionPool.json",
                new JsonIoHelperDefault()
                ),

                new StudentModelDatasourceJson(
                "allTestStudents",
                studentDirectory.toString(),
                new JsonIoHelperDefault()
                ),

                //TODO: remove default users from cohort datastores once there is a viable way to add students
                new CohortDatasourceJson(
                    "allCohorts",
                    cohortFile.toString(),
                    "src/test/resources/rewrite/testServerData/currentCohorts.json",
                    new JsonIoHelperDefault()
                )
            
            )
        );
    }
    
    @Test
    public void followUpQuestionsTest(){
        // current question should be QueueQ1 after answering Queue0.5 correct
        Question questionToBeAsked = this.parController.getCurrentQuestion("comp220Test");
        assertThat(questionToBeAsked.getType()).isEqualTo("Queue");
        assertThat(questionToBeAsked.getId()).isEqualTo("QueueQ1");
        assertThat(questionToBeAsked.getFollowupQuestions().size()).isEqualTo(1);

        // current question should have no follow up after answering Queue0.5 wrong
        // Question questionToBeAsked2 = this.parController.getCurrentQuestion("comp220Test1");
        // assertThat(questionToBeAsked2.getType()).isEqualTo("Queue");
        // assertThat(questionToBeAsked2.getId()).isEqualTo("QueueQ1");
        // assertThat(questionToBeAsked2.getFollowupQuestions().size()).isEqualTo(0);


        // current question should be Big-OQ0 after answering QueueQ1 correct
        // Question questionToBeAsked3 = this.parController.getCurrentQuestion("comp220Test2");
        // assertThat(questionToBeAsked3.getType()).isEqualTo("Big-O");
        // assertThat(questionToBeAsked3.getId()).isEqualTo("Big-OQ0"); // gives error here because it skips this question
        // assertThat(questionToBeAsked3.getFollowupQuestions().size()).isEqualTo(0);

    }

}


