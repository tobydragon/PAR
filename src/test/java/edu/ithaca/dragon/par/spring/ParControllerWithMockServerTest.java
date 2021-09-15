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
import edu.ithaca.dragon.par.comm.StudentAction;
import edu.ithaca.dragon.par.comm.StudentResponseAction;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.DomainDatasourceJson;
import edu.ithaca.dragon.par.pedagogy.QuestionChooserInOrder;
import edu.ithaca.dragon.par.student.StudentModelDatasource;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.QuestionHistory;
import edu.ithaca.dragon.par.student.json.Response;
import edu.ithaca.dragon.par.student.json.StudentModelDatasourceJson;
import edu.ithaca.dragon.par.student.json.StudentModelJson;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;




public class ParControllerWithMockServerTest {
    
    private MockMvc mockMvc;

    private ParController parController;

    private Path cohortFile;

    private DomainDatasource domainDatasource;

    private StudentModelDatasource studentModelDatasource;


    @BeforeEach
    public void initController(@TempDir Path tempDir) throws IOException{
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

        this.studentModelDatasource = new StudentModelDatasourceJson(
            "allTestStudents",
            studentDirectory.toString(),
            new JsonIoHelperDefault()
            );

        this.parController = new ParController(
            new ParServer(
                domainDatasource,

                studentModelDatasource,

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

    @Test
    public void isUserIdAvailableTest() throws Exception{
        MvcResult validUserId = this.mockMvc.perform(get("/api2/isUserIdAvailable?idToCheck=boc1")).andExpect(status().isOk()).andReturn();
        MvcResult invalidUserId = this.mockMvc.perform(get("/api2/isUserIdAvailable?idToCheck=o1")).andExpect(status().isOk()).andReturn();
        assertFalse(Boolean.valueOf(invalidUserId.getResponse().getContentAsString()));
        assertTrue(Boolean.valueOf(validUserId.getResponse().getContentAsString()));
    }

    @Test
    public void getCohortIdsTest() throws Exception{
        MvcResult result = this.mockMvc.perform(get("/api2/getCohortIds")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString().contains("inOrder")).isTrue();
        assertThat(result.getResponse().getContentAsString().contains("byOutOfOrderConcepts")).isFalse();
    }

    @Test
    public void getCurrentQuestionTest(@TempDir Path tempDir) throws Exception{
        
        this.mockMvc.perform(get("/api2/getCurrentQuestion?userId=bocTest"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id",Matchers.is("614-plane-./images/3CTransverse.jpg")))
            .andExpect(jsonPath("$.type",Matchers.is("plane")));
        
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

        this.mockMvc = MockMvcBuilders.standaloneSetup(parController220).build();
        
        this.mockMvc.perform(get("/api2/getCurrentQuestion?userId=comp220Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id",Matchers.is("QueueQ1")))
        .andExpect(jsonPath("$.type",Matchers.is("Queue")))
        .andExpect(jsonPath("$.followupQuestions", Matchers.hasSize(1)));
    

        this.mockMvc.perform(get("/api2/getCurrentQuestion?userId=comp220Test1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id",Matchers.is("QueueQ1")))
        .andExpect(jsonPath("$.type",Matchers.is("Queue")))
        .andExpect(jsonPath("$.followupQuestions", Matchers.hasSize(0)));

    }

    @Test
    public void getQuestionHistorySummaryTest() throws Exception{
        this.mockMvc.perform(get("/api2/getQuestionHistorySummary?userId=o1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.questionIdsSeen", Matchers.hasSize(4)))
        .andExpect(jsonPath("$.questionIdsRespondedTo", Matchers.hasSize(3)))
        .andExpect(jsonPath("$.questionIdsIncorrect", Matchers.hasItem("850-structure3-./images/Annotated2Long.jpg")));
    }

    @Test
    public void addNewUserTest() throws Exception{
        
        // assert that o5 doesn't exist beforehand
        Map <String,Cohort> cohortMapBefore = new JsonIoUtil(new JsonIoHelperDefault()).mapfromReadOnlyFile("src/test/resources/rewrite/testServerData/currentCohortsNoBoc.json", Cohort.class);
        assertThat(!cohortMapBefore.get("inOrder").studentIds.contains("o5"));

        MvcResult o5DidNotExistResult = this.mockMvc.perform(
            post("/api2/addNewUser")
            .content(new ObjectMapper().writeValueAsString(new CreateStudentAction("o5","inOrder")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertTrue(Boolean.valueOf(o5DidNotExistResult.getResponse().getContentAsString()));
        Map <String,Cohort> cohortMapAfter = new JsonIoUtil(new JsonIoHelperDefault()).mapfromReadOnlyFile("src/test/resources/rewrite/testServerData/currentCohortsNoBoc.json", Cohort.class);
        assertThat(cohortMapAfter.get("inOrder").studentIds.contains("o5"));
        StudentModelJson o5StudentModel = (StudentModelJson) studentModelDatasource.getStudentModel("o5");
        List<String> domainQuestionIds = domainDatasource.getAllQuestions().stream().map(q -> q.getId()).collect(Collectors.toList());
        assertThat(new QuestionChooserInOrder(domainQuestionIds).chooseQuestion(o5StudentModel, domainDatasource).getId()).isEqualTo("614-plane-./images/3CTransverse.jpg");
        assertThat(new QuestionChooserInOrder(domainQuestionIds).chooseQuestion(o5StudentModel, domainDatasource).getType()).isEqualTo("plane");

        o5StudentModel.addTimeSeen("614-plane-./images/3CTransverse.jpg");
        o5StudentModel.addResponse("614-plane-./images/3CTransverse.jpg","question responded to");
        assertThat(o5StudentModel.getQuestionHistories().keySet().size()).isEqualTo(1);
        MvcResult o5ExistsResult = this.mockMvc.perform(
            post("/api2/addNewUser")
            .content(new ObjectMapper().writeValueAsString(new CreateStudentAction("o5","inOrder")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertFalse(Boolean.valueOf(o5ExistsResult.getResponse().getContentAsString()));
        assertThat(o5StudentModel.getQuestionHistories().keySet().size()).isEqualTo(1);
    }

    @Test
    public void addResponseTest() throws JsonProcessingException, Exception{
        MvcResult invalidResponseResult = this.mockMvc.perform(
            post("/api2/addResponse")
            .content(new ObjectMapper().writeValueAsString(new StudentResponseAction("o7", "850-structure3-./images/Annotated2Long.jpg", "response1")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        assertFalse(Boolean.valueOf(invalidResponseResult.getResponse().getContentAsString()));

        MvcResult validResponseResult = this.mockMvc.perform(
            post("/api2/addResponse")
            .content(new ObjectMapper().writeValueAsString(new StudentResponseAction("o1", "850-structure3-./images/Annotated2Long.jpg", "response2")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        assertTrue(Boolean.valueOf(validResponseResult.getResponse().getContentAsString()));
    }

    @Test
    public void addTimeSeenTest() throws JsonProcessingException, Exception{
        MvcResult invalidTimeSeenResult = this.mockMvc.perform(
            post("/api2/addTimeSeen")
            .content(new ObjectMapper().writeValueAsString(new StudentAction("o7", "850-structure3-./images/Annotated2Long.jpg")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
            
        assertFalse(Boolean.valueOf(invalidTimeSeenResult.getResponse().getContentAsString()));

        MvcResult validTimeSeenResult = this.mockMvc.perform(
            post("/api2/addTimeSeen")
            .content(new ObjectMapper().writeValueAsString(new StudentAction("o1", "850-structure3-./images/Annotated2Long.jpg")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
            
        assertTrue(Boolean.valueOf(validTimeSeenResult.getResponse().getContentAsString()));

    }

}
