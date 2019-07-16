package edu.ithaca.dragon.par.pedagogicalModel;

public class PageSettings {
    private String scoreType;
    private Boolean showScore;

    public String getScoreType(){
        return scoreType;
    }

    public boolean getShowScore(){ return showScore; }

    public void setScoreType(String setNew){
        scoreType= setNew;
    }

    public void setShowScore(boolean setNew){
        showScore= setNew;
    }
}
