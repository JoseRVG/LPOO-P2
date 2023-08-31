package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Star.java - class used to represent a star.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Star extends Body {
    /**
     * Sprite sheet for a star.
     */
    private static Bitmap spriteSheet;
    /**
     * Frame count for a flag.
     */
    private static int frameCount;
    /**
     * Frame time for all the stars' animations.
     */
    private static final int frameTime = 200;

    /**
     * Boolean that indicates if the star is currently visible.
     */
    private boolean visible;
    /**
     * Number of millis since the star became invisible.
     */
    private long timeInvisible;
    /**
     * Maximum number of millis for a star to remain invisible.
     */
    private static final long maxTimeInvisible = 3000;

    //Rectangles that make up a star
    /**
     * x coordinate of the first collision rectangle upper left corner pixel.
     */
    private static final int rectAX = 0;
    /**
     * y coordinate of the first collision rectangle upper left corner pixel.
     */
    private static final int rectAY = 0;
    /**
     * Width of the first collision rectangle.
     */
    private static final int rectAWidth = 25;
    /**
     * Height of the first collision rectangle.
     */
    private static final int rectAHeight = 21;

    /**
     * Star's maximum x velocity.
     */
    private static final int starMaxVx = 0;

    /**
     * Star's maximum y velocity.
     */
    private static final int starMaxVy = 0;

    /**
     * Star's x acceleration due to friction.
     */
    private static final int starXAten = 0;

    /**
     * Star's y acceleration due to gravity.
     */
    private static final int starGravity = 0;

    /**
     * Basic constructor for the Star class.
     *
     * @param  x Initial x coordinate of the star on the map.
     * @param  y Initial y coordinate of the star on the map.
     */
    public Star(double x, double y)
    {
        super(x,y,starMaxVx,starMaxVy,starXAten,starGravity);
        this.visible = true;
        this.timeInvisible = 0;
        initAnimation();
    }

    /**
     * Draws the star on top of the given bitmap, in the appropriate position.
     *
     * @param  gameMap Bitmap that contains the game's image, where the star's sprite will be drawn.
     */
    public void draw(Bitmap gameMap)
    {
        if(visible)
        {
            super.draw(gameMap);
        }
    }

    /**
     * Returns an array with the body's collision rectangles.
     *
     * @return Array containing the body's collision rectangles.
     */
    protected ArrayList<Rect> getCollisionRectangles()
    {
        ArrayList<Rect> rects = new ArrayList<Rect>();

        int x = (int) Math.round(getX());
        int y = (int) Math.round(getY());

        int xA = x + rectAX;
        int yA = y + rectAY;
        Rect A = new Rect(xA, yA, xA + rectAWidth - 1, yA + rectAHeight - 1);
        rects.add(A);

        return rects;
    }

    /**
     * Updates the body's animation.
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    void updateAnim(long deltaT)
    {
        //Update the star's animation
        if(visible)
        {
            super.updateAnim(deltaT);
        }
    }

    /**
     * Updates the star's state.
     * <p>
     *     This function is the one that controls the star's AI, so it become visible again after a certain amount of time (maxTimeInvisible).
     * </p>
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    void updateState(long deltaT)
    {
        if(!visible)
        {
            timeInvisible += deltaT;
            if(timeInvisible >= maxTimeInvisible)
            {
                timeInvisible = 0;
                visible = true;
            }
        }
    }

    /**
     * Makes the star disappear (if it was visible).
     *
     * @return  Boolean that indicates whether the stars was previously visible.
     */
    boolean disappear()
    {
        if(visible)
        {
            this.visible = false;
            return true;
        }
        return false;
    }

    /**
     * Loads the star's sprite sheets and frame counts.
     *
     * @param spriteSheet Sprite sheet for a star.
     * @param frameCount Frame count for a star.
     */
    public static void loadSprites(Bitmap spriteSheet, int frameCount)
    {
        Star.spriteSheet = spriteSheet;
        Star.frameCount = frameCount;
    }

    /**
     * Returns the star's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
       return spriteSheet;
    }

    /**
     * Returns the star's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
       return frameCount;
    }

    /**
     * Returns the star's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }
}
