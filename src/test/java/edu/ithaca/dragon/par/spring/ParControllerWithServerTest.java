package edu.ithaca.dragon.par.spring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.util.JsonIoHelperDefault;

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

    @BeforeEach
    void initController() throws Exception{
        this.parController = new ParController(
            new ParServer(
                new DomainDatasourceJson(
                    "HorseUltrasound",
                    "src/test/resources/rewrite/testServerData/currentQuestionPool.json",
                    "author/defaultQuestionPool.json",
                    new JsonIoHelperDefault()
                ),

                new StudentModelDatasourceJson(
                "allStudents",
                "src/test/resources/rewrite/testServerData/student",
                new JsonIoHelperDefault()
                ),

                //TODO: remove default users from cohort datastores once there is a viable way to add students
                new CohortDatasourceJson(
                    "allCohorts",
                    "src/test/resources/rewrite/testServerData/currentCohorts.json",
                    "author/defaultCohorts.json",
                    new JsonIoHelperDefault()
                )
            
            )
        );
    }

    @Test
    public void contextLoads() throws Exception{
        assertNotEquals(null,  new ParController());
    }


    @Test
    public void greetingFromServerTest(){
        long start = System.currentTimeMillis();
        assertEquals("Hello from PAR api2", this.restTemplate.getForObject("http://localhost:" + port + "/api2/", String.class));
        long end = System.currentTimeMillis();
        System.out.println("\nTime it took to run in ms: "+(end-start)+"\n");
    }


}
