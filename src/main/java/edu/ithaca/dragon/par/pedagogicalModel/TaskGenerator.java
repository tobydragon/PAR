package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TaskGenerator {

    /**
     * Generates an ImageTask with a single Question
     * Adds unseen Questions before adding seen Questions
     * @param studentModel
     * @return
     */
    public static Question getInitialQuestionForTask(StudentModel studentModel, int level){
        checkStudentModel(studentModel);

        switch(level){
            case 1:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("plane")){
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "plane");
                }

                break;
            case 2:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("plane")){
                            return unseen.get(i);
                        }
                    }
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("structure")){
                            return unseen.get(i);
                        }
                    }
                    return getLeastSeenQuestion(studentModel, "plane");
                }

                break;
            case 3:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("structure")){
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "structure");
                }

                break;
            case 4:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("structure")){
                            return unseen.get(i);
                        }
                    }
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("attachment")){
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "structure");
                }

                break;

            case 5:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("structure")){
                            return unseen.get(i);
                        }
                    }
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("attachment")){
                            return unseen.get(i);
                        }
                    }
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("zone")){
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "structure");
                }

                break;
            case 6:
                if(studentModel.getUnseenQuestionCount()>0){
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getUnseenQuestions();
                    for (int i = 0; i< unseen.size(); i++){
                        if(unseen.get(i).getType().equals("zone")){
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "zone");
                }


                break;
            default:
                throw new RuntimeException("Level "+level+" is not valid");
        }

        throw new RuntimeException("Level "+level+" is not valid");
    }

    /**
     * Generates a Task with Questions that share the same URL
     * Currently includes questions that may have duplicate types
     * @param studentModel
     * @return
     */
    public static ImageTask makeTask(StudentModel studentModel){
        checkStudentModel(studentModel);

        int level = StudentModel.calcLevel(studentModel.knowledgeScoreByType());

        Question initialQuestion = TaskGenerator.getInitialQuestionForTask(studentModel, level);
        List<Question> questionList = TaskGenerator.addAllQuestions(studentModel, initialQuestion);
        questionList = TaskGenerator.filterQuestions(level, questionList);

        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        //let studentModel know that unseen questions are seen
        for(Question currQuestion : questionList){
            studentModel.getUserQuestionSet().givenQuestion(currQuestion.getId());
        }
        return imageTask;
    }

    public static List<Question> filterQuestions(int level, List<Question> questionList){
        //only levels 1 and 2 have plane questions
        if(level==3 || level==4 || level==5 || level==6){
            questionList = removeType(questionList, "plane");
        }

        //only levels 2, 3, 4, and 5 have structure questions
        if(level==1 || level==6){
            questionList = removeType(questionList, "structure");
        }

        //only levels 4 and 5 have attachment questions
        if(level==1 || level==2 || level==3 || level==6){
            questionList = removeType(questionList, "attachment");
        }

        //only levels 5 and 6 have zone questions
        if(level==1 || level==2 || level==3 || level==4){
            questionList = removeType(questionList, "zone");
        }
        return questionList;
    }

    public static List<Question> removeType(List<Question> questions, String type){
        List<Question> newList = new ArrayList<Question>();
        for(Question currQuestion: questions){
            if(!currQuestion.getType().equals(type)){
                newList.add(currQuestion);
            }
        }
        return newList;
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

    public static List<Question> addAllQuestions(StudentModel studentModel, Question initialQuestion){
        //put initialQuestion, unseenQuestions and seenQuestions all in a list
        List<Question> unseenQuestionsWithCorrectUrl = TaskGenerator.getQuestionsWithUrl(studentModel.getUserQuestionSet().getUnseenQuestions(), initialQuestion.getImageUrl());
        List<Question> seenQuestionsWithCorrectUrl = TaskGenerator.getQuestionsWithUrl(studentModel.getUserQuestionSet().getSeenQuestions(), initialQuestion.getImageUrl());
        List<Question> questionList = new ArrayList<>();
        questionList.addAll(unseenQuestionsWithCorrectUrl);
        questionList.addAll(seenQuestionsWithCorrectUrl);

        return questionList;
    }

    public static void checkStudentModel(StudentModel studentModel){
        if(studentModel.getUnseenQuestionCount() == 0 && studentModel.getSeenQuestionCount() == 0){
            throw new RuntimeException("The studentModel has no questions");
        }
    }

    public static Question getLeastSeenQuestion(StudentModel studentModel, String type){
        //TODO check if there are unseen questions?

        List<Question> seenQuestions = studentModel.getUserQuestionSet().getSeenQuestions();

        if(seenQuestions.size()==0){
            throw new RuntimeException("The studentModel has no questions");
        }

        int index = 0;
        for(int i = 0; i<seenQuestions.size(); i++){
            if( (studentModel.getUserQuestionSet().getSeenQuestions().get(i).getType().equals(type)) && (studentModel.getUserQuestionSet().getTimesSeen(seenQuestions.get(i).getId()) < studentModel.getUserQuestionSet().getTimesSeen(seenQuestions.get(index).getId()))){
                index = i;
            }
        }
        return seenQuestions.get(index);
    }

}
