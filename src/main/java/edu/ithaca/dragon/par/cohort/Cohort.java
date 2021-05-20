package edu.ithaca.dragon.par.cohort;

import edu.ithaca.dragon.par.pedagogicalmodel2.QuestionChooser;

import java.util.Set;

public class Cohort{
    public String id;
    public String domainId;
    public Set<String> studentIds;
    public QuestionChooser questionChooser;

    public Cohort(){}

    public Cohort(String id, String domainId, Set<String> studentIds, QuestionChooser questionChooser) {
        this.id = id;
        this.domainId = domainId;
        this.studentIds = studentIds;
        this.questionChooser = questionChooser;
    }

    boolean containsStudent(String studentId){
        return studentIds.contains(studentId);
    }

    QuestionChooser getQuestionChooser(){
        return questionChooser;
    }
}
