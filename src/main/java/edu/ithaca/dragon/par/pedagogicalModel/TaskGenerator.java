package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
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

            if (level == 1) {
                if (studentModel.getUnseenQuestionCount() > 0) {
                    //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                    List<Question> unseen = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions();
                    for (int i = 0; i < unseen.size(); i++) {
                        if (unseen.get(i).getType().equals("plane")) {
                            return unseen.get(i);
                        }
                    }

                    return getLeastSeenQuestion(studentModel, "plane");
                }
            }
        else if (level == 2) {
            if (studentModel.getUnseenQuestionCount() > 0) {
                //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                List<Question> unseen = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions();
                for (int i = 0; i < unseen.size(); i++) {
                    if (unseen.get(i).getType().equals("plane")) {
                        return unseen.get(i);
                    }
                }
                for (int i = 0; i < unseen.size(); i++) {
                    if (unseen.get(i).getType().equals("structure")) {
                        return unseen.get(i);
                    }
                }
                return getLeastSeenQuestion(studentModel, "plane");
            }
        }
        else if (level == 3) {
            if (studentModel.getUnseenQuestionCount() > 0) {
                //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                List<Question> unseen = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions();
                for (int i = 0; i < unseen.size(); i++) {
                    if (unseen.get(i).getType().equals("structure")) {
                        return unseen.get(i);
                    }
                }

                return getLeastSeenQuestion(studentModel, "structure");
            }
        }

        else if (level == 4 || level == 5 || level==6 ) {
            if (studentModel.getUnseenQuestionCount() > 0) {
                //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                List<Question> unseen = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions();
                for (int i = 0; i < unseen.size(); i++) {
                    if (unseen.get(i).getType().equals("structure")) {
                        if (unseen.get(i).getFollowupQuestions().size() > 0) {
                            return unseen.get(i);
                        }
                    }
                }

                return getLeastSeenQuestion(studentModel, "structure");
            }
        }
        else if (level == 7) {
            if (studentModel.getUnseenQuestionCount() > 0) {
                //make and return an imageTask with the first question from the studentModels.unseenQuestions that matches the level
                List<Question> unseen = studentModel.getUserQuestionSet().getTopLevelUnseenQuestions();
                for (int i = 0; i < unseen.size(); i++) {
                    if (unseen.get(i).getType().equals("zone")) {
                        return unseen.get(i);
                    }
                }
                return getLeastSeenQuestion(studentModel, "zone");
            }
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
        return makeTask(studentModel, StudentModel.calcLevel(studentModel.knowledgeScoreByType()));
    }

    public static ImageTask makeTask(StudentModel studentModel, int level){
        checkStudentModel(studentModel);

        Question initialQuestion = TaskGenerator.getInitialQuestionForTask(studentModel, level);
        List<Question> questionList = TaskGenerator.addAllQuestions(studentModel, initialQuestion);
        questionList = TaskGenerator.filterQuestions(level, questionList);

        ImageTask imageTask = new ImageTask(initialQuestion.getImageUrl(), questionList);

        //let studentModel know that unseen questions are seen
        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);
        return imageTask;
    }


    public static List<Question> filterQuestions(int level, List<Question> questionList){
        //only levels 1 and 2 have plane questions
        if(level==3 || level==4 || level==5 || level==6 || level==7){
            questionList = removeTypeFromQuestionList(questionList, "plane");
        }

        //only levels 2, 3, 4, 5, and 6 have structure questions
        if(level==1 || level==7){
            questionList = removeTypeFromQuestionList(questionList, "structure");
        }

        //only levels 4, 5, and 6 have attachment questions
        if(level==1 || level==2 || level==3 || level==7){
            questionList = removeTypeFromQuestionList(questionList, "attachment");
        }

        //only levels 6 and 7 have zone questions
        if(level==1 || level==2 || level==3 || level==4 || level==5){
            questionList = removeTypeFromQuestionList(questionList, "zone");
        }
        return questionList;
    }

    public static List<Question> removeTypeFromQuestionList(List<Question> questions, String type){
        List<Question> newList = new ArrayList<>();
        for(Question currQuestion: questions){
            if(!currQuestion.getType().equals(type)){
                Question cleanQuestion = removeTypeFromQuestion(currQuestion, type);
                newList.add(cleanQuestion);
            }
        }
        return newList;
    }

    public static Question removeTypeFromQuestion(Question question, String type){
        if(!question.getType().equals(type)) {
            if (question.getFollowupQuestions().size() != 0){
                List<Question> cleanFollowups = new ArrayList<>();
                for (Question followupQuestion : question.getFollowupQuestions()){
                    if (!followupQuestion.getType().equals(type)) {
                        Question cleanFollowUp = removeTypeFromQuestion(followupQuestion, type);
                        cleanFollowups.add(cleanFollowUp);
                    }
                }
                return new Question(question, cleanFollowups);
            }
            return question;
        }
        else{
            throw new RuntimeException("root question matches type, cannot remove itself");
        }
    }

    /**
     * Creates a list of questions that have the questionUrl out of questionList
     */
    public static List<Question> addAllQuestions(StudentModel studentModel, Question initialQuestion){
        //put initialQuestion, unseenQuestions and seenQuestions all in a list
        List<Question> unseenQuestionsWithCorrectUrl = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getTopLevelUnseenQuestions(), initialQuestion.getImageUrl());
        List<Question> seenQuestionsWithCorrectUrl = QuestionPool.getTopLevelQuestionsFromUrl(studentModel.getUserQuestionSet().getTopLevelSeenQuestions(), initialQuestion.getImageUrl());
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

        List<Question> seenQuestions = studentModel.getUserQuestionSet().getTopLevelSeenQuestions();

        if(seenQuestions.size()==0){
            throw new RuntimeException("The studentModel has no questions");
        }

        int index = 0;
        for(int i = 0; i<seenQuestions.size(); i++){
            if( (studentModel.getUserQuestionSet().getTopLevelSeenQuestions().get(i).getType().equals(type)) && (studentModel.getUserQuestionSet().getTimesSeen(seenQuestions.get(i).getId()) < studentModel.getUserQuestionSet().getTimesSeen(seenQuestions.get(index).getId()))){
                index = i;
            }
        }
        return seenQuestions.get(index);
    }

}
