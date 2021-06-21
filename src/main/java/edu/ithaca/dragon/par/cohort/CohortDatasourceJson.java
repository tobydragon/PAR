package edu.ithaca.dragon.par.cohort;

import edu.ithaca.dragon.par.pedagogy.QuestionChooser;
import edu.ithaca.dragon.util.JsonIoHelper;
import edu.ithaca.dragon.util.JsonIoHelperDefault;
import edu.ithaca.dragon.util.JsonIoUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class CohortDatasourceJson implements CohortDatasource{
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final String id;
    private final String filePath;
    private Map<String, Cohort> cohorts;
    private List<String> cohortIds;
    private JsonIoUtil jsonIoUtil;

    public CohortDatasourceJson(String id, String filePath) throws IOException {
        this(id, filePath, null, new JsonIoHelperDefault());
    }

    public CohortDatasourceJson(String id, String filePath, String defaultReadOnlyFilePath, JsonIoHelper jsonIoHelper) throws IOException {
        this.id = id;
        this.filePath = filePath;
        jsonIoUtil = new JsonIoUtil(jsonIoHelper);
        
        if (defaultReadOnlyFilePath != null){
            cohorts = jsonIoUtil.mapFromFileOrCopyFromReadOnlyFile(filePath, defaultReadOnlyFilePath, Cohort.class);
        }
        else {
            cohorts = jsonIoUtil.mapFromFile(filePath, Cohort.class);
        }
        if (cohorts.size()<1){
            throw new IllegalArgumentException("FilePaths led to empty list: " + filePath + ", " + defaultReadOnlyFilePath);
        }
    }

    @Override
    public QuestionChooser getQuestionChooser(String studentId) {
        for (Cohort cohort: cohorts.values()){
            if (cohort.containsStudent(studentId)){
                return cohort.getQuestionChooser();
            }
        }
        //first cohort is default
        logger.info("Student not in any cohort, using default");
        return cohorts.values().iterator().next().getQuestionChooser();
    }

    @Override
    public List<String> getCohortIds() {
        cohortIds = new ArrayList<String>();
        for (Cohort cohort: cohorts.values()){
            cohortIds.add(cohort.id);
        }
        return cohortIds;
    }

    @Override
    public void addStudentToCohort(String cohortId, String studentId) {
        if (cohorts.get(cohortId).getStudentIds().contains(studentId)){
            throw new IllegalArgumentException("Student id already exists: " + studentId);
        }
        else{
            cohorts.get(cohortId).addStudentId(studentId);
            overwriteFile();
        }
    }

    public Collection<String> getStudentIdsForCohort(String cohortId){
        if (cohorts.get(cohortId).getId().equals(cohortId)){
            return cohorts.get(cohortId).getStudentIds();
        }
        else{
            throw new IllegalArgumentException("No cohort found for " + cohortId);
        }
    }

    public void overwriteFile(){
        try{
            jsonIoUtil.toFile(filePath, cohorts);
        }
        catch(IOException e){
            throw new RuntimeException("Unable to write to file");
        }


    }
}
