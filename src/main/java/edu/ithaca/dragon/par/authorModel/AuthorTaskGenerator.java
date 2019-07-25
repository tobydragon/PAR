package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AuthorTaskGenerator {

    public static ImageTask makeTaskTemplate(AuthorModel authorModel){
        if (authorModel.getQuestionCountList().size()<1){
            return new ImageTask("noMoreQuestions", new ArrayList<>());
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

    public static Set<String> urls(QuestionPool questionPool){
        Set<String> urls=new LinkedHashSet<>();
        for(int i=0;i<questionPool.getAllQuestions().size()-1;i++){
            urls.add(questionPool.getAllQuestions().get(i).getImageUrl());
        }
        return urls;
    }

    public static List<ImageTask> authoredQuestions(QuestionPool questionPool){
        List<ImageTask> authoredQuestions=new ArrayList<>();
        List<Question> forTask;
        Set<String> urls=urls(questionPool);

        for (String currUrl:urls){
            forTask=new ArrayList<>();
            for (int k = 0; k < questionPool.getAllQuestions().size(); k++) {
                if(currUrl.equals(questionPool.getAllQuestions().get(k).getImageUrl())){
                    forTask.add(questionPool.getAllQuestions().get(k));
                }
            }
            ImageTask task = new ImageTask(currUrl, forTask);
            authoredQuestions.add(task);
        }
        return authoredQuestions;
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
