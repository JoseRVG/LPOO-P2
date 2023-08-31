package maverick.gleao.wormsworld.ui;

/**
 * Created by Maverick on 16-May-16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.FileOutputStream;

import maverick.gleao.wormsworld.logic.Game;
import maverick.gleao.wormsworld.logic.HighScores;


public class GameEndActivity extends Activity implements OnClickListener{
    int score;
    long timeRemaining;
    Game.GameEvent event;
    Game.Level level;
    GameEndView view;

    boolean showingImage;

    public static final String KEY_GAMEEND_SCORE = "maverick.gleao.wormsdestruction.gameEnd_score";
    public static final String KEY_GAMEEND_TIME = "maverick.gleao.wormsdestruction.gameEnd_time";
    public static final String KEY_GAMEEND_EVENT = "maverick.gleao.wormsdestruction.gameEnd_event";
    public static final String KEY_GAMEEND_LEVEL = "maverick.gleao.wormsdestruction.gameEnd_level";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        score = i.getIntExtra(KEY_GAMEEND_SCORE,0);
        timeRemaining = i.getLongExtra(KEY_GAMEEND_TIME,0);
        event = (Game.GameEvent) i.getSerializableExtra(KEY_GAMEEND_EVENT);
        level = (Game.Level) i.getSerializableExtra(KEY_GAMEEND_LEVEL);

        this.showingImage = true;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        view = new GameEndView(this, getEventBitmap(event));
        setContentView(view);
        view.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer music_id = getEventMusic(event);
        Music.play(this, music_id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    public void onClick(View v) {
        if(this.showingImage)
        {
            this.showingImage = false;
            setContentView(R.layout.game_results);

            final TextView textResultsCoinScore = (TextView) findViewById(R.id.results_coin_score);
            textResultsCoinScore.setText("" + score);

            final TextView textResultsTime = (TextView) findViewById(R.id.results_time);
            textResultsTime.setText("" + Math.round(timeRemaining/1000) + " seconds");

            final TextView textResultsScore = (TextView) findViewById(R.id.results_score);
            textResultsScore.setText("" + HighScores.computeScore(score,timeRemaining,event));

            final TextView textResultsHighScore = (TextView) findViewById(R.id.results_highscore);
            textResultsHighScore.setText("" + HighScores.getInstance().getScore(level));

            this.findViewById(R.id.game_results).setOnClickListener(this);
        }
        else
        {
            //Save highscore
            saveHighScore();

            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Bitmap getEventBitmap(Game.GameEvent event)
    {
        Bitmap bitmap = null;
        if(event != null)
        {
            switch(event)
            {
                case WIN:
                    bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.win);
                    break;
                case LOSS_BOO:
                    bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.loss_boo);
                    break;
                case LOSS_TIMEUP:
                    bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.loss_timeup);
                    break;
                case LOSS_OUTOFBOUNDS:
                    bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.loss_outofbounds);
                    break;
            }
        }
        return bitmap;
    }

    private static Integer getEventMusic(Game.GameEvent event)
    {
        Integer music_id = null;
        if(event != null)
        {
            switch(event)
            {
                case WIN:
                    music_id = R.raw.win;
                    break;
                case LOSS_BOO:
                    music_id = R.raw.loss_boo;
                    break;
                case LOSS_TIMEUP:
                    music_id = R.raw.loss_timeout;
                    break;
                case LOSS_OUTOFBOUNDS:
                    music_id = R.raw.loss_outofbounds;
                    break;
            }
        }
        return music_id;
    }

    private void saveHighScore()
    {
        HighScores scores = HighScores.getInstance();
        scores.updateScore(level,HighScores.computeScore(score,timeRemaining,event));
        try {
            FileOutputStream outputStream = openFileOutput(HighScores.highScoresFileName, Context.MODE_PRIVATE);
            scores.saveScores(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
