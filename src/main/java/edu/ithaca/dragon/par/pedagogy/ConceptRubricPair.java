package edu.ithaca.dragon.par.pedagogy;

public class ConceptRubricPair {
    private String concept;
    private OrderedConceptRubric score;
    
    public ConceptRubricPair(){}
    public ConceptRubricPair(String concept,OrderedConceptRubric score){
        this.concept=concept;
        this.score = score;
    }
    public String getConcept() {
        return concept;
    }
    public void setConcept(String concept) {
        this.concept = concept;
    }
    public OrderedConceptRubric getScore() {
        return score;
    }
    public void setScore(OrderedConceptRubric score) {
        this.score = score;
    }
    
}
