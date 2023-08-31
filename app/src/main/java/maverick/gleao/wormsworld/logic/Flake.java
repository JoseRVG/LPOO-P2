package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Flake.java - class used to represent a flake, used on the foreground.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Flake extends Body {
    /**
     * Sprite sheet for a large snowflake.
     */
    private static Bitmap spriteSheetLarge;
    /**
     * Frame count for a large snowflake.
     */
    private static int frameCountLarge;

    /**
     * Sprite sheet for a small snowflake.
     */
    private static Bitmap spriteSheetSmall;
    /**
     * Frame count for a small snowflake.
     */
    private static int frameCountSmall;

    /**
     * Frame time for all the flakes' animations.
     */
    private static final int frameTime = 200;

    /**
     * Represents the size of a snowflake.
     */
    enum Size
    {
        /**
         * Large snowflake.
         */
        LARGE,
        /**
         * Small snowflake.
         */
        SMALL
    };

    /**
     * Size of the flake.
     */
    private Size size;

    /**
     * Flake's maximum x velocity.
     */
    private static final int flakeMaxVx = 0;

    /**
     * Flake's maximum y velocity.
     */
    private static final int flakeMaxVy = 30;

    /**
     * Flake's x acceleration due to friction.
     */
    private static final int flakeXAten = 0;


    /**
     * Basic constructor for the Flake class.
     *
     * @param  x Initial x coordinate of the flake on the map.
     * @param  y Initial y coordinate of the flake on the map.
     * @param  size Size of the snowflake.
     */
    public Flake(double x, double y, Size size)
    {
        super(x,y,flakeMaxVx,flakeMaxVy,flakeXAten,generateRandomGravity(),true);
        this.size = size;
        initAnimation();
    }

    /**
     * Generate a random gravity for a flake.
     *
     * @return Random gravity for a flake
     */
    private static int generateRandomGravity()
    {
        return 3*((new Random()).nextInt(10) + 1);
    }

    /**
     * Returns an array with the body's collision rectangles.
     *
     * @return Array containing the body's collision rectangles.
     */
    protected ArrayList<Rect> getCollisionRectangles()
    {
        return new ArrayList<Rect>();
    }

    /**
     * Loads the flakes's sprite sheets and frame counts.
     *
     * @param spriteSheetLarge Sprite sheet for a large flake.
     * @param frameCountLarge Frame count for a large flake.
     * @param spriteSheetSmall Sprite sheet for a small flake.
     * @param frameCountSmall Frame count for a small flake.
     */
    public static void loadSprites(Bitmap spriteSheetLarge, int frameCountLarge, Bitmap spriteSheetSmall, int frameCountSmall)
    {
        Flake.spriteSheetLarge = spriteSheetLarge;
        Flake.frameCountLarge = frameCountLarge;

        Flake.spriteSheetSmall = spriteSheetSmall;
        Flake.frameCountSmall = frameCountSmall;
    }

    /**
     * Returns the flake's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
        if(size == Size.LARGE)
        {
            return spriteSheetLarge;
        }
        else
        {
            return spriteSheetSmall;
        }
    }

    /**
     * Returns the flakes's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
        if(size == Size.LARGE)
        {
            return frameCountLarge;
        }
        else
        {
            return frameCountSmall;
        }
    }

    /**
     * Returns the flakes's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }
}
