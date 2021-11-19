package edu.ithaca.dragon.par.pedagogy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.ithaca.dragon.par.analysis.QuestionHistorySummary;
import edu.ithaca.dragon.par.domain.DomainDatasource;
import edu.ithaca.dragon.par.domain.Question;
import edu.ithaca.dragon.par.student.StudentModelInfo;
import edu.ithaca.dragon.par.student.json.QuestionHistory;



public class QuestionChooserChangeConceptByWindow implements QuestionChooser{
    Random rand = new Random();
    //Each concept(type) has an average of last <window-size> questions which gets bucketed
    //rubric-style: unprepared,developing,competent,exemplary
    //having too few questions answered puts you automatically into unprepared
    //concepts are ordered
    //unprepared on next topic can get turned to developing by competent grade on prior
    //exemplary on the prior topic can get turned into competent by current topic
    //only ask questions on topics that are developing or competent

    //Find questions of the right type(s) that have the min seen count.
    //If a minSeen question has the same URL as last question, take that, otherwise go for first minSeen

    public List<String> conceptIds;
    public int windowSize;

    public QuestionChooserChangeConceptByWindow(){
        windowSize=3;

    }

    public QuestionChooserChangeConceptByWindow(List<String> concepts, int windowSize){
        conceptIds=concepts;
        this.windowSize=windowSize;
        
    }
    public Question chooseQuestion(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){
        
        //reused random question chooser code
        //could be piped instead
        
        List<Question> questions = chooseQuestions(studentModelInfo, domainDatasource);
        if(questions.size()>0){
            return questions.get((int)(Math.random()*questions.size()));
        }
        else{
            List<String> answers = new ArrayList<>();
            answers.add("End");
            return new Question("EndQ","Hi, if you're seeing this, it means you've scored exemplary on all course concepts and are now finished. Congrats! If you'd like the chance to practice more concepts with this system, create a new user anytime.","Finished","End",answers,"./images/PartyHat.jpeg");
        }
    }
    public List<Question> chooseQuestions(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){
        //Algorithm: 
        // 1. get all questions from domain datasource
        // 2. select the first concept from the list of questions to be developing, the rest are unprepared
        // 3. (Once competent, the next concept will be developing)
        // 4. (Once exemplary, the next concept will be updated to competent)
        // 5. from the developing and competent buckets, the question asked will be chosen based on least recently seen
        // *Test case for when first two topics are exemplary and the next 
        
        if(checkAllChooserConceptIdsAreInDomain(domainDatasource)){
            List<Question> eligibleQuestions = new ArrayList<>();

            List<ConceptRubricPair> conceptScores = createConceptScores(studentModelInfo,domainDatasource);

            for(ConceptRubricPair conceptScore:conceptScores){
                if(conceptScore.getScore()==OrderedConceptRubric.COMPETENT || conceptScore.getScore()==OrderedConceptRubric.DEVELOPING){
                    eligibleQuestions.addAll(domainDatasource.retrieveQuestionsByConcept(conceptScore.getConcept()));
                }
            }

        
            // if(eligibleQuestions.size()!=0){
            //     String questionIdToBeAsked = studentModelInfo.findQuestionSeenLeastRecently(eligibleQuestions.stream().map(q -> q.getId()).collect(Collectors.toList()));
            //     Question qToBeAsked = domainDatasource.getQuestion(questionIdToBeAsked);
            //     if(!hasUnpreparedFollowUpQuestions(conceptScores, qToBeAsked)){
            //         return qToBeAsked;
            //     }
            //     else{
            //         List<Question> followUpsWithUnpreparedRemoved = qToBeAsked.getFollowupQuestions().stream().filter(fq -> findScoreByConcept(conceptScores, fq.getType())!=OrderedConceptRubric.UNPREPARED).collect(Collectors.toList());
            //         Question qCopy = new Question(qToBeAsked, followUpsWithUnpreparedRemoved);
            //         return qCopy;
            //     }
            // }
            // else{
            //     throw new RuntimeException("No questions eligible for choosing");
            // }
            // if(eligibleQuestions.size()==0){
            //     throw new RuntimeException("No questions eligible for choosing");
            // }
            // else{
            //     return eligibleQuestions;
            // }
            List<String> eligibleIds = eligibleQuestions.stream().map(x -> x.getId()).collect(Collectors.toList()); 
            if(eligibleIds.size()>0){
                return studentModelInfo.findQuestionsSeenLeastRecently(eligibleIds).stream().map(val -> domainDatasource.getQuestion(val)).collect(Collectors.toList());
            }
            else{
                return new ArrayList<Question>();
            }
        } else{
            throw new RuntimeException("error: chooser concept ids do not match domain concepts");
        }
    }

