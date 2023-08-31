package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Worm.java - class used to represent a worm.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Worm extends Body {
    /**
     * Boolean that indicates if the worm is facing the right.
     */
    boolean facingRight;

    /**
     * Sprite sheet for a worm facing the left.
     */
    private static Bitmap spriteSheet;
    /**
     * Sprite sheet for a worm facing the right.
     */
    private static Bitmap rightSpriteSheet;
    /**
     * Frame count for a worm.
     */
    private static int frameCount;
    /**
     * Frame time for all the worms' animations.
     */
    private static final int frameTime = 200;

    //Rectangles that make up a worm
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
    private static final int rectAWidth = 11;
    /**
     * Height of the first collision rectangle.
     */
    private static final int rectAHeight = 13;

    /**
     * x coordinate of the second collision rectangle upper left corner pixel.
     */
    private static final int rectBX = 3;
    /**
     * y coordinate of the second collision rectangle upper left corner pixel.
     */
    private static final int rectBY = 12;
    /**
     * Width of the second collision rectangle.
     */
    private static final int rectBWidth = 8;
    /**
     * Height of the second collision rectangle.
     */
    private static final int rectBHeight = 1;

    /**
     * x coordinate of the third collision rectangle upper left corner pixel.
     */
    private static final int rectCX = 3;
    /**
     * y coordinate of the third collision rectangle upper left corner pixel.
     */
    private static final int rectCY = 13;
    /**
     * Width of the third collision rectangle.
     */
    private static final int rectCWidth = 14;
    /**
     * Height of the third collision rectangle.
     */
    private static final int rectCHeight = 6;

    /**
     * Worm's maximum x velocity.
     */
    private static final int wormMaxVx = 30;

    /**
     * Worm's maximum y velocity.
     */
    private static final int wormMaxVy = 40;

    /**
     * Worm's x acceleration due to friction.
     */
    private static final int wormXAten = 50;

    /**
     * Worm's y acceleration due to gravity.
     */
    private static final int wormGravity = 30;

    /**
     * Basic constructor for the Worm class.
     *
     * @param  x Initial x coordinate of the worm on the map.
     * @param  y Initial y coordinate of the worm on the map.
     */
    public Worm(double x, double y)
    {
        super(x,y,wormMaxVx,wormMaxVy,wormXAten,wormGravity);
        this.facingRight = false;
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

        int spriteWidth = getSpriteWidth();

        int xA = x + rectAX;
        int yA = y + rectAY;
        int xB = x + rectBX;
        int yB = y + rectBY;
        int xC = x + rectCX;
        int yC = y + rectCY;
        if(facingRight)
        {
            xA = x + spriteWidth - rectAX - rectAWidth; //spriteWidth - (rectAX + rectAWidth - 1) - 1
            xB = x + spriteWidth - rectBX - rectBWidth;
            xC = x + spriteWidth - rectCX - rectCWidth;
        }
        Rect A = new Rect(xA, yA, xA + rectAWidth - 1, yA + rectAHeight - 1);
        rects.add(A);

        Rect B = new Rect(xB, yB, xB + rectBWidth - 1, yB + rectBHeight - 1);
        rects.add(B);

        Rect C = new Rect(xC, yC, xC + rectCWidth - 1, yC + rectCHeight - 1);
        rects.add(C);

        return rects;
    }

    /**
     * Moves the worm on the given direction, altering its x velocity.
     *
     * @param  right Boolean that indicates if the worm should move right (false for left).
     */
    void move(boolean right)
    {
        super.move(right);
        if(right != this.facingRight)
        {
            this.turn();
        }
    }

    /**
     * Makes the worm turn to the other direction.
     */
    private void turn()
    {
        this.facingRight = !this.facingRight;
        initAnimation();
    }

    /**
     * Determines if the worm is facing the right.
     *
     * @return Boolean that indicates if the worm is facing the right.
     */
    boolean isFacingRight()
    {
        return this.facingRight;
    }

    /**
     * Loads the worm's sprite sheets and frame counts.
     *
     * @param spriteSheet Sprite sheet for a worm facing the left.
     * @param rightSpriteSheet Sprite sheet for a worm facing the right.
     * @param frameCount Frame count for a worm.
     */
    public static void loadSprites(Bitmap spriteSheet, Bitmap rightSpriteSheet, int frameCount)
    {
        Worm.spriteSheet = spriteSheet;
        Worm.rightSpriteSheet = rightSpriteSheet;
        Worm.frameCount = frameCount;
    }

    /**
     * Returns the worm's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
        if(this.facingRight)
        {
            return rightSpriteSheet;
        }
        else
        {
            return spriteSheet;
        }
    }

    /**
     * Returns the worm's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
        return frameCount;
    }

    /**
     * Returns the worm's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }
}
