package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonUtil;

import java.io.IOException;

public class JsonDatastore implements Datastore{

    String filePath;

    public JsonDatastore(String filePath) {
        this.filePath = filePath;

    }

    @Override
    public java.util.List<Question> loadQuestions() throws IOException {
        return JsonUtil.listFromJsonFile(filePath, Question.class);
    }
}
