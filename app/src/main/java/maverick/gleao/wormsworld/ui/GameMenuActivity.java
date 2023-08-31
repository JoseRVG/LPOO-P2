package maverick.gleao.wormsworld.ui;

/**
 * Created by Maverick on 16-May-16.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import maverick.gleao.wormsworld.logic.Game;
import maverick.gleao.wormsworld.logic.HighScores;

public class GameMenuActivity extends Activity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner;
    private ImageView chord_img;
    private Game.Level level;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.gamemenu);

        spinner =(Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.map,android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        chord_img=(ImageView) findViewById(R.id.mapView);
        chord_img.setImageResource(R.drawable.ship);
        level = Game.Level.SHIP;

        final TextView textLevelHighScore = (TextView) findViewById(R.id.textLevelHighScore);
        textLevelHighScore.setText("Highscore: " + HighScores.getInstance().getScore(level));

        Button btnG = (Button) findViewById(R.id.btnGame);
        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread myThread = new Thread(){
                    @Override
                    public void run() {
                        Intent iv = new Intent(getApplicationContext(),GamePlayActivity.class);
                        iv.putExtra(GamePlayActivity.KEY_MAINACTIVITY_LEVEL,level);
                        startActivity(iv);
                        finish();
                    }
                };
                myThread.start();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView mytext=(TextView) view;
         Toast.makeText(this,"select map"+mytext.getText(),Toast.LENGTH_SHORT).show();

        if(spinner.getId()==R.id.spinner) {
            int pos = spinner.getSelectedItemPosition();
            switch (pos) {
                case 0:
                    chord_img.setImageResource(R.drawable.ship);
                    level = Game.Level.SHIP;
                    break;
                case 1:
                    chord_img.setImageResource(R.drawable.snowman);
                    level = Game.Level.SNOWMAN;
                    break;
                case 2:
                    chord_img.setImageResource(R.drawable.dinosaurs);
                    level = Game.Level.DINOSAURS;
                    break;
                case 3:
                    chord_img.setImageResource(R.drawable.desert);
                    level = Game.Level.DESERT;
                    break;
                case 4:
                    chord_img.setImageResource(R.drawable.tycoon);
                    level = Game.Level.TYCOON;
                    break;
                case 5:
                    chord_img.setImageResource(R.drawable.volcano);
                    level = Game.Level.VOLCANO;
                    break;
                case 6:
                    chord_img.setImageResource(R.drawable.japan);
                    level = Game.Level.JAPAN;
                    break;
            }
        }
        final TextView textLevelHighScore = (TextView) findViewById(R.id.textLevelHighScore);
        textLevelHighScore.setText("Highscore: " + HighScores.getInstance().getScore(level));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
