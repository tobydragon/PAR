package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class JsonDatastore implements Datastore{

    String filePath;

    public JsonDatastore(String filePath) throws IOException{
        this.filePath = filePath;
    }

    @Override
    public java.util.List<Question> loadQuestions() throws IOException {
        return JsonUtil.listFromJsonFile(filePath, Question.class);
    }
}
