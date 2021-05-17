package edu.ithaca.dragon.par.pedagogicalModel;

public class LevelMessageInformation {
    private int previousLevel;
    private int currentLevel;

    public LevelMessageInformation(){
        previousLevel = 1;
        currentLevel = 1;
    }

    public LevelMessageInformation(int previousLevelIn, int currentLevelIn){
        previousLevel = previousLevelIn;
        currentLevel = currentLevelIn;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(int levelIn) {
        this.previousLevel = levelIn;
    }
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int levelIn) {
        this.currentLevel = levelIn;
    }
}
