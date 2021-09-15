package edu.ithaca.dragon.par.spring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.ithaca.dragon.par.ParServer;
import edu.ithaca.dragon.par.cohort.Cohort;
import edu.ithaca.dragon.par.cohort.CohortDatasourceJson;
import edu.ithaca.dragon.par.comm.CreateStudentAction;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.json.Response;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

public class ParControllerMockStressTests {
    private MockMvc mockMvcConcurrent;
    private DomainDatasource domainDatasource;
    private StudentModelDatasource studentModelDatasourceConcurrent;
    private ParController parControllerConcurrent;
    private Path cohortFileConcurrent;

    @BeforeEach
    public void initController(@TempDir Path tempDir) throws IOException{
        
        Path studentDirectoryConcurrent = tempDir.resolve("student");
        FileUtils.copyDirectory(new File("src/test/resources/rewrite/testServerData/concurrentStudent"),studentDirectoryConcurrent.toFile());
        this.cohortFileConcurrent = tempDir.resolve("currentConcurrentCohorts.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentConcurrentCohorts.json"),cohortFileConcurrent);

        Path questionPoolFile = tempDir.resolve("currentQuestionPool.json");
        Files.copy(Paths.get("src/test/resources/rewrite/testServerData/currentQuestionPool.json"),questionPoolFile);
        this.domainDatasource = new DomainDatasourceJson(
            "HorseUltrasound",
            questionPoolFile.toString(),
            "src/test/resources/rewrite/testServerData/currentQuestionPool.json",
            new JsonIoHelperDefault()
        );

        this.studentModelDatasourceConcurrent = new StudentModelDatasourceJson(
            "allTestConcurrentStudents",
            studentDirectoryConcurrent.toString(),
            new JsonIoHelperDefault()
            );

        this.parControllerConcurrent = new ParController(
            new ParServer(
                domainDatasource,

                studentModelDatasourceConcurrent,

                //TODO: remove default users from cohort datastores once there is a viable way to add students
                new CohortDatasourceJson(
                    "allCohorts",
                    cohortFileConcurrent.toString(),
                    "src/test/resources/rewrite/testServerData/currentConcurrentCohorts.json",
                    new JsonIoHelperDefault()
                )
            
            )
        );

