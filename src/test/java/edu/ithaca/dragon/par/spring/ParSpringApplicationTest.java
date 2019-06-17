package edu.ithaca.dragon.par.spring;


import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParSpringApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getDefaultApiResponseTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Greetings from PAR API!")));
	}

	@Test
	public void nextQuestionTest() throws Exception{
		//if this test fails, the nextQuestion mapping has changed. Update this test to reflect that
		mvc.perform(MockMvcRequestBuilders.get("/api/nextQuestion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"id\":\"PlaneQ1\",\"difficulty\":1,\"questionText\":\"On which plane is the ultrasound taken?\",\"correctAnswer\":\"Lateral\",\"possibleAnswers\":[\"Transverse\",\"Lateral\",\"I don't know\"],\"imageUrl\":\"../static/images/demoEquine02.jpg\"}")));
	}

}
