package edu.ithaca.dragon.par.studentmodel2;

public class StudentModelDatasourceInMemoryExample {

    public static StudentModelDatasourceInMemory createExample(){
        StudentModelDatasourceInMemory example = new StudentModelDatasourceInMemory();
        example.createNewModelForId("Homer");
        example.addTimeSeen("Homer", "q1");
        example.addResponse("Homer", "q1", "I don't know");
        example.addTimeSeen("Homer", "q2");
        example.addResponse("Homer", "q2", "donuts");

        example.createNewModelForId("Bart");
        example.addTimeSeen("Bart", "q1");
        example.addResponse("Bart", "q1", "I don't know");
        example.addTimeSeen("Bart", "q3");
        example.addResponse("Bart", "q3", "skateboard");

        example.createNewModelForId("Lisa");
        example.addTimeSeen("Lisa", "q1");
        example.addResponse("Lisa", "q1", "1000");
        example.addTimeSeen("Lisa", "q2");
        example.addResponse("Lisa", "q2", "water");
        example.addTimeSeen("Lisa", "q3");
        example.addResponse("Lisa", "q3", "being well rested");

        return example;
    }
}
