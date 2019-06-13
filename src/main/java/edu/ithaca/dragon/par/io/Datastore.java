package edu.ithaca.dragon.par.io;

import edu.ithaca.dragon.par.domain.Question;

import java.io.IOException;
import java.util.List;

public interface Datastore {

    List<Question> loadQuestions() throws IOException;

}
