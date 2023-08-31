package maverick.gleao.wormsworld.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import maverick.gleao.wormsworld.logic.HighScores;

/**
 * Created by Maverick on 15-May-16.
 */
public class MainMenuActivity extends Activity{

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.mainscream);

        Button btnGameConfig = (Button) findViewById(R.id.one_on_one);
        btnGameConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread myThread = new Thread(){
                    @Override
                    public void run() {
                            Intent iv = new Intent(getApplicationContext(),GameMenuActivity.class);
                            startActivity(iv);
                            finish();
                    }
                };
                myThread.start();
            }
        });

        Button btnExit = (Button) findViewById(R.id.btnZoomIn);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        Button btnResetScores = (Button) findViewById(R.id.btnResetScores);
        btnResetScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetHighScores();
            }
        });

        loadHighScores();
    }

    private void loadHighScores()
    {
        HighScores scores = HighScores.getInstance();
        try {
            FileInputStream inputStream = openFileInput(HighScores.highScoresFileName);
            scores.loadScores(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetHighScores()
    {
        HighScores scores = HighScores.getInstance();
        scores.resetScores();
        try {
            FileOutputStream outputStream = openFileOutput(HighScores.highScoresFileName, Context.MODE_PRIVATE);
            scores.saveScores(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
