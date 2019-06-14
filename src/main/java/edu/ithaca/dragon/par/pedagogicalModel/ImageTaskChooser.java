package edu.ithaca.dragon.par.pedagogicalModel;
import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.par.studentModel.UserQuestionSet;

import java.util.*;

public class ImageTaskChooser {

//
//    public ImageTaskChooser(UserQuestionSet questionSetIn, int difficultyIn){
//        this.questionSet = questionSetIn;
//        this.difficulty = difficultyIn;
//
//    }

    public static Question nextImageTask(UserQuestionSet questionSet, int difficulty){
        List<Question> unseen = questionSet.getUnseenQuestions();
        List<Question> unseenSameDifficulty = new ArrayList<Question>();
        for (int i = 0; i<unseen.size(); i++){
            if(unseen.get(i).getDifficulty()==difficulty){
                unseenSameDifficulty.add(unseen.get(i));
            }
        }
        if(unseenSameDifficulty.size()>0){
            Question q = unseenSameDifficulty.get(0);
            questionSet.givenQuestion(q.getId());
            return q;
        }
        return null;
    }
}
