package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Coin.java - class used to represent a coin, used to boost the player's score.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Coin extends Body {
    /**
     * Sprite sheet for a yellow coin.
     */
    private static Bitmap spriteSheetYellow;
    /**
     * Frame count for a yellow coin.
     */
    private static int frameCountYellow;

    /**
     * Sprite sheet for a red coin.
     */
    private static Bitmap spriteSheetRed;
    /**
     * Frame count for a red coin.
     */
    private static int frameCountRed;

    /**
     * Frame time for all the coin's animations.
     */
    private static final int frameTime = 200;

    /**
     * Represents the type of coin, distinguished by its color.
     */
    enum Color
    {
        /**
         * Yellow coin, worth 1 point.
         */
        YELLOW,
        /**
         * Red coin, worth 5 point.
         */
        RED
    };

    /**
     * HashMap used to map each coin's color to its value.
     */
    private static HashMap<Color,Integer> colorToValues;
    static
    {
        colorToValues = new HashMap<Color,Integer>();
        colorToValues.put(Color.YELLOW, 1);
        colorToValues.put(Color.RED, 5);
    }

    /**
     * The type of the coin.
     */
    private Coin.Color color;

    //Rectangles that make up a coin
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
    private static final int rectAWidth = 14;
    /**
     * Height of the first collision rectangle.
     */
    private static final int rectAHeight = 16;

    /**
     * Coin's maximum x velocity.
     */
    private static final int coinMaxVx = 0;

    /**
     * Coin's maximum y velocity.
     */
    private static final int coinMaxVy = 0;

    /**
     * Coin's x acceleration due to friction.
     */
    private static final int coinXAten = 0;

    /**
     * Coin's y acceleration due to gravity.
     */
    private static final int coinGravity = 0;

    /**
     * Basic constructor for the Coin class.
     *
     * @param  x Initial x coordinate of the coin on the map.
     * @param  y Initial y coordinate of the coin on the map.
     * @param color The type of coin.
     */
    public Coin(double x, double y, Coin.Color color)
    {
        super(x,y,coinMaxVx,coinMaxVy,coinXAten,coinGravity);
        this.color = color;
        initAnimation();
    }

    /**
     * Retrieves the coin's value.
     *
     * @return  Integer representing the coin's value.
     */
    int getValue()
    {
        return colorToValues.get(this.color);
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
     * Loads the coin's sprite sheets and frame counts.
     *
     * @param spriteSheetYellow Sprite sheet for a yellow coin.
     * @param frameCountYellow Frame count for a yellow coin.
     * @param spriteSheetRed Sprite sheet for a red coin.
     * @param frameCountRed Frame count for a red coin.
     */
    public static void loadSprites(Bitmap spriteSheetYellow, int frameCountYellow, Bitmap spriteSheetRed, int frameCountRed)
    {
        Coin.spriteSheetYellow = spriteSheetYellow;
        Coin.frameCountYellow = frameCountYellow;

        Coin.spriteSheetRed = spriteSheetRed;
        Coin.frameCountRed = frameCountRed;
    }

    /**
     * Returns the coin's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
        if(this.color == Color.YELLOW)
        {
            return spriteSheetYellow;
        }
        else
        {
            return spriteSheetRed;
        }
    }

    /**
     * Returns the coin's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
        if(this.color == Color.YELLOW)
        {
            return frameCountYellow;
        }
        else
        {
            return frameCountRed;
        }
    }

    /**
     * Returns the coin's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }
}
