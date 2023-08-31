package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Animation.java - Class used to animate sprites, stored using a sprite sheet.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
class Animation {
    /**
     * Bitmap containing the sprite sheet.
     */
    Bitmap spriteSheet;
    /**
     * Number of frames in the sprite sheet.
     */
    private int frameCount;
    /**
     * Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    private long frameTime;
    /**
     * Width of a frame in pixels.
     */
    private int frameWidth;
    /**
     * Height of a frame in pixels.
     */
    private int frameHeight;

    /**
     * Number of milliseconds that have passed since the last frame swap.
     */
    private int curTime;
    /**
     * Index of the frame currently being displayed.
     */
    private int curFrame;

    /**
     * A constructor for the Animation class that receives a sprite sheets, its number of frames and the amount of millis to display each frame.
     *
     * @param  spriteSheet Bitmap containing the sprite sheet.
     * @param  frameCount Number of frames in the sprite sheet.
     * @param  frameTime Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    Animation(Bitmap spriteSheet, int frameCount, long frameTime)
    {
        this.spriteSheet = spriteSheet;
        this.frameCount = frameCount;
        this.frameWidth = spriteSheet.getWidth()/frameCount;
        this.frameHeight = spriteSheet.getHeight();
        this.curFrame = 0;
        this.curTime = 0;
        this.frameTime = frameTime;
    }

    /**
     * Draws the current frame onto a specific position on the given bitmap.
     * <p>
     *     It should be noted that only part of the sprite may be drawn on the bitmap and, occasionally,
     *     the sprite will not be drawn at all, if the camera's vision doesn't intersect the sprite.
     * </p>
     *
     * @param  gameMap Bitmap representing the game's image, where the sprite is to be drawn (if possible).
     * @param  spriteMinX x coordinate on the bitmap where the upper left corner of the sprite is to be drawn.
     * @param  spriteMinY y coordinate on the bitmap where the upper left corner of the sprite is to be drawn.
     */
    void drawSprite(Bitmap gameMap, int spriteMinX, int spriteMinY)
    {
        if(spriteSheet == null)
        {
            return;
        }
        Bitmap sprite = Bitmap.createBitmap(spriteSheet,curFrame*frameWidth,0,frameWidth,frameHeight);
        int spriteMaxX = spriteMinX + frameWidth - 1;
        int spriteMaxY = spriteMinY + frameHeight - 1;

        Camera cam = Camera.getInstance();
        int camMinX = cam.getX();
        int camMinY = cam.getY();
        int camMaxX = camMinX + cam.getWidth() - 1;
        int camMaxY = camMinY + cam.getHeight() - 1;

        //Check if at least part of the sprite is to be drawn on the gameMap
        if (spriteMaxX >= camMinX && spriteMaxY >= camMinY &&
                spriteMinX <= camMaxX && spriteMinY <= camMaxY) {
            //Find out which parts of the sprite should be drawn
            int srcMinX = Math.max(spriteMinX, camMinX) - spriteMinX;
            int srcMinY = Math.max(spriteMinY, camMinY) - spriteMinY;
            int srcMaxX = Math.min(spriteMaxX, camMaxX) - spriteMinX;
            int srcMaxY = Math.min(spriteMaxY, camMaxY) - spriteMinY;

            int scrWidth = srcMaxX - srcMinX + 1;
            int scrHeight = srcMaxY - srcMinY + 1;

            int destMinX = Math.max(spriteMinX, camMinX) - camMinX;
            int destMinY = Math.max(spriteMinY, camMinY) - camMinY;

            for (int dy = 0; dy < scrHeight; dy++) {
                for (int dx = 0; dx < scrWidth; dx++) {
                    int pixel = sprite.getPixel(dx + srcMinX, dy + srcMinY);
                    if (pixel != Color.TRANSPARENT) {
                        gameMap.setPixel(dx + destMinX, dy + destMinY, pixel);
                    }
                }
            }
        }
    }

    /**
     * Update the animation's frame.
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    void update(long deltaT)
    {
        curTime += deltaT;
        if(curTime >= frameTime) {
            curTime = 0;
            curFrame++;
            if (curFrame == frameCount) {
                curFrame = 0;
            }
        }
    }

    /**
     * Returns a frame's width.
     *
     * @return width of a frame (in pixels).
     */
    public int getSpriteWidth()
    {
        return this.frameWidth;
    }
}
