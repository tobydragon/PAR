package edu.ithaca.dragon.par.domainmodel2;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class DomainDatasourceJson implements  DomainDatasource{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private JsonIoUtil jsonIoUtil;

    private String questionFilePath;
    private List<Question> questions;

    public DomainDatasourceJson(String questionFilePath) throws IOException {
        this(questionFilePath, new JsonIoHelperDefault());
    }

    public DomainDatasourceJson(String questionFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this(questionFilePath, null, jsonIoHelper);
    }

    public DomainDatasourceJson(String questionFilePath, String defaultQuestionReadOnlyFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        this.questionFilePath = questionFilePath;
        if (defaultQuestionReadOnlyFilePath != null){
            questions = jsonIoUtil.listFromFileOrCopyFromReadOnlyFile(questionFilePath, defaultQuestionReadOnlyFilePath, Question.class);
        }
        else {
            questions = jsonIoUtil.listFromFile(questionFilePath, Question.class);
        }
    }

    @Override
    public List<Question> getAllQuestions() {
        return questions;
    }
}
