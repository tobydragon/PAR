package edu.ithaca.dragon.par.spring;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.ithaca.dragon.par.comm.CreateStudentAction;
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class StressTestUsersForActionWorker implements Runnable{
    private MockMvc mockMvc;
    private int numRunIter;
    private String cohortId;
    private int threadNum;
    public StressTestUsersForActionWorker(String cohortId, MockMvc mockMvc, int threadNum, int numRunIter){
        this.mockMvc = mockMvc;
        this.numRunIter = numRunIter;
        this.cohortId = cohortId;
        this.threadNum = threadNum;
    }
    public void run(){
        
        for(int i=0;i<numRunIter;i++){
            String studentId = threadNum+String.valueOf(cohortId.charAt(0))+i;
            try{
                mockMvc.perform(
                post("/api2/addNewUser")
                .content(new ObjectMapper().writeValueAsString(new CreateStudentAction(studentId,cohortId)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
            } catch(Exception e){
                throw new RuntimeException("exception thrown when addTimeSeen post request was made");
            }
            fakeWork();
            try{
            mockMvc.perform(
            post("/api2/addTimeSeen")
            .content(new ObjectMapper().writeValueAsString(new StudentAction(studentId, "614-plane-./images/3CTransverse.jpg")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
            } catch(Exception e){
                throw new RuntimeException("exception thrown when addTimeSeen post request was made");
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
