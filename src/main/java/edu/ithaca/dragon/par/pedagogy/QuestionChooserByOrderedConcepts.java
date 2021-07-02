package edu.ithaca.dragon.par.pedagogy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javafx.util.Pair;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.QuestionHistory;



public class QuestionChooserByOrderedConcepts implements QuestionChooser{
    //Each concept(type) has an average of last <window-size> questions which gets bucketed
    //rubric-style: unprepared,developing,competent,exemplary
    //having too few questions answered puts you automatically into unprepared
    //concepts are ordered
    //unprepared on next topic can get turned to developing by competent grade on prior
    //exemplary on the prior topic can get turned into competent by current topic
    //only ask questions on topics that are developing or competent

    //Find questions of the right type(s) that have the min seen count.
    //If a minSeen question has the same URL as last question, take that, otherwise go for first minSeen

    public List<Pair<String,OrderedConceptRubric>> conceptScores;
    public List<String> conceptIds;

    public QuestionChooserByOrderedConcepts(){
        conceptScores = new ArrayList<>();
    }

    public QuestionChooserByOrderedConcepts(List<String> concepts){
        conceptIds=concepts;
        conceptScores = new ArrayList<>();
        String firstConcept = concepts.iterator().next();
        for(String concept :concepts){
            if(concept.equalsIgnoreCase(firstConcept)){
                conceptScores.add(new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.DEVELOPING));
            }
            else{
                conceptScores.add(new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.UNPREPARED));
            }
        }
        
    }

    public Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){
        //Algorithm: 
        // 1. get all questions from domain datasource
        // 2. select the first concept from the list of questions to be developing, the rest are unprepared
        // 3. (Once competent, the next concept will be developing)
        // 4. (Once exemplary, the next concept will be updated to competent)
        // 5. from the developing and competent buckets, the question asked will be chosen based on least recently seen
        // *Test case for when first two topics are exemplary and the next 
        
        System.out.println("pre performance update");
        conceptScores.stream().forEach(System.out::println);

        updateConceptScoresBasedOnPerformanceData(studentModelInfo, domainDatasource);
        System.out.println("\n");
        System.out.println("post performance update");
        conceptScores.stream().forEach(System.out::println);
        System.out.println("\n");
        System.out.println("post comparative update");
        
        updateConceptScoresBasedOnComparativeResults();

        conceptScores.stream().forEach(System.out::println);
        System.out.println("\n");

        List<Question> eligibleQuestions = new ArrayList<>();
        for(Pair<String,OrderedConceptRubric> conceptScore:conceptScores){
            if(conceptScore.getValue()==OrderedConceptRubric.COMPETENT || conceptScore.getValue()==OrderedConceptRubric.DEVELOPING){
                eligibleQuestions.addAll(domainDatasource.getQuestionsByConcept(conceptScore.getKey()));
            }
        }

        if(eligibleQuestions.size()!=0){
            String questionIdToBeAsked = studentModelInfo.findQuestionSeenLeastRecently(eligibleQuestions.stream().map(q -> q.getId()).collect(Collectors.toList()));
            return domainDatasource.getQuestion(questionIdToBeAsked);
        }
        else{
            throw new RuntimeException("No questions eligible for choosing");
        }
    }

    public List<Pair<String,OrderedConceptRubric>> getConceptScores() {
        return conceptScores;
    }

    public void setConceptScores(List<Pair<String,OrderedConceptRubric>> conceptScores) {
        this.conceptScores = conceptScores;
    }

    public List<String> getConceptIds() {
        return conceptIds;
    }

    public void setConceptIds(List<String> conceptIds) {
        this.conceptIds = conceptIds;
        String firstConcept = conceptIds.iterator().next();
        List<Pair<String,OrderedConceptRubric>>conceptScoresIn = new ArrayList<>();
        for(String concept :conceptIds){
            if(concept.equalsIgnoreCase(firstConcept)){
                conceptScoresIn.add(new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.DEVELOPING));
            }
            else{
                conceptScoresIn.add(new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.UNPREPARED));
            }
        }
        setConceptScores(conceptScoresIn);
    }
    
    public static List<Question> getQuestionsFromStudentModelByConcept(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        List<Question> questionsByConceptFromDatasource = domainDatasource.getQuestionsByConcept(concept);
        List<Question> questionList = new ArrayList<>();
        for(Question question:questionsByConceptFromDatasource){
            for(QuestionHistory questionHist: questionHistories){
                if(questionHist.questionId.equalsIgnoreCase(question.getId())){
                    questionList.add(question);
                }
            }
        }
        return questionList;
    }

    public void updateConceptScoresBasedOnPerformanceData(StudentModelInfo studentModelInfo,DomainDatasource domainDatasource){
        Collection <QuestionHistory> questionHistories = studentModelInfo.getQuestionHistories();
        int i=0;
        for(Pair<String,OrderedConceptRubric> conceptScore:conceptScores){
            String concept = conceptScore.getKey();
            List<Question> conceptQuestionsSeenByStudent = getQuestionsFromStudentModelByConcept(concept,questionHistories,domainDatasource);
            if(calcUnprepared(conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.UNPREPARED));
            }
            else if(calcDeveloping(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.DEVELOPING));            
            }
            else if(calcCompetent(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.COMPETENT));
            }
            else{
                conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.EXEMPLARY));
            }
            i++;
        }
    }

    public void updateConceptScoresBasedOnComparativeResults(){
        int i=0;
        boolean isAllExemplary = true;
        for(Pair<String,OrderedConceptRubric> conceptScore:conceptScores){
            String concept = conceptScore.getKey();
            if(i==0 && conceptScore.getValue()==OrderedConceptRubric.UNPREPARED){
                conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.DEVELOPING));
                isAllExemplary=false;
            }
            else if(i-1!=-1 && conceptScore.getValue()==OrderedConceptRubric.UNPREPARED){
                Pair<String,OrderedConceptRubric> previousConceptScore = conceptScores.get(i-1);
                if(previousConceptScore.getValue()==OrderedConceptRubric.COMPETENT || previousConceptScore.getValue()==OrderedConceptRubric.EXEMPLARY){
                    conceptScores.set(i,new Pair<String,OrderedConceptRubric>(concept,OrderedConceptRubric.DEVELOPING));
                }
                isAllExemplary=false;
            }
            else{
                if(conceptScore.getValue()!=OrderedConceptRubric.EXEMPLARY){
                    isAllExemplary=false;
                }
            }
            i++;
        }

        if(isAllExemplary){
            conceptScores.stream().map(score -> new Pair<String,OrderedConceptRubric>(score.getKey(),OrderedConceptRubric.DEVELOPING));
        }
        
    }

    public boolean calcUnprepared(List<Question> conceptQuestionsSeenByStudent, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
    
        List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
        questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
        for(Question question: conceptQuestionsSeenByStudent){
            for(String questionId:questionIdsForEverCorrect){
                if(questionId.equalsIgnoreCase(question.getId())){
                    return false;
                }
            }
        }
        return true;
    
    }

    public boolean calcDeveloping(String concept, List<Question> conceptQuestionsSeenByStudent, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        if(calcCompetent(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)||calcExemplary(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
            return false;
        }
        else{
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));

            for(Question question: conceptQuestionsSeenByStudent){
                for(String questionId:questionIdsForEverCorrect){
                    if(questionId.equalsIgnoreCase(question.getId())){
                        return true;
                    }
                }
            }
            return false;
        }
        
        
    }
    public boolean calcCompetent(String concept, List<Question> conceptQuestionsSeenByStudent, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        if(calcExemplary(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
            return false;
        }
        else{
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
            float denom = (float) domainDatasource.getQuestionsByConcept(concept).size();
            float num = 0;
            for(Question question: conceptQuestionsSeenByStudent){
                for(String questionId:questionIdsForEverCorrect){
                    if(questionId.equalsIgnoreCase(question.getId())){
                        num++;
                    }
                }
            }
            if(num/denom > 0.5){
                return true;
            } else {return false;}
        }
    
    }
    public boolean calcExemplary(String concept, List<Question> conceptQuestionsSeenByStudent, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
        questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
        int denom = domainDatasource.getQuestionsByConcept(concept).size();
        int num = 0;
        for(Question question: conceptQuestionsSeenByStudent){
            for(String questionId:questionIdsForEverCorrect){
                if(questionId.equalsIgnoreCase(question.getId())){
                    num++;
                }
            }
        }
        if(num/denom ==1){
            return true;
        } else {return false;}
    }
    
}
