package edu.ithaca.dragon.par.deprecated.domainModel.equineUltrasound;

import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.util.FileSystemUtil;
import edu.ithaca.dragon.util.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquineQuestionTemplateCreator {

    static int nextIdNumberToGive = 600;

    private static String createQuestionId(String identifier){
        String id = "" + nextIdNumberToGive + "-" +identifier;
        nextIdNumberToGive++;
        return id;
    }

    public static final String planeQuestion = "On which plane is the ultrasound taken?";
    public static final List<String> planeResponses = Arrays.asList("transverse (short axis)", "longitudinal (long axis)");

    public static final List<String> structureQuestions = Arrays.asList(
//            "What is the the hyperechoic structure?",
//            "What is the the hypoechoic structure?",
//            "What is the structure in the near field?",
//            "What is the structure in the far field?",
            null,
            null,
            null,
            null);

    public static final List<String> structureResponses = Arrays.asList(
            "Superficial digital flexor tendon",
            "Deep digital flexor tendon",
            "Suspensory ligament (body)",
            "Suspensory ligament (branches)",
            "Distal check ligament (accessory ligament of the deep digital flexor tendon)",
            "Metacarpus bone 3 (Third metacarpal bone)",
            "Proximal sesamoid bones",
            "P1 (First phalanx)",
            "P2 (Second phalanx)",
            "Distal sesamoidean ligaments – straight and oblique)",
            "Palmar annular ligament",
            "Palmar ligament",
            "Palmar vessels (medial/lateral)",
            "Palmar metacarpal vessels (medial/lateral)");

    public static final String attachmentQuestion0 = "What is this structure’s proximal attachment?";

    public static final List<String> attachment0Responses = Arrays.asList(
            "Medial humeral epicondyle",
            "Lateral humeral epicondyle",
            "Both the proximal metacarpus 3 and distal row of carpal bones",
            "Distal row of carpal bones",
            "proximal metacarpus 3");

    public static final String attachmentQuestion1 = "What is this structure’s distal attachment?";

    public static final List<String> attachment1Responses = Arrays.asList(
            "Proximal phalanx (P1)",
            "Middle phalanx (P2)",
            "Distal phalanx (P3)",
            "Proximal sesamoid bones",
            "Both proximal and middle phalanxes",
            "Deep digital flexor tendon");

    public static final String zoneQuestion = "In which zone is the ultrasound taken?";
    public static final List<String> zoneResponses = Arrays.asList("1", "1A", "1B", "2", "2A", "2B", "3", "3A", "3B", "3C");

    public static List<Question> createQuestionsForImage(String imagePath){
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(createQuestionId(EquineQuestionTypes.plane.toString()+"-"+imagePath),
                planeQuestion,
                EquineQuestionTypes.plane.toString(),
                null,
                planeResponses,
                imagePath));
        for (int i=0; i<structureQuestions.size(); i++){
            String structureId = EquineQuestionTypes.structure.toString()+i+"-"+imagePath;
            List<Question> attachmentQuestionsToAdd = createAttachmentQuestionsList(imagePath, structureId);
            questions.add(new Question(createQuestionId(structureId), structureQuestions.get(i), EquineQuestionTypes.structure.toString(), null, structureResponses, imagePath, attachmentQuestionsToAdd));
        }
        questions.add(new Question(createQuestionId(EquineQuestionTypes.zone.toString()+"-"+imagePath), zoneQuestion,
                EquineQuestionTypes.zone.toString(), null, zoneResponses, imagePath));
        return questions;
    }

    public static List<Question> createAttachmentQuestionsList(String imagePath, String structureId){
        List<Question> attachmentQuestions = new ArrayList<>();
        attachmentQuestions.add(new Question(
                createQuestionId(EquineQuestionTypes.attachment.toString()+"0-"+structureId),
                attachmentQuestion0,
                EquineQuestionTypes.attachment.toString(),
                null,
                attachment0Responses,
                imagePath
        ));
        attachmentQuestions.add(new Question(
                createQuestionId(EquineQuestionTypes.attachment.toString()+"1-"+structureId),
                attachmentQuestion1,
                EquineQuestionTypes.attachment.toString(),
                null,
                attachment1Responses,
                imagePath
        ));
        return attachmentQuestions;
    }

    public static List<Question> createQuestionsForImageList(List<String> imagePathList) {
        List<Question> questions = new ArrayList<>();
        for (String imagePath: imagePathList){
            questions.addAll(createQuestionsForImage(imagePath));
        }
        return questions;
    }

    public static void main(String[] args){
        String imageFilePath = "src/main/resources/static/images/new";
        String imagePathForJavascript = "./images";
        String questionFileToCreate = "src/main/resources/author/generatedQuestions.json";

        try {
            List<String> imageFilePaths = FileSystemUtil.addPathToFilenames(imagePathForJavascript, FileSystemUtil.findAllFileNamesInDir(imageFilePath, ".jpg"));

            List<Question> generatedQuestions = createQuestionsForImageList(imageFilePaths);
            JsonUtil.toJsonFile(questionFileToCreate, generatedQuestions);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


}
