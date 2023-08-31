package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

/**
 * Game.java - class used to represent a game.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Game extends Observable {
    /**
     * The game's map.
     */
    private Map map;
    /**
     * The game's hero.
     */
    private Worm hero;
    /**
     * The game's destination.
     */
    private Flag destination;
    /**
     * The game's enemy.
     */
    private Boo boo;
    /**
     * Array containing all the game's coins.
     */
    private LinkedList<Coin> coins;
    /**
     * Array containing all the game's stars.
     */
    private ArrayList<Star> stars;
    /**
     * Current score of the game (in terms of the total coin value).
     */
    private int score;
    /**
     * The game's foreground.
     */
    private Foreground foreground;

    //Button flags
    /**
     * Boolean that indicates if the game's left button is being pressed.
     */
    private boolean pressingLeft;
    /**
     * Boolean that indicates if the game's right button is being pressed.
     */
    private boolean pressingRight;
    /**
     * Boolean that indicates if the game's zoom in button is being pressed.
     */
    private boolean zoomingIn;
    /**
     * Boolean that indicates if the game's zoom out button is being pressed.
     */
    private boolean zoomingOut;

    /**
     * Object used for thread synchronization.
     */
    private Object lock1 = new Object();

    /**
     * Represents the level used for the game.
     */
    public enum Level{
        /**
         * Level taking place in a sinister ship...
         */
        SHIP,
        /**
         * Level taking place in a winter wonderland!
         */
        SNOWMAN,
        /**
         * Level taking place in a jurassic environment.
         */
        DINOSAURS,
        /**
         * Level taking place in some ruins on a barren desert...
         */
        DESERT,
        /**
         * Level taking place in a dangerous construction site.
         */
        TYCOON,
        /**
         * Level taking place in a blazing volcano!
         */
        VOLCANO,
        /**
         * Level taking place in a traditional japanese town.
         */
        JAPAN
    };

    /**
     * Array containing of the available levels.
     */
    static final Level[] levels = {Level.SHIP, Level.SNOWMAN, Level.DINOSAURS, Level.DESERT, Level.TYCOON, Level.VOLCANO, Level.JAPAN};

    /**
     * Level currently being played.
     */
    private Game.Level level;

    /**
     * Represents a game event (for observer notification).
     */
    public enum GameEvent{
        /**
         * Event to update the coin score (when the hero collects a coin).
         */
        SCORE_UPDATE,
        /**
         * Event for a game victory, when the hero reaches the flag on time.
         */
        WIN,
        /**
         * Event for a game loss, when the boo touches the hero.
         */
        LOSS_BOO,
        /**
         * Event for a game loss, when the time is up.
         */
        LOSS_TIMEUP,
        /**
         * Event for a game loss, when the worms falls out of bounds.
         */
        LOSS_OUTOFBOUNDS
    };

    /**
     * Coordinate (x and y) used for objects which have not been placed on the map yet.
     */
    private static final int deadCoordinate = -50;

    /**
     * Number of millis left for the game to end in a loss due to a timeout.
     */
    private long timeRemaining;
    /**
     * Default duration (in millis) for a game on a "normal" level.
     */
    private static final long defaultMaxTime = 180000;

    /**
     * Default duration (in millis) for a game on a large level.
     */
    private static final long defaultLargeMaxTime = 300000;

    /**
     * Minimum number of coins for a game on a "normal" level.
     */
    private static final int minCoins = 3;
    /**
     * Range of the number of coins for a game on a "normal" level.
     */
    private static final int coinRange = 20;

    /**
     * Minimum number of coins for a game on a large level.
     */
    private static final int largeMinCoins = 10;
    /**
     * Range of the number of coins for a game on a large level.
     */
    private static final int largeCoinRange = 30;

    /**
     * Basic constructor for the Game class.
     *
     * @param map Bitmap containing the map's graphics.
     * @param mapKey Bitmap containing the block type of each pixel (the bitmap uses color coding).
     * @param background Bitmap containing the map's background graphics.
     * @param level Level currently being played.
     */
    public Game(Bitmap map, Bitmap mapKey, Bitmap background, Game.Level level)
    {
        this.map = new Map(map,mapKey,background);
        synchronized (lock1)
        {
            Camera.getInstance().setMaxX(map.getWidth() - 1);
            Camera.getInstance().setMaxY(map.getHeight() - 1);
            Camera.getInstance().setWidth((int) Math.round(map.getWidth() / 2));
            Camera.getInstance().setHeight((int) Math.round(map.getHeight() / 2));
        }
        this.level = level;
        hero = new Worm(deadCoordinate,deadCoordinate);
        destination = new Flag(deadCoordinate,deadCoordinate);
        boo = new Boo(deadCoordinate,deadCoordinate);
        coins = new LinkedList<Coin>();
        stars = this.map.getStars();
        this.score = 0;
        this.foreground = new Foreground(this.map);
        if(levelUsesFlakes(level)) {
            this.foreground.setActiveFlakes(true);
        }

        this.pressingLeft = false;
        this.pressingRight = false;
        this.zoomingIn = false;
        this.zoomingOut = false;

        this.timeRemaining = getLevelMaxTime(level);

        placeBodies();
        placeCoins();
    }

    /**
     * Retrieves a bitmap with all of the game's graphics, background excluded.
     *
     * @return Bitmap with all of the game's graphics, background excluded.
     */
    public Bitmap getGameMap()
    {
        Bitmap gameMap = map.getGameMap();
        if(map != null)
        {
            synchronized (lock1) {
                Camera cam = Camera.getInstance();
                gameMap = Bitmap.createBitmap(gameMap, cam.getX(), cam.getY(), Math.min(cam.getWidth(), gameMap.getWidth()), Math.min(cam.getHeight(), gameMap.getHeight()));
                gameMap = gameMap.copy(gameMap.getConfig(), true);
                drawBodies(gameMap);
                foreground.draw(gameMap);
            }
        }
        return gameMap;
    }

    /**
     * Retrieves a bitmap of the game's background.
     *
     * @return Bitmap of the game's background.
     */
    public Bitmap getBackground() {
        Bitmap background = map.getBackground();
        if (background != null) {
            synchronized (lock1) {
                Camera cam = Camera.getInstance();
                background = Bitmap.createBitmap(background, cam.getX(), cam.getY(), Math.min(cam.getWidth(), background.getWidth()), Math.min(cam.getHeight(), background.getHeight()));
            }
        }
        return background;
    }

    /**
     * Moves the camera.
     *
     * @param  deltaX Horizontal movement of the camera.
     * @param  deltaY Vertical movement of the camera.
     */
    public void moveCamera(int deltaX, int deltaY)
    {
        synchronized (lock1)
        {
            Camera cam = Camera.getInstance();
            cam.move(deltaX, deltaY);
        }
    }

    /**
     * Randomly places all of the game's bodies.
     */
    private void placeBodies()
    {
        ArrayList<Body> collidables = new ArrayList<Body>();
        //Place the hero worm
        hero.placeBody(map, collidables);
        collidables.add(hero);
        //Place the destination flag
        destination.placeBody(map, collidables);
        collidables.add(destination);

        //Place the boo
        boo.placeBody(map, collidables);
    }

    /**
     * Randomly places all of the game's coin.
     */
    private void placeCoins()
    {
        Random rand = new Random();
        int n_coins = (rand.nextInt(getCoinRange(level)) + getMinCoins(level));

        //Place the yellow coins
        ArrayList<Body> collidables = new ArrayList<Body>();
        collidables.add(hero);
        collidables.add(destination);
        collidables.add(boo);
        for(int i = 0; i < n_coins; i++)
        {
            Coin coin;
            if(rand.nextInt(50) <= 10)
            {
                coin = new Coin(deadCoordinate,deadCoordinate, Coin.Color.RED);
            }
            else
            {
                coin = new Coin(deadCoordinate,deadCoordinate, Coin.Color.YELLOW);
            }
            coin.placeBody(map, collidables);
            collidables.add(coin);
            coins.add(coin);
        }
    }

    /**
     * Draws all of the game's bodies on top of the given bitmap, in the appropriate position.
     *
     * @param  gameMap Bitmap that contains the game's image, where the bodies' sprites will be drawn.
     */
    private void drawBodies(Bitmap gameMap)
    {
        hero.draw(gameMap);
        destination.draw(gameMap);
        boo.draw(gameMap);
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            coin.draw(gameMap);
        }
        for (int i = 0; i < stars.size(); i++) {
            Star star = stars.get(i);
            star.draw(gameMap);
        }
    }

    /**
     * Updates all of the game's state, namely all of the game's bodies's positions and animations.
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    public void update(long deltaT)
    {
        //Update the time
        this.timeRemaining -= deltaT;
        if(this.timeRemaining <= 0)
        {
            setChanged();
            notifyObservers(GameEvent.LOSS_TIMEUP);
            return;
        }

        //Process camera buttons
        if(zoomingIn && !zoomingOut)
        {
            synchronized (lock1)
            {
                Camera.getInstance().zoom(0.8);
            }
        }
        else if(zoomingOut && !zoomingIn)
        {
            synchronized (lock1)
            {
                Camera.getInstance().zoom(1.2);
            }
        }

        //Update the hero
        if(pressingLeft && !pressingRight)
        {
            hero.move(false);
        }
        else if(pressingRight && !pressingLeft)
        {
            hero.move(true);
        }

        hero.updateAnim(deltaT);
        ArrayList<Body> collidables = new ArrayList<Body>();
        hero.updatePos(deltaT,map,collidables);

        if(hero.isOutofYBounds(map.getMapHeight() - 1))
        {
            setChanged();
            notifyObservers(GameEvent.LOSS_OUTOFBOUNDS);
            return;
        }

        //Update the destination
        destination.updateAnim(deltaT);

        if(hero.colidesWith(destination))
        {
            setChanged();
            notifyObservers(GameEvent.WIN);
            return;
        }

        //Update the boo
        boo.updateState(hero);
        boo.updateAnim(deltaT);
        boo.updatePos(deltaT,map,collidables);

        if(hero.colidesWith(boo))
        {
            setChanged();
            notifyObservers(GameEvent.LOSS_BOO);
            return;
        }

        //Update the coins
        for(Iterator<Coin> i = coins.iterator(); i.hasNext() ; )
        {
            Coin coin = i.next();
            //Test for collision between hero and coin
            if(hero.colidesWith(coin))
            {
                this.score += coin.getValue();
                setChanged();
                notifyObservers(GameEvent.SCORE_UPDATE);
                i.remove();
            }
            else
            {
                coin.updateAnim(deltaT);
            }
        }

        //Update the stars
        for(int i = 0; i < stars.size(); i++)
        {
            Star star = stars.get(i);
            //Test for collision between hero and star
            if(hero.colidesWith(star))
            {
                if(star.disappear())
                {
                    hero.restoreJumps();
                }
            }
            else
            {
                star.updateAnim(deltaT);
            }
            star.updateState(deltaT);
        }

        //Update the foreground
        foreground.update(deltaT);
    }

    /**
     * Updates one of the arrow button's state (left or right button).
     *
     * @param  right Boolean that indicates if the right button is tp be updated (false for the left).
     * @param  newState New state for the button (true for being pressed).
     */
    public void setArrowBtnState(boolean right, boolean newState)
    {
        if(right)
        {
            this.pressingRight = newState;
        }
        else
        {
            this.pressingLeft = newState;
        }
    }

    /**
     * Makes the hero jump (if possible).
     */
    public void jump()
    {
        hero.jump();
    }

    /**
     * Updates the zoom in button's state.
     *
     * @param  newState New state for the button (true for being pressed).
     */
    public void processBtnZoomIn(boolean newState)
    {
        this.zoomingIn = newState;
    }

    /**
     * Updates the zoom out button's state.
     *
     * @param  newState New state for the button (true for being pressed).
     */
    public void processBtnZoomOut(boolean newState)
    {
        this.zoomingOut = newState;
    }

    /**
     * Getter method for the game's score.
     *
     * @return Game's score (coin total value).
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Getter method for the game's time remaining.
     *
     * @return Game's time remaining.
     */
    public long getTimeRemaining()
    {
        return this.timeRemaining;
    }

    /**
     * Centers the camera on the hero.
     */
    public void setCamOnHero()
    {
        synchronized (lock1)
        {
            hero.centerCamera();
        }
    }

    /**
     * Getter method for the game's level.
     *
     * @return Current level being played.
     */
    public Game.Level getLevel()
    {
        return this.level;
    }

    /**
     * Determines if the current level has snowflakes on the foreground.
     *
     * @param level Level currently being played
     * @return Boolean indicating if the current level has snowflakes on the foreground.
     */
    private static boolean levelUsesFlakes(Game.Level level)
    {
        if(level == Level.SNOWMAN)
        {
            return true;
        }
        return false;
    }

    /**
     * Retrieves the duration of a game, on a given level.
     *
     * @param level Level currently being played
     * @return Duration of a game, on a given level (in millis).
     */
    public static long getLevelMaxTime(Game.Level level)
    {
        if((level == Level.TYCOON) || (level == Level.JAPAN))
        {
            return defaultLargeMaxTime;
        }
        return defaultMaxTime;
    }

    /**
     * Retrieves the minimum number of coins of a game, on a given level.
     *
     * @param level Level currently being played
     * @return Minimum number of coins of a game, on a given level.
     */
    private static int getMinCoins(Game.Level level)
    {
        if((level == Level.TYCOON) || (level == Level.JAPAN))
        {
            return largeMinCoins;
        }
        return minCoins;
    }

    /**
     * Retrieves the range for the number of coins of a game, on a given level.
     *
     * @param level Level currently being played
     * @return Range for the number of coins of a game, on a given level.
     */
    private static int getCoinRange(Game.Level level)
    {
        if((level == Level.TYCOON) || (level == Level.JAPAN))
        {
            return largeCoinRange;
        }
        return coinRange;
    }
}
