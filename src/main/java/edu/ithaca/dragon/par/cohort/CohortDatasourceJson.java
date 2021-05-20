package edu.ithaca.dragon.par.cohort;

import edu.ithaca.dragon.par.pedagogy.QuestionChooser;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class CohortDatasourceJson implements CohortDatasource{
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final String id;
    private final String filePath;
    private List<Cohort> cohorts;

    public CohortDatasourceJson(String id, String filePath) throws IOException {
        this(id, filePath, null, new JsonIoHelperDefault());
    }

    public CohortDatasourceJson(String id, String filePath, String defaultReadOnlyFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this.id = id;
        JsonIoUtil jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        this.filePath = filePath;
        if (defaultReadOnlyFilePath != null){
            cohorts = jsonIoUtil.listFromFileOrCopyFromReadOnlyFile(filePath, defaultReadOnlyFilePath, Cohort.class);
        }
        else {
            cohorts = jsonIoUtil.listFromFile(filePath, Cohort.class);
        }
        if (cohorts.size()<1){
            throw new IllegalArgumentException("FilePaths led to empty list: " + filePath + ", " + defaultReadOnlyFilePath);
        }
    }

    @Override
    public QuestionChooser getQuestionChooser(String studentId) {
        for (Cohort cohort: cohorts){
            if (cohort.containsStudent(studentId)){
                return cohort.getQuestionChooser();
            }
        }
        //first cohort is default
        logger.info("Student not in any cohort, using default");
        return cohorts.get(0).getQuestionChooser();
    }
}
