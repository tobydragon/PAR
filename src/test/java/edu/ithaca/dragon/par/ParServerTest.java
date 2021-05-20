package edu.ithaca.dragon.par;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainmodel2.DomainDatasourceJson;
import edu.ithaca.dragon.par.pedagogicalmodel2.QuestionChooserRandom;
import edu.ithaca.dragon.par.studentmodel2.inmemory.StudentModelDatasourceInMemoryExample;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ParServerTest {

    @Test
    public void basicSystemTest() throws IOException {
//        ParServer server = new ParServer(
//                new DomainDatasourceJson("HorseUltrasound", "src/test/resources/author/questionPools/SampleQuestionPool.json"),
//                StudentModelDatasourceInMemoryExample.createExample(),
//                new QuestionChooserRandom()
//        );
//        for (int i=0; i< 500; i++) {
//            Question question = server.getCurrentQuestion("Homer");
//            server.addTimeSeen("Homer", question.getId());
//            server.addResponse("Homer", question.getId(), question.getPossibleAnswers().get(0));
//            server.addResponse("Homer", question.getId(), question.getCorrectAnswer());
//        }
//
//        System.out.println(server);

    }
}
