//package edu.ithaca.dragon.par.spring;
//
//
//import static org.hamcrest.Matchers.equalTo;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ParSpringApplicationTest {
//
//	@Autowired
//	private MockMvc mvc;
//	@Test
//	public void getDefaultApiResponseTest() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/api/").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().string(equalTo("Greetings from PAR API!")));
//	}
//
//	//These tests rely on the working data set, not the test dataset, which we can't guratentee anything about
//	//for intsance, this test assumes there are student questions, but in our server there may be no authored questions
//	//we need to be able to send a test datastore if we want these tests to be meaningful
//	@Test
//	public void apiFurtherTesting() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/api/nextImageTask?userId=Student").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("application/json;charset=UTF-8"));
//
//
//		mvc.perform(MockMvcRequestBuilders.get("/api/calcScore?userId=Student").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("application/json;charset=UTF-8"));
//
//		mvc.perform(MockMvcRequestBuilders.get("/api/getPageSettings?userId=Student").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("application/json;charset=UTF-8"));
//
//		mvc.perform(MockMvcRequestBuilders.get("/api/getImageTaskSettings?userId=Student").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("application/json;charset=UTF-8"));
//	}
//
//	@Test
//	public void webControllerTest() throws Exception {
//		mvc.perform(MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("text/html;charset=UTF-8"));
//
//		mvc.perform(MockMvcRequestBuilders.get("/imageTaskView?userId=Student").accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("text/html;charset=UTF-8"));
//	}
//
//}
