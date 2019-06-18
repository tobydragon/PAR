package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
//QUESTION POOL,USERMODEL, ITR
public class ParServer {
    private  Logger logger = LogManager.getLogger(this.getClass());

    public Question nextQuestion() {
        try {
            List<Question> questionsFromFile = JsonUtil.listFromJsonFile("src/test/resources/author/SampleQuestions.json", Question.class);
            return questionsFromFile.get(0);
        }
        catch (IOException e){
            logger.error("sample questions not found", e);
            return null;
        }
    }
}
