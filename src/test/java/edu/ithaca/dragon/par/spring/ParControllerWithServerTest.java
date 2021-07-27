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

    

}
