package edu.ithaca.dragon.par.pedagogicalModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.*;

public class ImageTaskChooser {

    /**
     * Initial algorithm for generating ImageTasks. These imageTasks have lists with only one question
     * @param questionSet
     * @param difficulty
     * @return an image task with only one question
     */
    public static ImageTask nextImageTaskSingle(UserQuestionSet questionSet, int difficulty){
        List<Question> chooseSeen;
        List<Question> chooseUnseen;

        //unseen questions
        chooseUnseen = questionSet.getUnseenQuestions();    //unseen questions
        List<Integer> indUnseenSameDiff = getIndexesOfSameDifficulty(chooseUnseen, difficulty); //indexes of all questions with the same difficulty
        if(indUnseenSameDiff.size()>0) {   //if there's an unseen question matching difficulty
            ImageTask im = makeTask(chooseUnseen, indUnseenSameDiff.get(0)); //makes image task with the first unseen question
            questionSet.givenQuestion(questionSet.getUnseenQuestions().get(indUnseenSameDiff.get(0)).getId());  //switches question from seen to unseen
            return im;
        }


        //seen questions
        chooseSeen = questionSet.getSeenQuestions();    //no unseen questions with same difficulty, must get seen question
        List<Integer> indSeenSameDiff = getIndexesOfSameDifficulty(chooseSeen, difficulty);
        if (indSeenSameDiff.size()>0){
            int chooseInd = indexOfSeenLeast(questionSet, indSeenSameDiff);     //chooses index of the question with the same difficulty that has been seen the least
            ImageTask im = makeTask(chooseSeen, chooseInd);     //makes image task with the seen question with the same difficulty that has been seen the least
            questionSet.givenQuestion(questionSet.getSeenQuestions().get(chooseInd).getId());    //adds 1 to times seen
            return im;
        }
        return null;
    }

    public static List<Integer> getIndexesOfSameDifficulty(List<Question> questions, int difficulty){
        List<Integer> indexesSeen = new ArrayList<Integer>();
        for(int i = 0; i<questions.size(); i++){
//            if (questions.get(i).getType()==difficulty){
//                indexesSeen.add(i);
//            }
        }
        return indexesSeen;
    }

    public static int indexOfSeenLeast(UserQuestionSet questionSet, List<Integer> matchingIndexes){
        int minTimesSeen = questionSet.getTimesSeen(questionSet.getSeenQuestions().get(matchingIndexes.get(0)).getId());
        int index = matchingIndexes.get(0);
        for(int i = 0; i < matchingIndexes.size(); i++){
            if(questionSet.getTimesSeen(questionSet.getSeenQuestions().get(matchingIndexes.get(i)).getId())<minTimesSeen){
                minTimesSeen = questionSet.getTimesSeen(questionSet.getSeenQuestions().get(matchingIndexes.get(i)).getId());
                index = matchingIndexes.get(i);
            }
        }
        return index;
    }

    public static ImageTask makeTask (List<Question> choose, int index){
        Question q = choose.get(index);
        List<Question> qList = new ArrayList<Question>();
        qList.add(q);
        ImageTask im = new ImageTask(q.getImageUrl(), qList);    //returns image task with the first question of matching difficulty
        return im;
    }


    public static ImageTask nextImageTask(UserQuestionSet questionSet, int difficulty, int numOfQuestions) {
        ImageTask it = nextImageTaskSingle(questionSet, difficulty);
        List<Question> sameUrlListUnseen = getSameUrlQuestionsUnseen(questionSet, it.getTaskQuestions().get(0));
        List<Question> sameUrlListSeen = getSameUrlQuestionsSeen(questionSet, it.getTaskQuestions().get(0));
        if (sameUrlListSeen.size()+sameUrlListUnseen.size()<=numOfQuestions-1){     //1 question is already in the image task
            addAllQuestions(it, sameUrlListUnseen, sameUrlListSeen);
            if(it.getTaskQuestions().size()>1) {
                markAllAsSeen(it, questionSet);
            }
            return it;
        }

        else{
            if (it.getTaskQuestions().size()<numOfQuestions){
                lessOrEqualDifficultyAdded(it, numOfQuestions, sameUrlListUnseen);
                if (it.getTaskQuestions().size()<numOfQuestions){
                    lessOrEqualDifficultyAdded(it, numOfQuestions, sameUrlListSeen);
                }
            }
            if (it.getTaskQuestions().size()<numOfQuestions){
                greaterDifficultyAdded(it, numOfQuestions, sameUrlListUnseen);
                if (it.getTaskQuestions().size()<numOfQuestions){
                    greaterDifficultyAdded(it, numOfQuestions, sameUrlListSeen);
                }
            }
            if(it.getTaskQuestions().size()>1) {
                markAllAsSeen(it, questionSet);
            }
            return it;
        }
    }

