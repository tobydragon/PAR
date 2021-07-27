package edu.ithaca.dragon.par.spring;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class StressTestActionsForUserWorker implements Runnable{
    private String questionId;
    private String studentId;
    private MockMvc mockMvc;
    private int numRunIter;
    public StressTestActionsForUserWorker(String questionId, String studentId, MockMvc mockMvc, int numRunIter){
        this.questionId = questionId;
        this.mockMvc = mockMvc;
        this.studentId = studentId;
        this.numRunIter = numRunIter;
    }
    public void run(){
        
        for(int i=0;i<numRunIter;i++){
            fakeWork();
            try{
            mockMvc.perform(
            post("/api2/addTimeSeen")
            .content(new ObjectMapper().writeValueAsString(new StudentAction(studentId, questionId)))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
            } catch(Exception e){
                throw new RuntimeException("exception thrown when addTimeSeen post request was made");
            }
            String responseText = "response"+i;
            try{
                mockMvc.perform(
                post("/api2/addResponse")
                .content(new ObjectMapper().writeValueAsString(new StudentResponseAction(studentId, questionId, responseText)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
            } catch(Exception e){
                throw new RuntimeException("exception thrown when addResponse post request was made");
            }
        }
    }

    //wastes time to simluate the work
	private void fakeWork(){
		double x = 100000;
		for (int y=0; y<100000; y++){
			x/=2;
		}
	}
    
}
