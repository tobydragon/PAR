package edu.ithaca.dragon.par.pedagogicalModel;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.domainModel.QuestionOrderedInfo;
import edu.ithaca.dragon.par.domainModel.QuestionPool;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.StudentModel;

import java.util.ArrayList;
import java.util.List;

public class OrderedTaskGenerator implements TaskGenerator {
    private final List<QuestionOrderedInfo> questionIds;
    private final int lastQuestionAskedIndex;
    private final QuestionPool questionPool;

    //orderedQuestionList assumed to be subset of StudentModel QuestionPool
    // TODO: 10/1/20 make robust by handling bad IDs
    public OrderedTaskGenerator(QuestionPool questionsToAsk, List<QuestionOrderedInfo> orderedQuestionList) {
        this.questionIds = orderedQuestionList;
        this.lastQuestionAskedIndex = 0;
        this.questionPool = questionsToAsk;
    }

    public static List<QuestionOrderedInfo> createOrderedQuestionInfoListFromQuestionPool(QuestionPool questionsToAdd, boolean isFollowupAttached) {
        List<QuestionOrderedInfo> toReturn = new ArrayList<>();
        List<Question> individualQuestions = questionsToAdd.getAllQuestions();
        for (int i = 0; i < individualQuestions.size(); i++){
            Question temp = individualQuestions.get(i);
            QuestionOrderedInfo tempInfo = new QuestionOrderedInfo(temp.getId(), isFollowupAttached);
            toReturn.add(tempInfo);
            List<Question> followups = temp.getFollowupQuestions();
            if(followups.size() > 0 && isFollowupAttached){
                for (int j = 0; j < followups.size(); j++){
                    Question depthOne = followups.get(j);
                    if(depthOne.getFollowupQuestions().size() > 0){
                        tempInfo = new QuestionOrderedInfo(depthOne.getId(), true);
                        toReturn.add(tempInfo);

                        List<Question> depthTwoList = depthOne.getFollowupQuestions();
                        for(int k = 0; k < depthTwoList.size(); k++){
                            Question depthTwo = depthTwoList.get(k);
                            tempInfo = new QuestionOrderedInfo(depthTwo.getId(), false);
                            toReturn.add(tempInfo);
                        }
                    } else {
                        tempInfo = new QuestionOrderedInfo(depthOne.getId(), false);
                        toReturn.add(tempInfo);
                    }
                }
            }
        }
        return toReturn;
    }

    @Override
    public ImageTask makeTask(StudentModel studentModel, int questionCountPerTypeForAnalysis) {
//        List<Question> questionsToSelect = questionPool.getAllQuestions();
//        Question nextQuestion= questionsToSelect.get((lastQuestionAskedIndex + 1) % questionIds.size());
//        List<Question> questionList = new ArrayList<>();
//        questionList.add(nextQuestion);
//        ImageTask imageTask = new ImageTask(nextQuestion.getImageUrl(), questionList);
//
//        studentModel.getUserQuestionSet().increaseTimesSeenAllQuestions(questionList);
        return null;
    }
}
