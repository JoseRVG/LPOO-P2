package maverick.gleao.wormsworld.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import maverick.gleao.wormsworld.logic.Game;

public class GamePlayView extends View {
    private static final String TAG = "GamePlayView";

    static Game game;
    private float oldX;
    private float oldY;

    public GamePlayView(Context context, Game game) {
        super(context);
        this.game = game;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int orientation = getResources().getConfiguration().orientation;

        // Draw the background...
        Paint paintBackground = new Paint();

        Bitmap background = game.getBackground();
        if(background != null) {
            canvas.drawBitmap(background, new Rect(0, 0, background.getWidth(), background.getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()), paintBackground);
        }

        //Draw the game map
        Paint paintGameMap = new Paint();

        Bitmap gameMap = game.getGameMap();
        if(gameMap != null) {
            canvas.drawBitmap(gameMap, new Rect(0, 0, gameMap.getWidth(), gameMap.getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()), paintGameMap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.d(TAG, "onTouchEvent - ACTION_DOWN: x = " + event.getX() + ", y = " + event.getY());

            oldX = event.getX();
            oldY = event.getY();

            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            //Read the new position
            float newX = event.getX();
            float newY = event.getY();
            float deltaX = newX - oldX;
            float deltaY = newY - oldY;

            Log.d(TAG, "onTouchEvent - ACTION_MOVE: dx " + deltaX + ", dy = " + deltaY);

            //Move the camera
            game.moveCamera((int)(-deltaX),(int)(-deltaY));

            //Update the map
            invalidate();

            //Update the old position
            oldX = newX;
            oldY = newY;

            return true;
        }
        return super.onTouchEvent(event);
    }
}
