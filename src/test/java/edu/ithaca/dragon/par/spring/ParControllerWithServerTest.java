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
import org.springframework.boot.test.context.SpringBootTest;

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
// Example tests that demonstrate the logic of tests that use the application context
// @SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
public class ParControllerWithServerTest {
    
    private ParController parController;

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
    public void getQuestionHistorySummaryTest(){
        QuestionHistorySummary o1Hist = this.parController.getQuestionHistorySummary("o1");
        assertThat(o1Hist.getQuestionIdsSeen().size()).isEqualTo(4);
        assertThat(o1Hist.getQuestionIdsRespondedTo().size()).isEqualTo(3);
        assertThat(o1Hist.getQuestionIdsIncorrect().contains("850-structure3-./images/Annotated2Long.jpg"));
    }

    @Test
    public void addNewUserTest() throws IOException{
        
        // issue creating cohorts from json file when reading boc (using pair list for conceptScore)
        this.parController.addNewUser(new CreateStudentAction("o5","inOrder"));
        Map <String,Cohort> cohortMap = new JsonIoUtil(new JsonIoHelperDefault()).mapfromReadOnlyFile("src/test/resources/rewrite/testServerData/currentCohortsNoBoc.json", Cohort.class);
        assertThat(cohortMap.get("inOrder").studentIds.contains("o5"));

        // testing new and existing studentId
        assertFalse(this.parController.addNewUser(new CreateStudentAction("o5","inOrder")));
        assertTrue(this.parController.addNewUser(new CreateStudentAction("o7","inOrder")));
        assertThat(cohortMap.get("inOrder").studentIds.contains("o7"));
    }

    @Test
    public void addTimeSeenTest() {
        // testing invalid and valid studentId
        assertFalse(this.parController.addTimeSeen(new StudentAction("o7", "850-structure3-./images/Annotated2Long.jpg")));
        assertTrue(this.parController.addTimeSeen(new StudentAction("o1", "850-structure3-./images/Annotated2Long.jpg")));
    }

    @Test
    public void addResponseTest(){
        // testing invalid and valid studentId
        assertFalse(this.parController.addResponse(new StudentResponseAction("o7", "850-structure3-./images/Annotated2Long.jpg", "response1")));
        assertTrue(this.parController.addResponse(new StudentResponseAction("o1", "850-structure3-./images/Annotated2Long.jpg", "response2")));
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
}
