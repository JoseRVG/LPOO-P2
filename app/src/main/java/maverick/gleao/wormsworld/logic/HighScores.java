package maverick.gleao.wormsworld.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * HighScores.java - class used to store the game's high scores.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class HighScores {
    /**
     * Structure that stores the game's high scores, for each level.
     */
    HashMap<Game.Level,Integer> scores;
    /**
     * Name of the file to store the highscores.
     */
    public static final String highScoresFileName = "highscores";

    /**
     * Single instance of this class (singleton).
     */
    private static HighScores ourInstance = new HighScores();

    /**
     * Retrieves the single instance of this class (singleton).
     *
     * @return The single instance of this class.
     */
    public static HighScores getInstance() {
        return ourInstance;
    }

    /**
     * Basic constructor for the HighScores class.
     */
    private HighScores()
    {
        scores = new HashMap<Game.Level,Integer>();
        resetScores();
    }

    /**
     * Retrieves the high score for a specified level.
     *
     * @param level Level to retrieve the score.
     * @return High score of the level.
     */
    public int getScore(Game.Level level)
    {
        return scores.get(level);
    }

    /**
     * Updates the high score for a specified level.
     *
     * @param level Level to update the score.
     * @param score Score to update the level (if the score surpasses the high score).
     */
    public void updateScore(Game.Level level, int score)
    {
        int oldScore = getScore(level);
        if(score > oldScore)
        {
            scores.put(level,score);
        }
    }

    /**
     * Computes a score.
     *
     * @param coin_score Score associated with the amount of coins collected.
     * @param timeRemaining Number of seconds remaining, after the game ended.
     * @param event Outcome of the game.
     * @return Final score of the game.
     */
    public static int computeScore(int coin_score, long timeRemaining, Game.GameEvent event)
    {
        if(event == Game.GameEvent.WIN)
        {
            return coin_score*(int)Math.round(timeRemaining/1000);
        }
        return 0;
    }

    /**
     * Saves the high scores to an internal file.
     *
     * @param outputStream Stream used to save the scores.
     */
    public void saveScores(FileOutputStream outputStream)
    {
        try {
            if(outputStream != null)
            {
                ObjectOutput output = new ObjectOutputStream(outputStream);
                output.writeObject(scores);
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the high scores from an internal file.
     *
     * @param inputStream Stream used to load the scores.
     */
    public void loadScores(FileInputStream inputStream)
    {
        try {
            if(inputStream != null)
            {
                ObjectInput input = new ObjectInputStream(inputStream);
                scores = (HashMap<Game.Level,Integer>)input.readObject();
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the high scores to 0.
     */
    public void resetScores()
    {
        for(int i = 0; i < Game.levels.length; i++)
        {
            scores.put(Game.levels[i],0);
        }
    }
}
