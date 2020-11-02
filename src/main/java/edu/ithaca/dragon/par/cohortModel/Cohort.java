package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.OrderedTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;

public class Cohort {
    private final TaskGenerator taskGenerator;
    private final List<String> studentIDs;
    private final MessageGenerator messageGenerator;
    private final QuestionPool questionPool;
    private final String questionOrderedInfoFilename;

    public Cohort(TaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool){
        this.taskGenerator = taskGenerator;
        this.studentIDs = studentIDs;
        this.messageGenerator = messageGenerator;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = null;
    }

    // this constructor should only be used for OrderedTaskGenerator; questionOrderedInfoFilename is not used by other TaskGenerators
    public Cohort(OrderedTaskGenerator taskGenerator, List<String> studentIDs, MessageGenerator messageGenerator, QuestionPool questionPool, String questionOrderedInfoFilename){
        this.taskGenerator = taskGenerator;
        this.studentIDs = studentIDs;
        this.messageGenerator = messageGenerator;
        this.questionPool = questionPool;
        this.questionOrderedInfoFilename = questionOrderedInfoFilename;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public TaskGenerator getTaskGenerator() {
        return taskGenerator;
    }

    public MessageGenerator getMessageGenerator(){ return messageGenerator; }

    public QuestionPool getQuestionPool(){ return questionPool; }

    public String getQuestionOrderedInfoFilename() {
        return questionOrderedInfoFilename;
    }

    public void addStudent(String studentID) {
        if(!studentIDs.contains(studentID)){
            studentIDs.add(studentID);
        }
    }

}
