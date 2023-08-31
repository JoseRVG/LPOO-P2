package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;

/**
 * Drawable.java - interface for an object that can be drawn onto the game's image.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
interface Drawable {
    /**
     * Draws the object on top of the given bitmap, in the appropriate position.
     *
     * @param  gameMap Bitmap that contains the game's image, where the sprite will be drawn.
     */
    void draw(Bitmap gameMap);
}
