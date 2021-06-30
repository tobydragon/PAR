package edu.ithaca.dragon.par.spring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
public class ParControllerWithServerTest {
    @Autowired 
    private ParController parController;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort 
    private int port;

    @Test
    public void contextLoads() throws Exception{
        assertNotEquals(null, parController);
    }


    @Test
    public void greetingFromServerTest(){
        long start = System.currentTimeMillis();
        assertEquals("Hello from PAR api2", this.restTemplate.getForObject("http://localhost:" + port + "/api2/", String.class));
        long end = System.currentTimeMillis();
        System.out.println("\nTime it took to run in ms: "+(end-start)+"\n");
    }


}
