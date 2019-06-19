package edu.ithaca.dragon.par.pedagogicalModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.io.ImageTask;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.Arrays;
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
            if (questions.get(i).getDifficulty()==difficulty){
                indexesSeen.add(i);
            }
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
        ImageTask im = new ImageTask(q.getImageUrl(), Arrays.asList(q));    //returns image task with the first question of matching difficulty
        return im;
    }


//    public static ImageTask nextImageTask(UserQuestionSet questionSet, int difficulty, int numOfQuestions){
//        List<Question> chosen;
//
//    }
}
