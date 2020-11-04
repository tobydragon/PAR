package edu.ithaca.dragon.par.pedagogicalModel;

public class LevelMessageInformationAttachment {
    private int previousLevel;
    private int currentLevel;

    public LevelMessageInformationAttachment(){
        previousLevel = 1;
        currentLevel = 1;
    }

    public LevelMessageInformationAttachment(int previousLevelIn, int currentLevelIn){
        previousLevel = previousLevelIn;
        currentLevel = currentLevelIn;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(int levelIn) {
        if (levelIn < 1 || levelIn > 6){
            throw new IllegalArgumentException("Invalid previousLevel");
        }
        this.previousLevel = levelIn;
    }
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int levelIn) {
        if (levelIn < 1 || levelIn > 6){
            throw new IllegalArgumentException("Invalid previousLevel");
        }
        this.currentLevel = levelIn;
    }
}
