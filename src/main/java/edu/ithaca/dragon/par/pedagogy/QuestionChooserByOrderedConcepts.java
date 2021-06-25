package edu.ithaca.dragon.par.pedagogy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.List;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.QuestionHistory;



public class QuestionChooserByOrderedConcepts {
    //Each concept(type) has an average of last <window-size> questions which gets bucketed
    //rubric-style: unprepared,developing,competent,exemplary
    //having too few questions answered puts you automatically into unprepared
    //concepts are ordered
    //unprepared on next topic can get turned to developing by competent grade on prior
    //exemplary on the prior topic can get turned into competent by current topic
    //only ask questions on topics that are developing or competent

    //Find questions of the right type(s) that have the min seen count.
    //If a minSeen question has the same URL as last question, take that, otherwise go for first minSeen

    public Map<String,OrderedConceptRubric> conceptScoring;

    public QuestionChooserByOrderedConcepts(DomainDatasource domainDatasource){
        conceptScoring = new LinkedHashMap<>();
        Set<String> concepts = domainDatasource.getAllConcepts();
        String firstConcept = concepts.iterator().next();
        for(String concept:concepts){
            if(concept.equalsIgnoreCase(firstConcept)){
                conceptScoring.put(concept,OrderedConceptRubric.DEVELOPING);
            }
            else{
                conceptScoring.put(concept,OrderedConceptRubric.UNPREPARED);
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

        Set<String> concepts = domainDatasource.getAllConcepts();
        Collection<QuestionHistory> questionHistories = studentModelInfo.getQuestionHistories();
        List<Question> questions = domainDatasource.getAllQuestions();
        if(questionHistories.size()!=0){
            for(String concept:concepts){
                if(conceptScoring.get(concept)!=OrderedConceptRubric.EXEMPLARY && calcExemplary(concept, questionHistories, domainDatasource)){
                    conceptScoring.computeIfPresent(concept, (key,val) -> OrderedConceptRubric.EXEMPLARY);
                    String nextConcept = getNextConcept(concept, concepts);
                    // Does not account for if the next topic is exemplary 
                    // (will automatically demote the next topic to competent)
                    if(nextConcept!=""&&!calcCompetent(nextConcept, questionHistories, domainDatasource)){
                        conceptScoring.computeIfPresent(nextConcept,(key,val) -> OrderedConceptRubric.COMPETENT);
                    }
                }
                else if(conceptScoring.get(concept)!=OrderedConceptRubric.COMPETENT && calcCompetent(concept, questionHistories, domainDatasource)){
                    conceptScoring.computeIfPresent(concept, (key,val) -> OrderedConceptRubric.COMPETENT);
                    String nextConcept = getNextConcept(concept, concepts);
                    // Does not account for if the next topic is exemplary or competent
                    // (will automatically demote the next topic to developing)
                    if(nextConcept!=""&&!calcDeveloping(nextConcept, questionHistories, domainDatasource)){
                        conceptScoring.computeIfPresent(nextConcept,(key,val) -> OrderedConceptRubric.DEVELOPING);
                    }
                }
                else if(conceptScoring.get(concept)!=OrderedConceptRubric.DEVELOPING && calcDeveloping(concept, questionHistories, domainDatasource)){
                    conceptScoring.computeIfPresent(concept,(key,val) -> OrderedConceptRubric.DEVELOPING);
                }
                // TODO: must figure out when to demote to unprepared so as not to overwrite competent/exemplary updates 
            }
            List<Question> eligibleQuestions = new ArrayList<>();
            for(Entry<String,OrderedConceptRubric> entry: conceptScoring.entrySet()){
                if(entry.getValue()==OrderedConceptRubric.COMPETENT||entry.getValue()==OrderedConceptRubric.DEVELOPING){
                    eligibleQuestions.addAll(domainDatasource.getQuestionsByConcept(entry.getKey()));
                }
            }
            if(eligibleQuestions.size()!=0){
                List<String> eligibleQuestionIds = new ArrayList<>();
                for(Question question : eligibleQuestions){
                    eligibleQuestionIds.add(question.getId());
                }
                String idOfQuestionToBeAsked = studentModelInfo.findQuestionSeenLeastRecently(eligibleQuestionIds);
                return domainDatasource.getQuestion(idOfQuestionToBeAsked);
            }
            else{
                throw new RuntimeException("No questions eligible for choosing");
            }
        }
        else{
            return questions.get(0);
        }
    }

    private String getNextConcept(String currentConcept,Set<String>concepts){
        Iterator<String> conceptIter = concepts.iterator();
        String nextConcept = "";
        while(conceptIter.hasNext()){
            if(conceptIter.next().equalsIgnoreCase(currentConcept)){
                if(conceptIter.hasNext()){
                    nextConcept=conceptIter.next();
                }
                break;
            }
        } // Otherwise there is no next concept
        return nextConcept;
    }
    private String getPreviousConcept(String currentConcept,Set<String>concepts){
        Object[] conceptsArray = concepts.toArray();
        int i=0;
        for(Object concept:conceptsArray){
            String conceptString = new String((String) concept);
            if(conceptString.equalsIgnoreCase(currentConcept)&&i!=0){
                return new String((String)conceptsArray[i-1]);
            }
            i++;
        }
        return "";
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
    public boolean calcUnprepared(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        String firstConcept = conceptScoring.keySet().iterator().next();
        if(concept.equalsIgnoreCase(firstConcept)){
            return false;
        } else {
            List<Question> questionsFromModelByConcept = getQuestionsFromStudentModelByConcept(concept, questionHistories, domainDatasource);
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
            for(Question question: questionsFromModelByConcept){
                for(String questionId:questionIdsForEverCorrect){
                    if(questionId.equalsIgnoreCase(question.getId())){
                        return false;
                    }
                }
            }
            return true;
        }
    }
    /**
     * @param questionsFromModelByConcept
     * @param questionHistories
     * @param domainDatasource
     * @returns true if the student has gotten at least one question correct at one point
     */
    public boolean calcDeveloping(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        String firstConcept = conceptScoring.keySet().iterator().next();
        String previousConcept=getPreviousConcept(concept, domainDatasource.getAllConcepts());
        if(concept.equalsIgnoreCase(firstConcept) && (calcCompetent(concept, questionHistories, domainDatasource)==false &&  calcExemplary(concept, questionHistories, domainDatasource)==false)){
            return true;
        }
        else if(previousConcept!=""&&calcCompetent(previousConcept,questionHistories,domainDatasource) && calcUnprepared(concept, questionHistories, domainDatasource)){
            return true;
        }
        else{
            List<Question> questionsFromModelByConcept = getQuestionsFromStudentModelByConcept(concept, questionHistories, domainDatasource);
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));

            for(Question question: questionsFromModelByConcept){
                for(String questionId:questionIdsForEverCorrect){
                    if(questionId.equalsIgnoreCase(question.getId())){
                        return true;
                    }
                }
            }
            return false;
        }
        
    }
    public boolean calcCompetent(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        String previousConcept=getPreviousConcept(concept, domainDatasource.getAllConcepts());
        if(previousConcept!=""&&calcExemplary(previousConcept,questionHistories,domainDatasource) && calcUnprepared(concept, questionHistories, domainDatasource)){
            return true;
        } else{
            List<Question> questionsFromModelByConcept = getQuestionsFromStudentModelByConcept(concept, questionHistories, domainDatasource);
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
            float denom = (float) domainDatasource.getQuestionsByConcept(concept).size();
            float num = 0;
            for(Question question: questionsFromModelByConcept){
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
    public boolean calcExemplary(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        List<Question> questionsFromModelByConcept = getQuestionsFromStudentModelByConcept(concept, questionHistories, domainDatasource);
        List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionsCorrectFirstTime(questionHistories,domainDatasource);
        questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionsCorrectAfterIncorrect(questionHistories, domainDatasource));
        int denom = domainDatasource.getQuestionsByConcept(concept).size();
        int num = 0;
        for(Question question: questionsFromModelByConcept){
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
