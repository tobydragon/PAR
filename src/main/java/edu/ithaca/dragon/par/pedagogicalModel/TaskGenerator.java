package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskGenerator {

    /**
     * Generates an ImageTask with a single Question
     * Adds unseen Questions before adding seen Questions
     * @param studentModel
     * @return
     */
    public static ImageTask makeTaskWithSingleQuestion(StudentModel studentModel){

        //there are questions the student has not seen
        if(studentModel.getUnseenQuestionCount()>0){
            //make and return an imageTask with the first question from the studentModels.unseenQuestions
            Question firstQuestionFromUnseen = studentModel.getUserQuestionSet().getUnseenQuestions().get(0);
            ImageTask imageTask = new ImageTask(firstQuestionFromUnseen.getImageUrl(), Arrays.asList(firstQuestionFromUnseen));

            //let userQuestionSet know the question has been seen
            studentModel.getUserQuestionSet().givenQuestion(firstQuestionFromUnseen.getId());

            return imageTask;
        }

        //the student has seen every question, get a question that they have already seen
        //TODO: because this takes the first question from seenQuestions, will it always take the same question? Maybe this should be random to avoid repetition?
        Question firstQuestionFromSeen = studentModel.getUserQuestionSet().getSeenQuestions().get(0);
        ImageTask imageTask = new ImageTask(firstQuestionFromSeen.getImageUrl(), Arrays.asList(firstQuestionFromSeen));

        return imageTask;
    }

    /**
     * Generates a Task with Questions that share the same URL
     * Currently includes questions that may have duplicate types
     * @param studentModel
     * @return
     */
    public static ImageTask makeTask(StudentModel studentModel){
        //create an ImageTask with a single Question
        ImageTask imageTask = TaskGenerator.makeTaskWithSingleQuestion(studentModel);

        //add unseenQuestions and seenQuestions
        List<Question> unseenQuestionsWithCorrectUrl = TaskGenerator.getQuestionsWithUrl(studentModel.getUserQuestionSet().getUnseenQuestions(), imageTask.getImageUrl());
        List<Question> seenQuestionsWithCorrectUrl = TaskGenerator.getQuestionsWithUrl(studentModel.getUserQuestionSet().getSeenQuestions(), imageTask.getImageUrl());
        addAllQuestions(imageTask, unseenQuestionsWithCorrectUrl, seenQuestionsWithCorrectUrl);

        //let studentModel know that unseen questions are seen
        for(Question currUnseenQuestion : unseenQuestionsWithCorrectUrl){
            studentModel.getUserQuestionSet().givenQuestion(currUnseenQuestion.getId());
        }

        return imageTask;
    }

    /**
     * Creates a list of questions that have the questionUrl out of questionList
     */
    public static List<Question> getQuestionsWithUrl(List<Question> questionList, String questionUrl){
        List<Question> toReturn = new ArrayList<>();
        for(Question questionToCheck : questionList){
            if(questionToCheck.getImageUrl().equals(questionUrl))
                toReturn.add(questionToCheck);
        }
        return toReturn;
    }

    public static void addAllQuestions(ImageTask it, List<Question> sameUrlListUnseen, List<Question> sameUrlListSeen){
        for (int i = 0; i<sameUrlListUnseen.size(); i++){
            if (!sameUrlListUnseen.get(i).getId().equals(it.getTaskQuestions().get(0).getId())) {
                it.addQuestion(sameUrlListUnseen.get(i));
            }
        }
        for (int i = 0; i<sameUrlListSeen.size(); i++){
            if (!sameUrlListSeen.get(i).getId().equals(it.getTaskQuestions().get(0).getId())) {
                it.addQuestion(sameUrlListSeen.get(i));
            }
        }
    }
}
