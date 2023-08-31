package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Boo.java - class used to represent a Boo, the game's main enemy.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Boo extends Body {
    /**
     * Boolean that indicates if the boo is moving.
     */
    private boolean moving;
    /**
     * Boolean that indicates if the boo is facing the right.
     */
    private boolean facingRight;

    /**
     * Sprite sheet for when the boo is immobile, facing the left.
     */
    private static Bitmap spriteSheetImmobile;
    /**
     * Sprite sheet for when the boo is immobile, facing the right.
     */
    private static Bitmap spriteSheetImmobileRight;
    /**
     * Frame count for when the boo is immobile.
     */
    private static int frameCountImmobile;

    /**
     * Sprite sheet for when the boo is moving, facing the left.
     */
    private static Bitmap spriteSheetMoving;
    /**
     * Sprite sheet for when the boo is moving, facing the right.
     */
    private static Bitmap spriteSheetMovingRight;
    /**
     * Frame count for when the boo is moving.
     */
    private static int frameCountMoving;

    /**
     * Frame time for all the boo's animations.
     */
    private static final int frameTime = 200;

    //Rectangles that make up a boo
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
    private static final int rectAWidth = 26;
    /**
     * Height of the first collision rectangle.
     */
    private static final int rectAHeight = 23;

    /**
     * Boo's maximum x velocity.
     */
    private static final int booMaxVx = 10;

    /**
     * Boo's maximum y velocity.
     */
    private static final int booMaxVy = 10;

    /**
     * Boo's x acceleration due to friction.
     */
    private static final int booXAten = 0;

    /**
     * Boo's y acceleration due to gravity.
     */
    private static final int booGravity = 0;

    /**
     * Basic constructor for the Boo class.
     *
     * @param  x Initial x coordinate of the boo on the map.
     * @param  y Initial y coordinate of the boo on the map.
     */
    public Boo(double x, double y)
    {
        super(x,y,booMaxVx,booMaxVy,booXAten,booGravity,true);
        this.facingRight = false;
        this.moving = false;
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
     * Updates the boo's state, taking into account the hero Worm.
     * <p>
     *     This function is the one that controls the enemy's AI.
     * </p>
     *
     * @param hero Hero worm.
     */
    void updateState(Worm hero)
    {
        //Face the hero
        if(isToTheLeftOf(hero) && !facingRight)
        {
            facingRight = true;
            initAnimation();
        }
        else if(isToTheRightOf(hero) && facingRight)
        {
            facingRight = false;
            initAnimation();
        }

        if(isFacingWorm(hero) && moving)
        {
            this.moving = false;
            initAnimation();
        }
        else if(!isFacingWorm(hero) && !moving)
        {
            this.moving = true;
            initAnimation();
        }

        //Decide the boo's new direction
        if(moving)
        {
            follow(hero,10);
        }
        else
        {
            stopX();
            stopY();
        }
    }


    /**
     * Loads the boo's sprite sheets and frame counts.
     *
     * @param spriteSheetImmobile Sprite sheet for when the boo is immobile, facing the left.
     * @param spriteSheetImmobileRight Sprite sheet for when the boo is immobile, facing the right.
     * @param frameCountImmobile Frame count for when the boo is immobile.
     * @param spriteSheetMoving Sprite sheet for when the boo is moving, facing the left.
     * @param spriteSheetMovingRight Sprite sheet for when the boo is moving, facing the right.
     * @param frameCountMoving Frame count for when the boo is moving.
     */
    public static void loadSprites(Bitmap spriteSheetImmobile, Bitmap spriteSheetImmobileRight, int frameCountImmobile,
                                   Bitmap spriteSheetMoving, Bitmap spriteSheetMovingRight, int frameCountMoving)
    {
        Boo.spriteSheetImmobile = spriteSheetImmobile;
        Boo.spriteSheetImmobileRight = spriteSheetImmobileRight;
        Boo.frameCountImmobile = frameCountImmobile;

        Boo.spriteSheetMoving = spriteSheetMoving;
        Boo.spriteSheetMovingRight = spriteSheetMovingRight;
        Boo.frameCountMoving = frameCountMoving;
    }

    /**
     * Returns the boo's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected Bitmap getSpriteSheet()
    {
        if(!moving)
        {
            if(!facingRight)
            {
                return spriteSheetImmobile;
            }
            else
            {
                return spriteSheetImmobileRight;
            }
        }
        else
        {
            if(!facingRight)
            {
                return spriteSheetMoving;
            }
            else
            {
                return spriteSheetMovingRight;
            }
        }
    }

    /**
     * Returns the boo's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected int getFrameCount()
    {
        if(!moving)
        {
            return frameCountImmobile;
        }
        else
        {
            return frameCountMoving;
        }
    }

    /**
     * Returns the boo's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected long getFrameTime()
    {
        return frameTime;
    }

    /**
     * Indicates if this boo is facing the worm given as a parameter.
     *
     * @param w Worm used for the test.
     * @return Boolean indicating if this boo is facing the worm.
     */
    private boolean isFacingWorm(Worm w)
    {
        if(w.isFacingRight() && !facingRight && isToTheRightOf(w))
        {
            return true;
        }
        if(!w.isFacingRight() && facingRight && isToTheLeftOf(w))
        {
            return true;
        }
        return false;
    }
}
