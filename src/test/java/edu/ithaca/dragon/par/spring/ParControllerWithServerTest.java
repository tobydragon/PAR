package edu.ithaca.dragon.par.spring;

import static org.assertj.core.api.Assertions.assertThat;

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
import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.comm.CreateStudentAction;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.domain.DomainDatasource;
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
public class ParControllerWithServerTest {
    
    private ParController parController;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort 
    private int port;

    private Path cohortFile;

    private DomainDatasource domainDatasource;

    @BeforeEach
    public void initController(@TempDir Path tempDir) throws Exception{
        Path questionPoolFile = tempDir.resolve("currentQuestionPool.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentQuestionPool.json"),questionPoolFile);
        
        Path studentDirectory = tempDir.resolve("student");
        FileUtils.copyDirectory(new File("src/test/resources/rewrite/testServerData/student"),studentDirectory.toFile());
        this.cohortFile = tempDir.resolve("currentCohorts.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentCohorts.json"),cohortFile);

        this.domainDatasource = new DomainDatasourceJson(
            "HorseUltrasound",
            questionPoolFile.toString(),
            "src/test/resources/rewrite/testServerData/currentQuestionPool.json",
            new JsonIoHelperDefault()
        );

        this.parController = new ParController(
            new ParServer(
                domainDatasource,

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
    public void contextLoads() throws Exception{
        assertThat(this.parController).isNotNull();
    }


    @Test
    public void greetingFromServerTest(){
        long start = System.currentTimeMillis();
        assertThat(this.parController.greeting()).isEqualTo("Hello from PAR api2");
        long end = System.currentTimeMillis();
        System.out.println("\nTime it took to run in ms: "+(end-start)+"\n");
    }

    @Test
    public void isUserIdAvailableTest(){
        assertThat(this.parController.isUserIdAvailable("o1")).isFalse();
        assertThat(this.parController.isUserIdAvailable("boc1")).isTrue();
    }

    @Test
    public void getCohortIdsTest(){
        assertThat(this.parController.getCohortIds().contains("inOrder")).isTrue();
        assertThat(this.parController.getCohortIds().contains("byOutOfOrderConcepts")).isFalse();
    }

    @Test
    public void getCurrentQuestionTest(@TempDir Path tempDir) throws IOException{
        
        Question questionToBeAsked1 = this.parController.getCurrentQuestion("bocTest");
        assertThat(questionToBeAsked1.getType()).isEqualTo("plane");
        assertThat(questionToBeAsked1.getId()).isEqualTo("614-plane-./images/3CTransverse.jpg");

        Path questionPoolFile = tempDir.resolve("current220QuestionPool.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/current220QuestionPool.json"),questionPoolFile);
        Path studentDirectory = tempDir.resolve("student");
        FileUtils.copyDirectory(new File("src/test/resources/rewrite/testServerData/student"),studentDirectory.toFile());
        
        

        ParController parController220 = new ParController(
            new ParServer(
                new DomainDatasourceJson(
                "Comp220",
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
        
        Question questionToBeAsked2 = parController220.getCurrentQuestion("comp220Test");
        assertThat(questionToBeAsked2.getType()).isEqualTo("Queue");
        assertThat(questionToBeAsked2.getId()).isEqualTo("QueueQ1");
        assertThat(questionToBeAsked2.getFollowupQuestions().size()).isEqualTo(1);

        Question questionToBeAsked3 = parController220.getCurrentQuestion("comp220Test1");
        assertThat(questionToBeAsked3.getType()).isEqualTo("Queue");
        assertThat(questionToBeAsked3.getId()).isEqualTo("QueueQ1");
        assertThat(questionToBeAsked3.getFollowupQuestions().size()).isEqualTo(0);

    }

    @Test
    public void addNewUserTest() throws IOException{
        
        // issue creating cohorts from json file when reading boc (using pair list for conceptScore)
        this.parController.addNewUser(new CreateStudentAction("o5","inOrder"));
        Map <String,Cohort> cohortMap = new JsonIoUtil(new JsonIoHelperDefault()).mapfromReadOnlyFile("src/test/resources/rewrite/testServerData/currentCohortsNoBoc.json", Cohort.class);
        assertThat(cohortMap.get("inOrder").studentIds.contains("o5"));
    }


}
