package edu.ithaca.dragon.par.authorModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.QuestionCount;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AuthorTaskGenerator {

    public static ImageTask makeTaskTemplate(AuthorModel authorModel){
        if (authorModel.getQuestionCountList().size()<1){
            return new ImageTask("noMoreQuestions", new ArrayList<>(), "None");//TODO:NEED LINE
        }

        QuestionCount initialQuestion = getInitialQuestion(authorModel.getQuestionCountList());
        List<QuestionCount> imageTaskList = getAllQuestionsWithSameUrl(initialQuestion, authorModel);
        List<Question> forTask = new ArrayList<>();
        for (int i = 0; i < imageTaskList.size(); i++){
            forTask.add(imageTaskList.get(i).getQuestion());
        }
        ImageTask task = new ImageTask(imageTaskList.get(0).getQuestion().getImageUrl(), forTask, "None");
        return task;
    }

    public static Set<String> urls(List<Question> questionList){
        Set<String> urls=new LinkedHashSet<>();
        for(int i=0;i<questionList.size();i++){
            urls.add(questionList.get(i).getImageUrl());
        }
        return urls;
    }

    public static List<ImageTask> authoredQuestions(List<Question> questionList){
        List<ImageTask> authoredQuestions=new ArrayList<>();
        List<Question> forTask;
        Set<String> urls=urls(questionList);

        for (String currUrl:urls){
            forTask=new ArrayList<>();
            for (int k = 0; k < questionList.size(); k++) {
                if(currUrl.equals(questionList.get(k).getImageUrl())){
                    forTask.add(questionList.get(k));
                }
            }
            ImageTask task = new ImageTask(currUrl, forTask, "None");
            authoredQuestions.add(task);
        }
        return authoredQuestions;
    }


    public static QuestionCount getInitialQuestion(List<QuestionCount> questionCountList){
        int index = 0;
        for (int i = 0; i < questionCountList.size(); i++){
            if (questionCountList.get(i).getTimesAttempted()<questionCountList.get(index).getTimesAttempted()){
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
