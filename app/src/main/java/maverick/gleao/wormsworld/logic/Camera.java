package maverick.gleao.wormsworld.logic;

/**
 * Camera.java - class used to represent the game's camera, used to select which portions of the game's map to show.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
class Camera {
    /**
     * Camera's x position.
     */
    private int x;
    /**
     * Camera's y position.
     */
    private int y;
    /**
     * Maximum value for the camera's x position.
     */
    private int maxX;
    /**
     * Maximum value for the camera's y position.
     */
    private int maxY;
    /**
     * Current width of the camera (number of pixels to be displayed horizontally).
     */
    private int width;
    /**
     * Current height of the camera (number of pixels to be displayed vertically).
     */
    private int height;
    /**
     * Single instance of this class (singleton).
     */
    private static Camera ourInstance = new Camera();

    /**
     * Retrieves the single instance of this class (singleton).
     *
     * @return The single instance of this class.
     */
    static Camera getInstance() {
        return ourInstance;
    }

    /**
     * Basic constructor for the Camera class.
     */
    private Camera() {
        this.x = 0;
        this.y = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Getter method for the camera's x position.
     *
     * @return Camera's x position.
     */
    int getX() {
        return x;
    }

    /**
     * Getter method for the camera's y position.
     *
     * @return Camera's y position.
     */
    int getY() {
        return y;
    }

    /**
     * Setter method for the camera's x position.
     * <p>
     *     This method makes sure that this coordinate does not exceed the maximum or minimum (0) values allowed.
     * </p>
     *
     * @param  x Camera's new x position.
     */
    void setX(int x)
    {
        this.x = x;
        if(this.x < 0)
        {
            this.x = 0;
        }
        else if( (this.x + width - 1) > maxX)
        {
            this.x = maxX - width + 1;
        }
    }

    /**
     * Setter method for the camera's u position.
     * <p>
     *     This method makes sure that this coordinate does not exceed the maximum or minimum (0) values allowed.
     * </p>
     *
     * @param  y Camera's new y position.
     */
    void setY(int y)
    {
        this.y = y;
        if(this.y < 0)
        {
            this.y = 0;
        }
        else if( (this.y + height - 1) > maxY)
        {
            this.y = maxY - height + 1;
        }
    }

    /**
     * Setter method for the camera's maximum x position.
     *
     * @param  maxX Camera's new maximum x position.
     */
    void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    /**
     * Setter method for the camera's maximum y position.
     *
     * @param  maxY Camera's new maximum y position.
     */
    void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    /**
     * Getter method for the camera's width.
     *
     * @return Camera's width.
     */
    int getWidth() {
        return width;
    }

    /**
     * Getter method for that camera's height.
     *
     * @return Camera's height.
     */
    int getHeight() {
        return height;
    }

    /**
     * Setter method for the camera's width.
     *
     * @param  width Camera's new width.
     */
    void setWidth(int width) {
        this.width = width;
    }

    /**
     * Setter method for the camera's height.
     *
     * @param  height Camera's new height.
     */
    void setHeight(int height) {
        this.height = height;
    }

    /**
     * Moves the camera.
     * <p>
     *     This method makes sure the camera doesn't "film" anything outside its area.
     * </p>
     *
     * @param  deltaX Horizontal movement of the camera.
     * @param  deltaY Vertical movement of the camera.
     */
    void move(int deltaX, int deltaY)
    {
        setX(this.x + deltaX);
        setY(this.y + deltaY);
    }

    /**
     * Zooms the camera.
     * <p>
     *     This method makes sure the camera doesn't "film" anything outside its area.
     * </p>
     * <p>
     *     The camera zooms in if the scale is less than 1, and zooms out if the scale is greater 1.
     * </p>
     *
     * @param  scale Scale to be multiplied to the camera's width and height.
     * @return Boolean indicating of the zoom was successful.
     */
    boolean zoom(double scale)
    {
        //Define the limits
        int maxWidth = maxX + 1;
        int maxHeight = maxY + 1;
        int minWidth = maxWidth / 16;
        int minHeight = maxHeight / 16;

        //Check if the zoom is not possible (according to the limits)
        if(width*scale > maxWidth || width*scale < minWidth ||
                height*scale > maxHeight || height*scale < minHeight)
        {
           return false;
        }

        //Update camera dimensions
        width *= scale;
        height *= scale;

        //Update X and Y (camera might have gone offscreen)
        while((x + width - 1) > maxX)
        {
            x--;
        }
        while((y + height - 1) > maxY)
        {
            y--;
        }

        return true;
    }

}