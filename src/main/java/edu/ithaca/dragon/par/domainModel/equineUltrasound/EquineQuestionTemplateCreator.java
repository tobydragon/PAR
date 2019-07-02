package edu.ithaca.dragon.par.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domainModel.Question;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquineQuestionTemplateCreator {

    public static final List<String> planeResponses = Arrays.asList("transverse", "longitudinal");

    public static final List<String> structureQuestions = Arrays.asList(
            "What is the the hyperechoic structure?",
            "What is the the hypoechoic structure?",
            "What is the structure in the near field?",
            "What is the in the far field?");

    public static final List<String> structureResponses = Arrays.asList(
            "Superficial digital flexor tendon",
            "Deep digital flexor tendon",
            "Suspensory ligament (body)",
            "Suspensory ligament (branches)",
            "Distal check ligament (We use Accessory ligament of the deep digital flexor tendon)",
            "Metacarpus bone 3 (Third metacarpal bone)",
            "Proximal sesamoid bones",
            "P1 (First phalanx)",
            "P2 (Second phalanx)",
            "distal sesamoidean ligaments – straight and oblique)",
            "Palmar annular ligament",
            "Palmar ligament",
            "Palmar vessels (medial/lateral)",
            "Palmar metacarpal vessels (medial/lateral)");

    public static final List<String> zoneResponses = Arrays.asList("1", "1A", "1B", "2", "2A", "2B", "3", "3A", "3B", "3C");

    public static List<Question> createQuestionsForImage(String imagePath){
       List<Question> questions = new ArrayList<>();
       questions.add(new Question("plane"+imagePath, "On which plane is the ultrasound taken?",
               EquineQuestionTypes.plane.toString(), null, planeResponses, imagePath));
       for (int i=0; i<structureQuestions.size(); i++){
           questions.add(new Question("structure"+i+imagePath, structureQuestions.get(i),
                   EquineQuestionTypes.structure.toString(), null, structureResponses, imagePath));
       }
       questions.add(new Question("zone"+imagePath, "in which zone is the ultrasound taken?",
                EquineQuestionTypes.zone.toString(), null, zoneResponses, imagePath));
       return questions;
    }

    public static List<Question> createQuestionsForImageList(List<String> imagePathList) {
        List<Question> questions = new ArrayList<>();
        for (String imagePath: imagePathList){
            questions.addAll(createQuestionsForImage(imagePath));
        }
        return questions;
    }

    public static void main(String[] args){
        String imageFilePath = "src/main/resources/static/images";
        String imagePathForJavascript = "./images";
        String questionFileToCreate = "src/main/resources/author/generatedQuestions.json";

        try {
            List<String> imageFilePaths = FileSystemUtil.addPathToFilenames(imagePathForJavascript,
                    FileSystemUtil.findAllFileNamesInDir(imageFilePath, ".jpg"));
            List<Question> generatedQuestions = createQuestionsForImageList(imageFilePaths);
            JsonUtil.toJsonFile(questionFileToCreate, generatedQuestions);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


}
