package maverick.gleao.wormsworld.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import maverick.gleao.wormsworld.logic.Boo;
import maverick.gleao.wormsworld.logic.Coin;
import maverick.gleao.wormsworld.logic.Flag;
import maverick.gleao.wormsworld.logic.Flake;
import maverick.gleao.wormsworld.logic.Game;
import maverick.gleao.wormsworld.logic.Star;
import maverick.gleao.wormsworld.logic.Worm;

public class GamePlayActivity extends Activity {
    private static final String TAG = "GamePlayActivity";
    public static final String KEY_MAINACTIVITY_LEVEL = "maverick.gleao.wormsdestruction.mainActivity_level";

    private Game game;
    private GamePlayView gamePlayView;
    private Game.Level level;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    private final long DT = 100;

    ProgressBar timeBar;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.mactivity);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        level = (Game.Level)getIntent().getSerializableExtra(KEY_MAINACTIVITY_LEVEL);
        Bitmap gameMap = null;
        Bitmap keyMap = null;
        Bitmap background = null;
        switch (level) {
            case SHIP:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.ship, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.ship_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.ship_background, opts);
                mainLayout.setBackgroundResource(R.drawable.ocean);
                break;
            case SNOWMAN:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.snowman, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.snowman_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.snowman_background, opts);
                mainLayout.setBackgroundResource(R.drawable.snowfall_reports);
                break;
            case DINOSAURS:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.dinosaurs, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.dinosaurs_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.dinosaurs_background, opts);
                mainLayout.setBackgroundResource(R.drawable.aliya06);
                break;
            case DESERT:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.desert, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.desert_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.desert_background, opts);
                mainLayout.setBackgroundResource(R.drawable.desertimage);
                break;
            case TYCOON:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.tycoon, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.tycoon_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.tycoon_background, opts);
                mainLayout.setBackgroundResource(R.drawable.oilrig);
                break;
            case VOLCANO:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.volcano_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.volcano_background, opts);
                mainLayout.setBackgroundResource(R.drawable.vulcao);
                break;
            case JAPAN:
                gameMap = BitmapFactory.decodeResource(getResources(), R.drawable.japan, opts);
                keyMap = BitmapFactory.decodeResource(getResources(), R.drawable.japan_key, opts);
                background = BitmapFactory.decodeResource(getResources(), R.drawable.japan_background, opts);
                mainLayout.setBackgroundResource(R.drawable.japanimage);
                break;
        }

        loadSprites(opts);

        game = new Game(gameMap, keyMap, background, level);

        FrameLayout Draw = (FrameLayout) findViewById(R.id.Draw);
        gamePlayView = new GamePlayView(this, game);
        Draw.addView(gamePlayView);
        gamePlayView.requestFocus();

        Log.d(TAG, "onCreate");

        Button btnZoomIn = (Button) findViewById(R.id.btnZoomIn);
        btnZoomIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.processBtnZoomIn(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        game.processBtnZoomIn(false);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        game.processBtnZoomIn(false);
                        return true;
                }
                return false;
            }
        });

        Button btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.setArrowBtnState(false, true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        game.setArrowBtnState(false, false);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        game.setArrowBtnState(false, false);
                        return true;
                }
                return false;
            }
        });

        Button btnJump = (Button) findViewById(R.id.btnJump);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.jump();
            }
        });

        Button btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.setArrowBtnState(true, true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        game.setArrowBtnState(true, false);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        game.setArrowBtnState(true, false);
                        return true;
                }
                return false;
            }
        });

        Button btnZoomOut = (Button) findViewById(R.id.btnZoomOut);
        btnZoomOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        game.processBtnZoomOut(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        game.processBtnZoomOut(false);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        game.processBtnZoomOut(false);
                        return true;
                }
                return false;
            }
        });

        Button btnCenterCamera = (Button) findViewById(R.id.btnCenterCamera);
        btnCenterCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.setCamOnHero();
            }
        });

        final TextView textScoreBoard = (TextView) findViewById(R.id.textScoreBoard);
        textScoreBoard.setText("Score: " + game.getScore());

        timeBar = (ProgressBar) findViewById(R.id.timeBar);
        timeBar.setMax((int)game.getLevelMaxTime(level));
        timeBar.setProgress(timeBar.getMax());

        //register observer
        Observer gameObserver = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                if (data instanceof Game.GameEvent) {
                    final Game.GameEvent event = (Game.GameEvent) data;
                    if (event == Game.GameEvent.SCORE_UPDATE)
                    {
                        textScoreBoard.post(new Runnable()
                        {
                            public void run() {
                                textScoreBoard.setText("Score: " + game.getScore());
                            }
                        });
                    }
                    else if ((event == Game.GameEvent.LOSS_BOO) || (event == Game.GameEvent.LOSS_TIMEUP) ||
                            (event == Game.GameEvent.LOSS_OUTOFBOUNDS) || (event == Game.GameEvent.WIN))
                    {
                        /*Thread myThread = new Thread(){
                            @Override
                            public void run() {
                                endGame(event);
                            }
                        };
                        myThread.start();*/
                        endGame(event);
                    }
                }
            }
        };

        game.addObserver(gameObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
        Integer music_id = getLevelMusic(game.getLevel());
        if(music_id != null)
        {
            Music.play(this, music_id);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //onResume we start our timer so it can start when the app comes from the background
        stoptimertask();
        Music.stop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onResume we start our timer so it can start when the app comes from the background
        stoptimertask();
    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, DT);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask()
        {
            public void run()
            {
                game.update(DT);
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        gamePlayView.invalidate();
                        timeBar.setProgress((int)game.getTimeRemaining());
                    }
                });
            }
        };
    }

    private static Integer getLevelMusic(Game.Level level)
    {
        Integer music_id = null;
        if(level != null) {
            switch (level) {
                case SNOWMAN:
                    music_id = R.raw.snowman;
                    break;
                case DESERT:
                    music_id = R.raw.desert;
                    break;
                case DINOSAURS:
                    music_id = R.raw.dinosaurs;
                    break;
                case SHIP:
                    music_id = R.raw.ship;
                    break;
                case JAPAN:
                    music_id = R.raw.japan;
                    break;
                case VOLCANO:
                    music_id = R.raw.volcano;
                    break;
                case TYCOON:
                    music_id = R.raw.tycoon;
                    break;
            }
        }
        return music_id;
    }

    void endGame(Game.GameEvent event)
    {
        //System.out.println("end game");
        Intent intent = new Intent(getApplicationContext(), GameEndActivity.class);
        intent.putExtra(GameEndActivity.KEY_GAMEEND_SCORE,game.getScore());
        intent.putExtra(GameEndActivity.KEY_GAMEEND_TIME,game.getTimeRemaining());
        intent.putExtra(GameEndActivity.KEY_GAMEEND_EVENT,event);
        intent.putExtra(GameEndActivity.KEY_GAMEEND_LEVEL,level);
        startActivity(intent);
        finish();
    }

    void loadSprites(BitmapFactory.Options opts)
    {
        Worm.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.immobile, opts),
                BitmapFactory.decodeResource(getResources(), R.drawable.right_immobile, opts),
                2);
        Flag.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.flag, opts),
                23);
        Coin.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_coin, opts), 8,
                BitmapFactory.decodeResource(getResources(), R.drawable.red_coin, opts), 8);
        Flake.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.large_flake, opts), 1,
                BitmapFactory.decodeResource(getResources(), R.drawable.small_flake, opts), 1);
        Boo.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.boo_immobile, opts), BitmapFactory.decodeResource(getResources(), R.drawable.boo_immobile_right, opts), 1,
                BitmapFactory.decodeResource(getResources(), R.drawable.boo_moving, opts), BitmapFactory.decodeResource(getResources(), R.drawable.boo_moving_right, opts), 4);
        Star.loadSprites(BitmapFactory.decodeResource(getResources(), R.drawable.star, opts), 23);
    }

}
