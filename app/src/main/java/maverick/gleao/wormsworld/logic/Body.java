package maverick.gleao.wormsworld.logic;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Body.java - class used to represent a physical object, which obeys the laws of physics.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
abstract class Body implements Drawable {
    /**
     * Body's x position.
     */
    private double x;
    /**
     * Body's y position.
     */
    private double y;
    /**
     * Body's x velocity.
     */
    private double vx;
    /**
     * Body's y velocity.
     */
    private double vy;
    /**
     * Body's x acceleration.
     */
    private double ax;
    /**
     * Body's y acceleration.
     */
    private double ay;

    /**
     * Body's maximum x velocity.
     */
    private double maxVx;
    /**
     * Body's maximum y velocity.
     */
    private double maxVy;

    /**
     * Body's x acceleration due to friction.
     */
    private double xAten;

    /**
     * Body's y acceleration due to gravity.
     */
    private double gravity;

    /**
     * Body's former x position.
     */
    private double oldX;
    /**
     * Body's former y position.
     */
    private double oldY;

    /**
     * Indicates that a body is transparent (in that case, collisions with the map are to be ignored).
     */
    private boolean transparent;

    /**
     * Number of times a body can jump.
     */
    private int n_jumps;
    /**
     * Maximum number of times a body can jump.
     */
    private static final int max_jumps = 2;
    /**
     * Object used to control the body's animation.
     */
    private Animation anim;

    /**
     * Value to be added to the x velocity when the body moves.
     */
    private static final int move_x = 30;
    /**
     * Value to be added to the y velocity when the body jumps.
     */
    private static final int jump_y = 40;

    /**
     * Basic constructor for the Body class.
     *
     * @param  x Initial x coordinate of the body on the map.
     * @param  y Initial y coordinate of the body on the map.
     * @param  maxVx Body's maximum x velocity.
     * @param  maxVy Body's maximum y velocity.
     * @param  xAten Body's x acceleration due to friction.
     * @param  gravity Body's y acceleration due to gravity.
     */
    Body(double x, double y, double maxVx, double maxVy, double xAten, double gravity) {
        this.x = x;
        this.y = y;
        this.maxVx = maxVx;
        this.maxVy = maxVy;
        this.xAten = xAten;
        this.gravity = gravity;
        this.transparent = false;
        this.n_jumps = this.max_jumps;
        initAnimation();
    }

    /**
     * More generic constructor for the Body class used for transparent bodies.
     *
     * @param  x Initial x coordinate of the body on the map.
     * @param  y Initial y coordinate of the body on the map.
     * @param  maxVx Body's maximum x velocity.
     * @param  maxVy Body's maximum y velocity.
     * @param  xAten Body's x acceleration due to friction.
     * @param  gravity Body's y acceleration due to gravity.
     * @param  transparent Indicates that a body is transparent.
     */
    Body(double x, double y, double maxVx, double maxVy, double xAten, double gravity, boolean transparent) {
        this(x,y,maxVx,maxVy,xAten,gravity);
        this.transparent = transparent;
    }

    /**
     * Updates a body's position taken into account collision with the map and with a group of other bodies.
     *
     * @param  deltaT Time (in millis) since the last update.
     * @param  map The game's map.
     * @param  collidables Bodies that this body must take into account during collision detection.
     */
    void updatePos(long deltaT, Map map, ArrayList<Body> collidables) {
        //Store the old values
        this.oldX = this.x;
        this.oldY = this.y;

        //Update the velocity
        this.vx += (deltaT / 1000.0) * this.ax;
        if (this.vx > this.maxVx) {
            this.vx = this.maxVx;
        }
        if (this.vx < -this.maxVx) {
            this.vx = -this.maxVx;
        }

        if(this.vx > 0)
        {
            this.vx = Math.max(0,this.vx - this.xAten*(deltaT / 1000.0));
        }
        else if(this.vx < 0)
        {
            this.vx = Math.min(0,this.vx + this.xAten*(deltaT / 1000.0));
        }

        //Update the y acceleration (gravity)
        if (!isOnGround(map,collidables))
        {
            this.ay = this.gravity;
        }

        this.vy += (deltaT / 1000.0) * this.ay;
        if (this.vy > this.maxVy) {
            this.vy = this.maxVy;
        }
        if (this.vy < -this.maxVy) {
            this.vy = -this.maxVy;
        }

        //Update the position
        this.x += (deltaT / 1000.0) * this.vx;
        this.y += (deltaT / 1000.0) * this.vy;

        //Deterine if the body colides before reaching its goal
        ArrayList<Vec2<Integer> > trajPoints = getTrajPoints();
        if (trajPoints.size() > 1) {
            setX(trajPoints.get(0).getX());
            setY(trajPoints.get(0).getY());

            for (int i = 1; i < trajPoints.size(); i++) {
                Vec2<Integer> point = trajPoints.get(i);
                setX(point.getX());
                setY(point.getY());
                boolean collision = colidesWith(map,collidables);
                if (collision)
                {
                    if(movedX())
                    {
                        stopX();
                    }
                    if(movedY())
                    {
                        stopY();
                    }
                    resetPos();
                    restoreJumps();
                    break;
                }
            }
        }
    }

