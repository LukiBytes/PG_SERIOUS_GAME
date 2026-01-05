package ModelScore;

public class EntryScore {

    private String playerName;
    private int score;
    private int difficulty;


    public EntryScore(String playerName, int score, int difficulty){
        this.playerName = playerName;
        this.score = score;
        this.difficulty = difficulty;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getDifficulty(){
        return difficulty;
    }


}
