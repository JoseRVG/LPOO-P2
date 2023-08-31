package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Flag.java - class used to represent a flag, the game's goal.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Flag extends Body {
    /**
     * Sprite sheet for a flag.
     */
    private static Bitmap spriteSheet;
    /**
     * Frame count for a flag.
     */
    private static int frameCount;
    /**
     * Frame time for all the flags' animations.
     */
    private static final int frameTime = 200;

    //Rectangles that make up a flag
    /**
     * x coordinate of the first collision rectangle upper left corner pixel.
     */
    final int rectAX = 29;
    /**
     * y coordinate of the first collision rectangle upper left corner pixel.
     */
    final int rectAY = 0;
    /**
     * Width of the first collision rectangle.
     */
    final int rectAWidth = 11;
    /**
     * Height of the first collision rectangle.
     */
    final int rectAHeight = 50;

    /**
     * Flag's maximum x velocity.
     */
    private static final int flagMaxVx = 0;

    /**
     * Flag's maximum y velocity.
     */
    private static final int flagMaxVy = 0;

    /**
     * Flag's x acceleration due to friction.
     */
    private static final int flagXAten = 0;

    /**
     * Flag's y acceleration due to gravity.
     */
    private static final int flagGravity = 0;

    /**
     * Basic constructor for the Flag class.
     *
     * @param  x Initial x coordinate of the flag on the map.
     * @param  y Initial y coordinate of the flag on the map.
     */
    public Flag(double x, double y)
    {
        super(x,y,flagMaxVx,flagMaxVy,flagXAten,flagGravity);
        initAnimation();
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
     * Loads the flag's sprite sheets and frame counts.
     *
     * @param spriteSheet Sprite sheet for a flag.
     * @param frameCount Frame count for a flag.
     */
    public static void loadSprites(Bitmap spriteSheet, int frameCount)
    {
        Flag.spriteSheet = spriteSheet;
        Flag.frameCount = frameCount;
    }

    /**
     * Returns the flag's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
        return spriteSheet;
    }

    /**
     * Returns the flag's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
        return frameCount;
    }

    /**
     * Returns the flag's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }
}
