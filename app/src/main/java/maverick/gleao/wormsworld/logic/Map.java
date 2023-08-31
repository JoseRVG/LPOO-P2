package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Map.java - class used to store a game's map.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
class Map {
    /**
     * Represents the type of a block of a map.
     */
    enum Block {
        /**
         * Neutral block that doesn't contain anything: it can be freely traversed by any entity of the game.
         */
        NONE,
        /**
         * Solid block that can't be traversed by any entity of the game.
         */
        NORMAL,
        /**
         * Solid block that can't be traversed by any entity of the game, and that has less friction.
         */
        SLIPPERY,
        /**
         * Neutral block that will contain a star, used to help the hero jump higher and farther.
         */
        STAR
    }

    /**
     * Bitmap containing the map's graphics.
     */
    private Bitmap map;
    /**
     * Bitmap containing the map's background graphics.
     */
    private Bitmap background;
    /**
     * Bitmap containing the type of block in each pixel of the map.
     */
    private Block[][] key;

    /**
     * A constructor for the Map class that receives bitmaps for the map and background's graphics and a special bitmap to load the block type of each pixel.
     *
     * @param  map Bitmap containing the map's graphics.
     * @param  mapKey Bitmap containing the block type of each pixel (the bitmap uses color coding).
     * @param  background Bitmap containing the map's background graphics.
     */
    Map(Bitmap map, Bitmap mapKey, Bitmap background)
    {
        this.map = map;
        this.background = background;
        int width = map.getWidth();
        int height = map.getHeight();
        key = new Block[height][width];
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                key[y][x] = keyToType(mapKey.getPixel(x,y));
            }
        }
    }

    /**
     * Getter method that returns the map's bitmap.
     *
     * @return Bitmap containing the map's graphics.
     */
    Bitmap getGameMap()
    {
        return map;
    }

    /**
     * Getter method that returns the background's bitmap.
     *
     * @return Bitmap containing the map's background graphics.
     */
    Bitmap getBackground()
    {
        return background;
    }

    /**
     * Returns the type of block associated with a color.
     *
     * @param color Color on the bitmap.
     * @return Bitmap containing the map's background graphics.
     */
    private static Block keyToType(int color)
    {
        Block type = null;

        switch(color)
        {
            case Color.WHITE:
                type = Block.NONE;
                break;
            case Color.BLACK:
                type = Block.NORMAL;
                break;
            case Color.CYAN:
                type = Block.SLIPPERY;
                break;
            case Color.MAGENTA:
                type = Block.STAR;
                break;
        }

        return type;
    }

    /**
     * Determines if the block of the map is tangible.
     *
     * @param  x x coordinate on the map.
     * @param  y y coordinate on the map.
     * @return Boolean that indicates if the block of the map is tangible.
     */
    boolean isTangible(int x, int y)
    {
        if(x < 0 || x >= getMapWidth() || y < 0 || y >= getMapHeight())
        {
            return false;
        }


        if((key[y][x] == Block.NONE) || (key[y][x] == Block.STAR))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Determines if a rectangle has one of its pixels colliding with one of the map's solid blocks.
     *
     * @param  r Rectangle to check for collision.
     * @return Boolean that indicates if the rectangle collides with the map.
     */
    boolean collidesWith(Rect r)
    {
        for(int y = r.top; y <= r.bottom; y++)
        {
            for(int x = r.left; x <= r.right; x++)
            {
                if(isTangible(x,y))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the width of the map.
     *
     * @return Width of the map (in pixels).
     */
    int getMapWidth()
    {
        if(key.length == 0)
        {
            return 0;
        }
        return key[0].length;
    }

    /**
     * Returns the height of the map.
     *
     * @return Height of the map (in pixels).
     */
    int getMapHeight()
    {
        return key.length;
    }

    /**
     * Returns the list of stars of the map, associated with each block whose type is STAR.
     *
     * @return List of all the stars of the map.
     */
    ArrayList<Star> getStars()
    {
        ArrayList<Star> stars = new ArrayList<Star>();
        int width = map.getWidth();
        int height = map.getHeight();
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(Block.STAR == key[y][x])
                {
                    Star star = new Star(x,y);
                    stars.add(star);
                }
            }
        }
        return stars;
    }
}