    public static void addAllQuestions(ImageTask it, List<Question> sameUrlListUnseen, List<Question> sameUrlListSeen){
        for (int i = 0; i<sameUrlListUnseen.size(); i++){
            if (sameUrlListUnseen.get(i).getId()!=it.getTaskQuestions().get(0).getId()) {
                it.getTaskQuestions().add(sameUrlListUnseen.get(i));
            }
        }
        for (int i = 0; i<sameUrlListSeen.size(); i++){
            if (sameUrlListSeen.get(i).getId()!=it.getTaskQuestions().get(0).getId()) {
                it.getTaskQuestions().add(sameUrlListSeen.get(i));
            }
        }
    }

    public static void lessOrEqualDifficultyAdded(ImageTask it, int numOfQuestions, List<Question> toBeAdded){
//        for (int i = 0; i<toBeAdded.size(); i++){
//            if ((toBeAdded.get(i).getType()<=it.getTaskQuestions().get(0).getType()) && (toBeAdded.get(i).getId()!=it.getTaskQuestions().get(0).getId())){
//                it.getTaskQuestions().add(toBeAdded.get(i));
//            }
//            if (it.getTaskQuestions().size()==numOfQuestions){
//                i=toBeAdded.size();
//            }
//        }
    }

    public static void greaterDifficultyAdded(ImageTask it, int numOfQuestions, List<Question> toBeAdded){
//        for (int i = 0; i<toBeAdded.size(); i++){
//            if ((toBeAdded.get(i).getType()>it.getTaskQuestions().get(0).getType()) && (toBeAdded.get(i).getId()!=it.getTaskQuestions().get(0).getId())){
//                it.getTaskQuestions().add(toBeAdded.get(i));
//            }
//            if (it.getTaskQuestions().size()==numOfQuestions){
//                i=toBeAdded.size();
//            }
//        }
    }



    public static List<Question> getSameUrlQuestionsUnseen(UserQuestionSet questionSet, Question picked){
        List<Question> qList = new ArrayList<Question>();
        for (int i = 0; i<questionSet.getUnseenQuestions().size(); i++){
            if ((questionSet.getUnseenQuestions().get(i).getImageUrl().equals(picked.getImageUrl()) && (!questionSet.getUnseenQuestions().get(i).getId().equals(picked.getId())))){
                qList.add(questionSet.getUnseenQuestions().get(i));
            }
        }
        return qList;
    }

    public static List<Question> getSameUrlQuestionsSeen(UserQuestionSet questionSet, Question picked){
        List<Question> qList = new ArrayList<Question>();
        for (int i = 0; i<questionSet.getSeenQuestions().size(); i++){
            if ((questionSet.getSeenQuestions().get(i).getImageUrl().equals(picked.getImageUrl()) && (!questionSet.getSeenQuestions().get(i).getId().equals(picked.getId())))){
                qList.add(questionSet.getSeenQuestions().get(i));
            }
        }
        return qList;
    }


    public static void markAllAsSeen(ImageTask it, UserQuestionSet questionSet){     //skips first question, as that is covered in nextImageTaskSingle
        for (int i = 1; i<it.getTaskQuestions().size(); i++){
            questionSet.givenQuestion(it.getTaskQuestions().get(i).getId());
        }
    }
}