    /**
     * Stops a body in the x axis (sets to 0 its x velocity and acceleration).
     */
    void stopX() {
        this.vx = 0;
        this.ax = 0;
    }

    /**
     * Stops a body in the y axis (sets to 0 its y velocity and acceleration).
     */
    void stopY()
    {
        this.vy=0;
        this.ay=0;
    }

    /**
     * Resets a body's x and y position.
     */
    private void resetPos()
    {
        resetX();
        resetY();
    }

    /**
     * Resets a body's x position.
     */
    private void resetX()
    {
        this.x = this.oldX;
    }

    /**
     * Resets a body's y position.
     */
    private void resetY()
    {
        this.y = this.oldY;
    }

    /**
     * Indicates if the body's last movement altered its position along the x axis.
     *
     * @return Boolean that indicates if the body moved horizontally.
     */
    private boolean movedX()
    {
        return (x != oldX);
    }

    /**
     * Indicates if the body's last movement altered its position along the y axis.
     *
     * @return Boolean that indicates if the body moved vertically.
     */
    private boolean movedY()
    {
        return (y != oldY);
    }

    /**
     * Getter method for the body's x position.
     *
     * @return Body's x position.
     */
    protected double getX()
    {
        return x;
    }

    /**
     * Getter method for the body's y position.
     *
     * @return Body's y position.
     */
    protected double getY()
    {
        return y;
    }

    /**
     * Setter method for the body's x position.
     *
     * @param  x Body's new x position.
     */
    protected void setX(int x)
    {
        this.oldX = this.x;
        this.x = (double)x;
    }

    /**
     * Setter method for the body's y position.
     *
     * @param  y Body's new y position.
     */
    protected void setY(int y)
    {
        this.oldY = this.y;
        this.y = (double)y;
    }