        this.mockMvcConcurrent = MockMvcBuilders.standaloneSetup(this.parControllerConcurrent).build();
    }

    @Test
    public void stressAddNewUserForUserTestSameQuestion() throws JsonProcessingException, Exception{
        
        int numThreads = 10;
        int numRunIter = 1000;
        List<Thread> actionsForStudentThreads = new ArrayList<>(numThreads);
        String studentId = "stressTestStudent";
        // creates a single user
        mockMvcConcurrent.perform(post("/api2/addNewUser")
        .content(new ObjectMapper().writeValueAsString(new CreateStudentAction(studentId,"inOrder")))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        StudentModelJson studentModel = (StudentModelJson) studentModelDatasourceConcurrent.getStudentModel(studentId);
        // creates threads where each one adds a time seen and a response
        for (int i=0;i<numThreads;i++){
            Thread actionsThread = new Thread(new TimeSeenAddResponseWorker("614-plane-./images/3CTransverse.jpg", studentModel.studentId, mockMvcConcurrent,numRunIter));
            actionsForStudentThreads.add(actionsThread);
            actionsThread.start();
        }

        long startTime = System.nanoTime();
        for (Thread thread : actionsForStudentThreads) {
            try {
                thread.join();
                System.out.println(thread.getName()+" finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
        long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;
		
		System.out.println("Finished in "+duration);
        List<String> studentModelQuestionResponses = studentModel.getQuestionHistories().get("614-plane-./images/3CTransverse.jpg").responses.stream().map(r -> r.getResponseText()).collect(Collectors.toList());
        int expectedSize = numThreads*numRunIter;
        assertThat(studentModelQuestionResponses.size()).isEqualTo(expectedSize);
         for(int i=0;i<expectedSize;i++){
            String expectedText = "response"+i%numRunIter;
            assertThat(studentModelQuestionResponses.stream().filter(rText -> rText.equalsIgnoreCase(expectedText)).collect(Collectors.toList()).size()).isEqualTo(numThreads);
        }

    }

    @Test
    public void stressAddTimeSeenAddResponseForUserTestRandomQuestions() throws JsonProcessingException, Exception{
        int numActionsThreads = 20;
        int numRunIter = 1000;
        List<Thread> actionsForStudentThreads = new ArrayList<>(numActionsThreads);
        // creates a single user
        String studentId = "stressTestStudent";
        mockMvcConcurrent.perform(post("/api2/addNewUser")
        .content(new ObjectMapper().writeValueAsString(new CreateStudentAction(studentId,"random")))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        StudentModelJson studentModel = (StudentModelJson) studentModelDatasourceConcurrent.getStudentModel(studentId);
        Random r = new Random(42);
        // generates threads where each one should be adding a response and a time seen 
        for (int i=0;i<numActionsThreads;i++){
            List<String> domainQuestionIds = domainDatasource.getAllQuestions().stream().map(q->q.getId()).collect(Collectors.toList());
            String questionId = domainQuestionIds.get(r.nextInt(domainQuestionIds.size()));
            Thread actionsThread = new Thread(new TimeSeenAddResponseWorker(questionId, studentId,mockMvcConcurrent,numRunIter));
            actionsForStudentThreads.add(actionsThread);
            actionsThread.start();
        }

        long startTime = System.nanoTime();
        for (Thread thread : actionsForStudentThreads) {
            try {
                thread.join();
                System.out.println(thread.getName()+" finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		
        long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;
		
		System.out.println("Finished in "+duration);
        int numResponses = studentModel.getQuestionHistories().values().stream().map(qh -> qh.responses.size()).reduce(0,(partialSum,responseCount) -> partialSum + responseCount);
        int numTimesSeen = studentModel.getQuestionHistories().values().stream().map(qh -> qh.timesSeen.size()).reduce(0,(partialSum,timesSeenCount) -> partialSum + timesSeenCount);
        int expectedSize = numActionsThreads*numRunIter;
        assertThat(numResponses).isEqualTo(expectedSize);
        assertThat(numTimesSeen).isEqualTo(expectedSize);
        List <Response> responses = new ArrayList<>();
        studentModel.getQuestionHistories().values().stream().map(qh -> qh.responses).collect(Collectors.toList()).forEach(responses::addAll);
        for(int i=0;i<numRunIter;i++){
            String responseText = "response"+i;
            int numResponsesPerIterPerRun = responses.stream().filter(response -> response.getResponseText().equalsIgnoreCase(responseText)).collect(Collectors.toList()).size();
            assertThat(numResponsesPerIterPerRun).isEqualTo(numActionsThreads);
        }
        System.out.println("\nend test");
    }

    @Test
    public void stressUsersForAddNewUserSameCohortTest() throws Exception{
        int numUserThreads = 10;
        int numRunIter = 100;
        List<Thread> studentsForActionThreads = new ArrayList<>(numUserThreads);
        // users created by each thread
        for (int i=0;i<numUserThreads;i++){
            Thread userThread = new Thread(new AddNewUsersWorker("random", mockMvcConcurrent, i, numRunIter));
            studentsForActionThreads.add(userThread);
            userThread.start();
        }

        long startTime = System.nanoTime();
        for (Thread thread : studentsForActionThreads) {
            try {
                thread.join();
                System.out.println(thread.getName()+" finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000;
		
		System.out.println("Finished in "+duration);
        Map <String,Cohort> cohortMap = new JsonIoUtil(new JsonIoHelperDefault()).mapfromReadOnlyFile(cohortFileConcurrent.toString(), Cohort.class);
        List <String> studentIds = new ArrayList<>();
        for ( Cohort cohort : cohortMap.values()) {
            studentIds.addAll(cohort.getStudentIds());
        }
        assertThat(studentIds.size()).isEqualTo(numUserThreads*numRunIter);
        for (String studentId : studentIds) {
            MvcResult invalidUserId = this.mockMvcConcurrent.perform(get("/api2/isUserIdAvailable?idToCheck="+studentId)).andExpect(status().isOk()).andReturn();
            assertFalse(Boolean.valueOf(invalidUserId.getResponse().getContentAsString()));
        }
    }
}
