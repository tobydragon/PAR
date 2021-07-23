package edu.ithaca.dragon.par.spring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.util.JsonIoHelperDefault;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;




public class ParControllerWithMockServerTest {
    
    private MockMvc mockMvc;

    // @InjectMocks
    private ParController parController;

    @BeforeEach
    public void initController(@TempDir Path tempDir) throws IOException{
        Path questionPoolFile = tempDir.resolve("currentQuestionPool.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentQuestionPool.json"),questionPoolFile);
        
        Path studentDirectory = tempDir.resolve("student");
        FileUtils.copyDirectory(new File("src/test/resources/rewrite/testServerData/student"),studentDirectory.toFile());
        Path cohortFile = tempDir.resolve("currentCohorts.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentCohorts.json"),cohortFile);

        DomainDatasource domainDatasource = new DomainDatasourceJson(
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

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.parController).build();
    }

    @Test
    public void greetingTest() throws Exception{
        long start = System.currentTimeMillis();
        assertNotNull(this.mockMvc);
        this.mockMvc.perform(get("/api2/")).andExpect(status().isOk()).andExpect(content().string("Hello from PAR api2"));
        long end = System.currentTimeMillis();
        System.out.println("\nTime it took to run in ms: "+(end-start)+"\n");
    }


}