    /**
     * Indicates if the body collides with the map.
     *
     * @param  m The game's map.
     * @return Boolean that indicates if the body collides with the map.
     */
    boolean colidesWith(Map m)
    {
        if(transparent)
        {
            return false;
        }
        ArrayList<Rect> tRects = getCollisionRectangles();

        for(int i = 0; i < tRects.size(); i++)
        {
            if(m.collidesWith(tRects.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicates if this body collides with the another body given as a parameter.
     *
     * @param  b The body used for the collision test.
     * @return Boolean that indicates if the body collides with the other body.
     */
    boolean colidesWith(Body b)
    {
        //An object cannot collide with itself!
        if(this == b)
        {
            return false;
        }
        ArrayList<Rect> tRects = getCollisionRectangles();
        ArrayList<Rect> wRects = b.getCollisionRectangles();

        for(int i = 0; i < tRects.size(); i++)
        {
            for(int j = 0; j < wRects.size(); j++)
            {
                if(Rect.intersects(tRects.get(i),wRects.get(j)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Indicates if this body collides with any of bodies of the given array parameter.
     *
     * @param  collidables The bodies used for the collision test.
     * @return Boolean that indicates if the body collides with any of the other bodies.
     */
    boolean colidesWith(ArrayList<Body> collidables)
    {
        for(Body body : collidables)
        {
            if(colidesWith(body))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicates if this body collides with the map or with any of bodies of the given array parameter.
     *
     * @param  map The game's map.
     * @param  collidables The bodies used for the collision test.
     * @return Boolean that indicates if the body collides with the map or with any of the other bodies.
     */
    boolean colidesWith(Map map, ArrayList<Body> collidables)
    {
        if(colidesWith(map))
        {
            return true;
        }

        for(Body body : collidables)
        {
            if(colidesWith(body))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array with the body's collision rectangles.
     *
     * @return Array containing the body's collision rectangles.
     */
    protected abstract ArrayList<Rect> getCollisionRectangles();

    /**
     * Returns an array with the body's trajectory points, between its former and current position.
     *
     * @return Array with the body's trajectory points.
     */
    ArrayList<Vec2<Integer> > getTrajPoints() //using MidPoint's line algorithm
    {
        int xi = (int) Math.round(oldX);
        int yi = (int) Math.round(oldY);
        int xf = (int) Math.round(x);
        int yf = (int) Math.round(y);

        return MidPoint.apply(xi,yi,xf,yf);
    }

    /**
     * Indicates if the body is touching the ground on the map.
     *
     * @param  m The game's map.
     * @return Boolean that indicates if the body is touching the ground on the map.
     */
    boolean isOnGround(Map m)
    {
        if(transparent)
        {
            return false;
        }
        setY((int)Math.round(getY()) + 1);

        boolean colides = colidesWith(m);

        resetY();

        return colides;
    }

    /**
     * Indicates if the body is on top of another body given as a parameter.
     *
     * @param  b The body used for test.
     * @return Boolean that indicates if the body is on top of the other body.
     */
    boolean isOnGround(Body b)
    {
        setY((int)Math.round(getY()) + 1);

        boolean colides = colidesWith(b);

        resetY();

        return colides;
    }

    /**
     * Indicates if this body is touching the ground on the map or is on top of any of the bodies given as parameters.
     *
     * @param  m The game's map.
     * @param  collidables The bodies used for the test.
     * @return Boolean that indicates if this body is touching the ground on the map or is on top of any of the other bodies.
     */
    boolean isOnGround(Map m, ArrayList<Body> collidables)
    {
        if (isOnGround(m))
        {
            return true;
        }

        for(Body body : collidables)
        {
            if(isOnGround(body))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Randomly places the body on the map, taking into account collision with the map and with an array of bodies.
     *
     * @param  map The game's map.
     * @param  collidables The bodies used for the collision test.
     */
    void placeBody(Map map, ArrayList<Body> collidables)
    {
        Random rand = new Random();
        do {
            int x = rand.nextInt(map.getMapWidth());
            int y = rand.nextInt(map.getMapHeight());
            setX(x);
            setY(y);
        } while (colidesWith(map,collidables) || (!(isOnGround(map)) && !transparent));
    }

    /**
     * Moves the body on the given direction, altering its x velocity.
     *
     * @param  right Boolean that indicates if the body should move right (false for left).
     */
    void move(boolean right)
    {
        this.vx += move_x * (right ? 1 : -1);
    }

    /**
     * Makes the body jump, altering its y velocity.
     */
    void jump()
    {
        if(this.n_jumps > 0)
        {
            this.vy -= jump_y;
            this.n_jumps--;
        }
    }

    /**
     * Enables the body to jump twice again.
     */
    void restoreJumps()
    {
        this.n_jumps = this.max_jumps;
    }

    /**
     * Indicates if the body is to the left of the body given as a parameter.
     *
     * @param  b Body used for the test.
     * @return Boolean indicating if the body is to the left of the other body.
     */
    boolean isToTheLeftOf(Body b)
    {
        return (this.x < b.getX());
    }

    /**
     * Indicates if the body is to the right of the body given as a parameter.
     *
     * @param  b Body used for the test.
     * @return Boolean indicating if the body is to the right of the other body.
     */
    boolean isToTheRightOf(Body b)
    {
        return (this.x > b.getX());
    }

    /**
     * Alters this body's velocity so it moves on the current direction of another body.
     *
     * @param  b Body to follow.
     * @param  V Norm of the velocity vector.
     */
    void follow(Body b, double V)
    {
        double dirX = b.getX() - this.x;
        double dirY = b.getY() - this.y;
        double norm = Math.sqrt(dirX*dirX + dirY*dirY);
        this.vx = V*dirX/norm;
        this.vy = V*dirY/norm;
    }

    /**
     * Centers the camera on the body.
     */
    void centerCamera()
    {
        Camera cam = Camera.getInstance();
        int width = cam.getWidth();
        int height = cam.getHeight();
        cam.setX((int)Math.round(this.x - width/2));
        cam.setY((int)Math.round(this.y - height/2));
    }

    /**
     * Indicates if the body is out of bounds, on the y axis, according to a maximum value.
     *
     * @param  maxY Maximum y value. If the body's y position surpasses this value, it is out of bounds.
     * @return Boolean indicating if the body is out of bounds.
     */
    boolean isOutofYBounds(int maxY)
    {
        return (this.y > maxY);
    }

    /**
     * (Re-)initializes the body's animation.
     */
    protected void initAnimation()
    {
        this.anim = new Animation(getSpriteSheet(),getFrameCount(),getFrameTime());
    }

    /**
     * Updates the body's animation.
     *
     * @param  deltaT Time (in millis) since the last update.
     */
    void updateAnim(long deltaT)
    {
        if(anim != null)
        {
            anim.update(deltaT);
        }

    }

    /**
     * Draws the body on top of the given bitmap, in the appropriate position.
     *
     * @param  gameMap Bitmap that contains the game's image, where the body's sprite will be drawn.
     */
    public void draw(Bitmap gameMap)
    {
        anim.drawSprite(gameMap,(int)Math.round(this.x),(int)Math.round(this.y));
    }

    /**
     * Returns one of the body's frame width.
     *
     * @return width of a frame (in pixels).
     */
    protected int getSpriteWidth()
    {
        return anim.getSpriteWidth();
    }

    /**
     * Returns the body's current sprite sheet.
     *
     * @return Bitmap containing the sprite sheet.
     */
    protected abstract Bitmap getSpriteSheet();

    /**
     * Returns the body's current frame count.
     *
     * @return Number of frames in the sprite sheet.
     */
    protected abstract int getFrameCount();

    /**
     * Returns the body's current frame time.
     *
     * @return Number of milliseconds each of the sprite sheet's frames should be displayed.
     */
    protected abstract long getFrameTime();
}
