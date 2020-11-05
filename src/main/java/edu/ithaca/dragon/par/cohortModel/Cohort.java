package edu.ithaca.dragon.par.cohortModel;

import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.pedagogicalModel.MessageGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.OrderedTaskGenerator;
import edu.ithaca.dragon.par.pedagogicalModel.TaskGenerator;

import java.util.List;
import java.util.Objects;

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

    // NOTE: MESSAGE GENERATORS DO NOT HAVE CONSTRUCTORS SO TYPES MUST BE EQUAL
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cohort)) return false;
        Cohort cohort = (Cohort) o;
        return Objects.equals(taskGenerator, cohort.taskGenerator) &&
                Objects.equals(studentIDs, cohort.studentIDs) &&
                Objects.equals(messageGenerator, cohort.messageGenerator) &&
                Objects.equals(questionPool, cohort.questionPool) &&
                Objects.equals(questionOrderedInfoFilename, cohort.questionOrderedInfoFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskGenerator, studentIDs, messageGenerator, questionPool, questionOrderedInfoFilename);
    }
}
