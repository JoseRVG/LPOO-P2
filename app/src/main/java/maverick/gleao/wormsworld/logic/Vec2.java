package maverick.gleao.wormsworld.logic;

/**
 * Vec2.java - class used to represent a two-dimensional vector.
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class Vec2 <T> {
    /**
     * Vector's x coordinate.
     */
    private T x;
    /**
     * Vector's y coordinate.
     */
    private T y;

    /**
     * Basic constructor for the Vec2 class.
     *
     * @param  x Initial x coordinate of vector.
     * @param  y Initial y coordinate of vector.
     */
    public Vec2(T x, T y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter method that returns the vector's x coordinate.
     *
     * @return Vector's x coordinate.
     */
    public T getX()
    {
        return x;
    }
    /**
     * Getter method that returns the vector's y coordinate.
     *
     * @return Vector's y coordinate.
     */
    public T getY()
    {
        return y;
    }

    /**
     * Setter method for the vector's x coordinate.
     *
     * @param  x Vector's new x coordinate.
     */
    void setX(T x)
    {
        this.x = x;
    }
    /**
     * Setter method for the vector's y coordinate.
     *
     * @param  y Vector's new y coordinate.
     */
    void setY(T y)
    {
        this.y = y;
    }

    /**
     * Determines if an object is equal to this Vec2 object.
     * <p>
     *     Two Vec2 objects are equal if their x and y coordinates are equal, respectively.
     * </p>
     *
     * @param  obj Object to compare to this Vec2 object.
     * @return Boolean that indicates if the object is equal to this Vec2 object.
     */
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof Vec2<?>)) return false;
        else
        {
            Vec2<T> other = (Vec2<T>)obj;
            return ((this.x == other.x) && (this.y == other.y));
        }
    }
}
