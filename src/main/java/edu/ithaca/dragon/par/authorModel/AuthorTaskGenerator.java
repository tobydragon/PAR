package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.List;

public class AuthorTaskGenerator {

    public static ImageTask makeTaskTemplate(AuthorModel authorModel){
        if (authorModel.getQuestionCountList().size()<1){
            throw new RuntimeException("No questions to be answered");
        }

        QuestionCount initialQuestion = getInitialQuestion(authorModel.getQuestionCountList());
        List<QuestionCount> imageTaskList = getAllQuestionsWithSameUrl(initialQuestion, authorModel);
        List<Question> forTask = new ArrayList<>();
        for (int i = 0; i < imageTaskList.size(); i++){
            forTask.add(imageTaskList.get(i).getQuestion());
        }
        ImageTask task = new ImageTask(imageTaskList.get(0).getQuestion().getImageUrl(), forTask);
        for (int i = 0; i < forTask.size(); i++){
            authorModel.increaseTimesSeen(forTask.get(i).getId());
        }
        return task;
    }

    public static QuestionCount getInitialQuestion(List<QuestionCount> questionCountList){
        int index = 0;
        for (int i = 0; i < questionCountList.size(); i++){
            if (questionCountList.get(i).getTimesSeen()<questionCountList.get(index).getTimesSeen()){
                index = i;
            }
        }
        return questionCountList.get(index);
    }

    public static List<QuestionCount> getAllQuestionsWithSameUrl(QuestionCount questionCount, AuthorModel authorModel){
        List<QuestionCount> sameUrl = new ArrayList<>();
        for (int i = 0; i < authorModel.getQuestionCountList().size(); i++){
            if (authorModel.getQuestionCountList().get(i).getQuestion().getImageUrl().equals(questionCount.getQuestion().getImageUrl())){
                sameUrl.add(authorModel.getQuestionCountList().get(i));
            }
        }
        return sameUrl;
    }
}