    public List<String> getConceptIds() {
        return conceptIds;
    }

    public void setConceptIds(List<String> conceptIds) {
        this.conceptIds = conceptIds;
    }
    
    public static List<Question> retrieveQuestionsFromStudentModelByConcept(String concept, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
        List<Question> questionsByConceptFromDatasource = domainDatasource.retrieveQuestionsByConcept(concept);
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

    public List<ConceptRubricPair> createConceptScores(StudentModelInfo studentModelInfo, DomainDatasource domainDatasource){
        List<ConceptRubricPair> conceptScores = new ArrayList<>();
        if(conceptIds.size()>0){
            int i=0;
            for (String concept : conceptIds) {
                if(i==0){
                    conceptScores.add(new ConceptRubricPair(concept, OrderedConceptRubric.DEVELOPING));
                }else{
                    conceptScores.add(new ConceptRubricPair(concept, OrderedConceptRubric.UNPREPARED));
                }
                i++;
            }
            updateConceptScoresBasedOnPerformanceData(conceptScores, studentModelInfo, domainDatasource);
            conceptScores = updateConceptScoresBasedOnComparativeResults(conceptScores);
        }
        return conceptScores;
    }

    public static OrderedConceptRubric findScoreByConcept(List<ConceptRubricPair>conceptScores, String concept){

        List<OrderedConceptRubric> scoreListSize1 = conceptScores.stream().filter(cs ->cs.getConcept().equalsIgnoreCase(concept))
            .map(conceptScore -> conceptScore.getScore())
            .collect(Collectors.toList());

        if(scoreListSize1.size()>1){
            throw new RuntimeException("error: duplicate concepts in scoring list");
        }
        else if(scoreListSize1.size()<1){
            throw new RuntimeException("error: concept not found in list");
        }
        else{
            return scoreListSize1.get(0);
        }
    }

    public void updateConceptScoresBasedOnPerformanceData(List<ConceptRubricPair> conceptScores, StudentModelInfo studentModelInfo,DomainDatasource domainDatasource){
        Collection <QuestionHistory> questionHistories = studentModelInfo.getQuestionHistories().values();
        int i=0;
        for(ConceptRubricPair conceptScore:conceptScores){
            String concept = conceptScore.getConcept();
            // Only score most recent questions equal to the window size
            // Makes it possible for students to go down in score
            List<Question> conceptQuestionsSeenByStudent = retrieveQuestionsFromStudentModelByConcept(concept,questionHistories,domainDatasource);
            int numConceptQuestionsSeenByStudent = conceptQuestionsSeenByStudent.size();
            if(numConceptQuestionsSeenByStudent>windowSize){
                conceptQuestionsSeenByStudent = conceptQuestionsSeenByStudent.subList(numConceptQuestionsSeenByStudent-windowSize,numConceptQuestionsSeenByStudent);
            }
            if(domainDatasource.retrieveQuestionsByConcept(concept).size()==0){
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.UNASSESSABLE));
            }
            else if(calcUnprepared(conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.UNPREPARED));
            }
            else if(calcDeveloping(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.DEVELOPING));            
            }
            else if(calcCompetent(concept, conceptQuestionsSeenByStudent, questionHistories, domainDatasource)){
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.COMPETENT));
            }
            else{
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.EXEMPLARY));
            }
            i++;
        }
    }

    public List<ConceptRubricPair> updateConceptScoresBasedOnComparativeResults(List<ConceptRubricPair> conceptScores){
        int i=0;
        conceptScores = conceptScores.stream().filter(cs -> cs.getScore()!=OrderedConceptRubric.UNASSESSABLE).collect(Collectors.toList());
        
        for(ConceptRubricPair conceptScore:conceptScores){
            String concept = conceptScore.getConcept();
            if(i==0 && conceptScore.getScore()==OrderedConceptRubric.UNPREPARED){
                conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.DEVELOPING));
            }
            else if(i-1!=-1 && conceptScore.getScore()==OrderedConceptRubric.UNPREPARED){
                ConceptRubricPair previousConceptScore = conceptScores.get(i-1);
                if(previousConceptScore.getScore()==OrderedConceptRubric.COMPETENT || previousConceptScore.getScore()==OrderedConceptRubric.EXEMPLARY){
                    conceptScores.set(i,new ConceptRubricPair(concept,OrderedConceptRubric.DEVELOPING));
                }
            }
            
            i++;
        }

        return conceptScores;
        
    }

    public boolean calcUnprepared(List<Question> conceptQuestionsSeenByStudent, Collection<QuestionHistory> questionHistories, DomainDatasource domainDatasource){
    
        List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionIdsCorrectFirstTime(questionHistories,domainDatasource);
        questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionIdsCorrectAfterIncorrect(questionHistories, domainDatasource));
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
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionIdsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionIdsCorrectAfterIncorrect(questionHistories, domainDatasource));

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
            List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionIdsCorrectFirstTime(questionHistories,domainDatasource);
            questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionIdsCorrectAfterIncorrect(questionHistories, domainDatasource));
            float denom = (float) domainDatasource.retrieveQuestionsByConcept(concept).size();
            if(denom>windowSize){
                denom = (float) windowSize;
            }
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
        List<String> questionIdsForEverCorrect = QuestionHistorySummary.findQuestionIdsCorrectFirstTime(questionHistories,domainDatasource);
        questionIdsForEverCorrect.addAll(QuestionHistorySummary.findQuestionIdsCorrectAfterIncorrect(questionHistories, domainDatasource));
        int denom = domainDatasource.retrieveQuestionsByConcept(concept).size();
        System.out.println("denom: "+denom);
        if(denom>windowSize){
            denom = windowSize;
        }
        int num = 0;
        for(Question question: conceptQuestionsSeenByStudent){
            for(String questionId:questionIdsForEverCorrect){
                if(questionId.equalsIgnoreCase(question.getId())){
                    num++;
                }
            }
        }
        System.out.println(num/(float)denom);
        if(num/(float)denom > 0.75){
            return true;
        } else {return false;}
    }

    public boolean checkAllChooserConceptIdsAreInDomain(DomainDatasource domainDatasource){
        if(conceptIds.size()==0){
            throw new RuntimeException("error: chooser contains no concept ids");
        }
        List<String> domainConcepts = domainDatasource.retrieveAllConcepts();
        for(String concept:conceptIds){
            if(!domainConcepts.contains(concept)){
               return false;
            }
        }
        return true;
    }

    public boolean hasUnpreparedFollowUpQuestions(List<ConceptRubricPair> conceptScores, Question qToBeAsked){
        List<Question> followUps = qToBeAsked.getFollowupQuestions();
        if(followUps.size()==0){
            return false;
        }else{
            for (Question fq : followUps) {
                if(findScoreByConcept(conceptScores, fq.getType())==OrderedConceptRubric.UNPREPARED){
                    return true;
                }
            }
            return false;
        }

        
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    
    
}
