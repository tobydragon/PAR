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
	public void nextImageTask() throws Exception{
		//if this test fails, the nextQuestion mapping has changed. Update this test to reflect that
		mvc.perform(MockMvcRequestBuilders.get("/api/nextImageTask").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"imageUrl\":\"../static/images/equine02.jpg\\\"\",\"taskQuestions\":[{\"id\":\"PlaneQ1\",\"difficulty\":1,\"questionText\":\"On which plane is the ultrasound taken?\",\"correctAnswer\":\"Lateral\",\"possibleAnswers\":[\"Transverse\",\"Lateral\",\"I don't know\"],\"imageUrl\":\"../static/images/demoEquine02.jpg\"},{\"id\":\"StructureQ1\",\"difficulty\":2,\"questionText\":\"What structure is in the near field?\",\"correctAnswer\":\"bone\",\"possibleAnswers\":[\"bone\",\"ligament\",\"tumor\",\"tendon\",\"I don't know\"],\"imageUrl\":\"../static/images/demoEquine04.jpg\"},{\"id\":\"ZoneQ1\",\"difficulty\":4,\"questionText\":\"In what zone is this ultrasound taken?\",\"correctAnswer\":\"3c\",\"possibleAnswers\":[\"1a\",\"1b\",\"2a\",\"2b\",\"2c\",\"3a\",\"3b\",\"3c\"],\"imageUrl\":\"../static/images/demoEquine02.jpg\"}]}")));
	}

}
