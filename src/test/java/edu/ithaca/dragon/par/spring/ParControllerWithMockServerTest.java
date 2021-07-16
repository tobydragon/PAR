package edu.ithaca.dragon.par.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ParControllerWithMockServerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void greetingTest() throws Exception{
        long start = System.currentTimeMillis();
        this.mvc.perform(get("/api2/")).andExpect(status().isOk()).andExpect(content().string("Hello from PAR api2"));
        long end = System.currentTimeMillis();
        System.out.println("\nTime it took to run in ms: "+(end-start)+"\n");
    }


}
