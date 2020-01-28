package edu.ithaca.dragon.par.studentModel;
import edu.ithaca.dragon.par.domainModel.Question;


import java.util.ArrayList;
import java.util.List;

public class ResponsesPerQuestion {
    public static final long SECONDS_BETWEEN =30;//TODO
    private String questionId;
    private String userId;
    private Question question;
    private String firstResponse;
    private String questionType;
    private List<QuestionResponse> allResponses;


    public ResponsesPerQuestion(){
        this.allResponses=new ArrayList<>();
    }
    public ResponsesPerQuestion(String userIdIn, Question questionIn, String responseIn){
        QuestionResponse questionResponse=new QuestionResponse(responseIn);
        this.userId=userIdIn;
        this.question=questionIn;
        this.questionId=questionIn.getId();
        this.questionType=questionIn.getType();
        firstResponse=responseIn;
        allResponses =new ArrayList<>();
        allResponses.add(questionResponse);
    }

    /**
     * Stub function that calls the static knowledgeCalc
     * @return 0 or 100
     */
    public double knowledgeCalc(){
       return knowledgeCalc(allResponses, question);
    }

    /**
     * Calculates the knowledge score of the student
     * This algorithm relies on the most recent response being at the front of allResponses!
     * @return 0 or 100
     */
    public static double knowledgeCalc(List<QuestionResponse> allResponses, Question question){
        double score=0;
        boolean responseIsCorrect = allResponses.get(0).getResponseText().trim().toLowerCase().equals(question.getCorrectAnswer().trim().toLowerCase());
        if(allResponses.size()==1) {
            if (responseIsCorrect){
                score = 100;
            }
            else{
                score = 0;
            }
        }
        else if(allResponses.size()>1) {
            //if the timestamp difference is > or == 30 seconds
            if (checkTimeStampDifference(allResponses.get(allResponses.size() - 1).getMillSeconds(), allResponses.get(allResponses.size() - 2).getMillSeconds())) {
                //check the answer if its right or wrong/ not dependent on previous answers
                if (responseIsCorrect) {
                    score = 100;
                }
                else score = 0;
            } else {
                //if the timestamp difference is < 30 seconds
                score = 100;
                //creates a list of QuestionResponses that have a timestamp difference < 30 second apart
                List<QuestionResponse> questionResponses = answersWithSameTimeStamp(allResponses);
                for (int k = 0; k < questionResponses.size() ; k++) {
                    //if any of the answers are wrong within this list then you receive a 0
                    if (!responseIsCorrect)
                        score = 0;
                }
            }
        }
        //TODO: add else case to deal with 0 case
        return score;
    }

    /**
     * Creates a list of QuestionResponses that have the same timestamps
     * @param allResponses list of all the responses recorded
     * @return list of QuestionResponses within less than a 30 seconds time window
     */
    public static List<QuestionResponse> answersWithSameTimeStamp(List<QuestionResponse> allResponses){
        List<QuestionResponse> questionResponses=new ArrayList<>();
        questionResponses.add(allResponses.get(allResponses.size()-1));
        for(int k=allResponses.size()-2;k>-1;k--){//k=allResponses.size()-2 to get all the index before the last element in the list
            if(!checkTimeStampDifference(allResponses.get(allResponses.size()-1).getMillSeconds(),allResponses.get(k).getMillSeconds())){
                questionResponses.add(allResponses.get(k));
            }
        }
        return questionResponses;
}

    /**
     * Calculates the seconds difference between two timestamps
     * @param firstTimeStamp newest timestamp
     * @param secondTimeStamp timestamp before last
     * @return true if the timestamps are 30 or more seconds apart/false if less than 30 seconds apart
     */
    public static boolean checkTimeStampDifference(long firstTimeStamp,long secondTimeStamp){
        long secondsDifference =Math.abs(firstTimeStamp-secondTimeStamp)/1000;
        if(secondsDifference>=SECONDS_BETWEEN) return true;
        return false;
    }


    public void addNewResponse(String newResponse){
        //creates a new timestamp for new response
        QuestionResponse questionResponse=new QuestionResponse(newResponse);
        this.allResponses.add(questionResponse);
    }

    public static boolean checkIfAnswerIsCorrect(String correctAnswer, String response){
        return false;
    }

    public int allResponsesSize(){
        return allResponses.size();
    }

    public void setQuestionId(String questionIdIn){this.questionId=questionIdIn;}
    public String getQuestionId(){
        return questionId;
    }

    public void setUserId(String userIdIn){this.userId=userIdIn;}
    public String getUserId(){
        return userId;
    }

    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<QuestionResponse> getAllResponses(){
        return allResponses;
    }
    public void setAllResponses(List<QuestionResponse> allResponses) {
        this.allResponses = allResponses;
    }

    public void setFirstResponse(String responseTextIn){this.firstResponse=responseTextIn;}
    public String getFirstResponse(){return firstResponse;}

    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public long getTimeStamp(){
        return allResponses.get(allResponsesSize()-1).getMillSeconds();
    }
    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null){
            return false;
        }
        if(!ResponsesPerQuestion.class.isAssignableFrom(otherObj.getClass())){
            return false;
        }
        ResponsesPerQuestion other = (ResponsesPerQuestion) otherObj;
        return this.getQuestionId().equals(other.getQuestionId())
                && this.getFirstResponse().equals(other.getFirstResponse())
                && this.getUserId().equals(other.getUserId())
                && this.getAllResponses().equals(other.getAllResponses())
                && this.getQuestion().equals(other.getQuestion());

    }

}

