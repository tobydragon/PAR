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
    public static ImageTask nextImageTask(UserQuestionSet questionSet, int difficulty){
        List<Question> choose;
        List<Question> sameDifficulty = new ArrayList<Question>();

        //unseen questions
        choose = questionSet.getUnseenQuestions();
        //returns the first one found with the same difficulty
        for (int i = 0; i<choose.size(); i++){
            if(choose.get(i).getDifficulty()==difficulty){
                Question q = choose.get(i);//updates seen vs unseen in the question set
                questionSet.givenQuestion(choose.get(i).getId());
                //returns image task with the first question of matching difficulty
                ImageTask im = new ImageTask(q.getImageUrl(), Arrays.asList(q));
                return im;
            }
        }
        //no unseen questions with same difficulty
        choose = questionSet.getSeenQuestions();
        //times seen array for questions of the same difficulty
        List<Integer> ts = new ArrayList<Integer>();
        //adds questions of same difficulty to array, adds corresponding times seen value to ts
        for (int i = 0; i<choose.size(); i++){
            if(choose.get(i).getDifficulty()==difficulty){
                sameDifficulty.add(choose.get(i));
                ts.add(questionSet.timesSeen.get(i));
            }
        }
        //if there's any matching the same difficulty (there should be, else given difficulty is invalid)
        if(sameDifficulty.size()>0){
            int minTimesSeen = questionSet.getTimesSeen(sameDifficulty.get(0).getId());
            int index = 0;
            //stores the index of the question that's been seen the least
            for(int i = 0; i < sameDifficulty.size(); i++){
                if(ts.get(i)<minTimesSeen){
                    minTimesSeen = ts.get(i);
                    index = i;
                }
            }
            Question q = sameDifficulty.get(index);
            //updates seen vs unseen in the question set
            questionSet.givenQuestion(q.getId());
            //returns the question with the matching difficulty that has been seen the least
            ImageTask im = new ImageTask(q.getImageUrl(), Arrays.asList(q));
            return im;
        }
        return null;
    }
}
