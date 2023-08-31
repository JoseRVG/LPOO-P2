package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Foreground.java - class used to represent the foreground.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
class Foreground implements Drawable {
    /**
     * The game's map.
     */
    private Map map;
    /**
     * Boolean that indicates if the foreground should produce more snowflakes.
     */
    private boolean activeFlakes;
    /**
     * Structure that contains the snowflakes currently being shown on the foreground.
     */
    private LinkedList<Flake> flakes;
    /**
     * Number of seconds between the spawning of two consecutive snowflakes.
     */
    private static final int flakeSpawnTime = 100;
    /**
     * Number of milliseconds that have passed since the spawning of the last snowflake.
     */
    private int curTime;

    /**
     * Initial value for a flake's y coordinate when it is created.
     */
    private static final int flakeYI = -50;

    /**
     * Basic constructor for the Foreground class.
     *
     * @param  map The game's map.
     */
    Foreground(Map map)
    {
        this.map = map;
        this.activeFlakes = false;
        this.flakes = new LinkedList<>();
        this.curTime = 0;
    }

    /**
     * Enables/Disables the production of new snowflakes.
     *
     * @param  activeFlakes Boolean that indicates if more snowflakes should be created.
     */
    void setActiveFlakes(boolean activeFlakes)
    {
        this.activeFlakes = activeFlakes;
    }

    /**
     * Draws the foreground on top of the given bitmap.
     *
     * @param  gameMap Bitmap that contains the game's image, where the foreground will be drawn.
     */
    public void draw(Bitmap gameMap)
    {
        synchronized (flakes) {
            for(Flake snowflake: flakes)
            {
                snowflake.draw(gameMap);
            }
        }
    }

    /**
     * Updates the foreground's objects (position and animations).
     * <p>
     *     It is also responsible for creating new snowflakes and deleting the ones that have reached the bottom of the screen.
     * </p>
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    public void update(long deltaT)
    {
        if(activeFlakes) {
            curTime += deltaT;

            //Create more snowflakes
            while (curTime >= flakeSpawnTime) {
                curTime -= flakeSpawnTime;
                Flake snowflake;
                Random rand = new Random();
                if (rand.nextInt(20) == 0) {
                    snowflake = new Flake(rand.nextInt(map.getMapWidth()), flakeYI, Flake.Size.LARGE);
                } else {
                    snowflake = new Flake(rand.nextInt(map.getMapWidth()), flakeYI, Flake.Size.SMALL);
                }
                synchronized (flakes) {
                    flakes.add(snowflake);
                }
            }
        }

        //Update all snowflakes' positions
        synchronized (flakes) {
            for (int i = 0; i < flakes.size(); i++) {
                Flake snowflake = flakes.get(i);
                ArrayList<Body> collidables = new ArrayList<Body>();
                snowflake.updatePos(deltaT, map, collidables);
                snowflake.updateAnim(deltaT);
                if (snowflake.getY() >= map.getMapHeight()) {
                    flakes.remove(snowflake);
                    i--;
                }
            }
        }
    }
}
