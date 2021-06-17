package edu.ithaca.dragon.par.pedagogy;

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

}
