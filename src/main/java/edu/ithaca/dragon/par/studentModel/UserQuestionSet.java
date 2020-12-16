package edu.ithaca.dragon.par.studentModel;

import edu.ithaca.dragon.par.domainModel.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class UserQuestionSet {
    private Logger logger = LogManager.getLogger(this.getClass());

    private String userId;
    private List<QuestionCount> questionCounts;


    public static UserQuestionSet buildNewUserQuestionSetFromQuestions(String userId, List<Question> questionsIn ){
        return new UserQuestionSet(userId, QuestionCount.questionToQuestionCount(questionsIn));
    }

    public static UserQuestionSet buildUserQuestionSetFromCounts(String userId, List<QuestionCount> questionCounts ){
        return new UserQuestionSet(userId, questionCounts);
    }

    private UserQuestionSet(String userId, List<QuestionCount> questionCounts ){
        this.userId = userId;
        this.questionCounts = questionCounts;
    }

    public List<QuestionCount> getQuestionCounts(){ return questionCounts; }

    public List<Question> getAllQuestions(){
        List<Question> allQuestions=new ArrayList<>();
        for(QuestionCount currQuestion: questionCounts){
            allQuestions.add(currQuestion.getQuestion());
           // if(currQuestion.getQuestion().getFollowupQuestions().size()>0){
          //      allQuestions.addAll(currQuestion.getQuestion().getFollowupQuestions());
         //   }
        }
        return allQuestions;
    }


    public List<Question> getTopLevelAttemptedQuestions(){
        List<Question> attempted = new ArrayList<>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesAttempted()>0){
                attempted.add(currQuestion.getQuestion());
            }
        }
        return attempted;
    }

    //TODO: this method isn't used. that means it also isn't tested.
    public List<QuestionCount> getTopLevelAttemptedQuestionCounts(){
        List<QuestionCount> attempted = new ArrayList<>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesAttempted()>0){
                attempted.add(currQuestion);
            }
        }
        return attempted;
    }

    public List<Question> getTopLevelUnattemptedQuestions(){
        List<Question> unattempted = new ArrayList<>();
        for (QuestionCount currQuestion: questionCounts){
            if (currQuestion.getTimesAttempted()==0){
                unattempted.add(currQuestion.getQuestion());
            }
        }
        return unattempted;
    }


    public int getTimesAttempted(String questionId){
        QuestionCount qc = getQuestionCountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        return qc.timesAttempted;
    }


    public String getUserId () {
        return userId;
    }

    public void increaseTimesAttempted(String questionId){
        QuestionCount qc = getQuestionCountFromId(questionId);
        if(qc == null){
            throw new RuntimeException("QuestionCount with id:" + questionId + " does not exist");
        }
        qc.increaseTimesAttempted();

    }

    public QuestionCount getQuestionCountFromId(String questionIdIn){
        QuestionCount q = getQuestionCountFromId(questionIdIn, questionCounts);
        if(q == null){
            throw new RuntimeException("QuestionCount with id:" + questionIdIn + " does not exist");
        }
        return q;
    }

    public static QuestionCount getQuestionCountFromId(String questionIdIn, List<QuestionCount> questionList){
        for(QuestionCount q : questionList){
            if(q.getQuestion().getId().equals(questionIdIn)){
                return q;
            }

            //call getQuestionFromId on the followup questions
            QuestionCount q2 = getQuestionCountFromId(questionIdIn, q.getFollowupCounts());
            if(q2 != null){
                return q2;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!UserQuestionSet.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        UserQuestionSet other = (UserQuestionSet) otherObj;
        return this.getUserId().equals(other.getUserId())
                && this.getQuestionCounts().equals(other.getQuestionCounts());
    }

    public void increaseTimesAttemptedAllQuestions(List<Question> questions){
        for (int i = 0; i < questions.size(); i++){
            increaseTimesAttempted(questions.get(i).getId());
            if (questions.get(i).getFollowupQuestions().size()>0){
                increaseTimesAttemptedAllQuestions(questions.get(i).getFollowupQuestions());
            }
        }
    }


    public void addQuestion(Question q) {
        if (getQuestionCountFromId(q.getId(), getQuestionCounts())!=null){
            //TODO: decide better how to handle mathicng ID numbers
            logger.error("Ignoring Question "+q.getId()+", another with same ID already exists in the UserQuestionSet");
        }
        QuestionCount qcToAdd = new QuestionCount(q);
        questionCounts.add(qcToAdd);
    }

    public Map<String,List<QuestionCount>> questionCountsByTypeMap (){
        Map<String,List<QuestionCount>> questionTypesListMapToUpdate = new LinkedHashMap<>();
        questionCountsByTypeMap(this.questionCounts, questionTypesListMapToUpdate);
        return questionTypesListMapToUpdate;
    }

    public static void questionCountsByTypeMap(List<QuestionCount> questionCountList, Map<String,List<QuestionCount>> questionTypesListMap ){
        for(QuestionCount questionCount:questionCountList){
            if(questionTypesListMap.get(questionCount.getQuestion().getType())==null){
                List<QuestionCount> questions=new ArrayList<>();
                questions.add(questionCount);
                questionTypesListMap.put(questionCount.getQuestion().getType(),questions);
            }
            else questionTypesListMap.get(questionCount.getQuestion().getType()).add(questionCount);
            //makes recursive call for follow ups
            questionCountsByTypeMap(questionCount.getFollowupCounts(),questionTypesListMap);
        }
    }
}

