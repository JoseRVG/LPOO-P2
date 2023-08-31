package maverick.gleao.wormsworld.logic;

import java.util.ArrayList;

/**
 * Midpoint.java - class used apply the Midpoint algorithm, to compute the pixels that form a line between two points.
 * <p>
 *     This class is vital for the collision detection.
 * </p>
 * @author  Gonçalo Leão
 * @author  José Gomes
 */
public class MidPoint {
    /**
     * Applies the midpoint algorithm for the two points given as parameters.
     *
     * @param xi x coordinate of the initial point.
     * @param yi y coordinate of the initial point.
     * @param xf x coordinate of the final point.
     * @param yf y coordinate of the final point.
     * @return Array containing the pixels that form the line between the two points (it includes the pixels representing the delimiting points)
     */
    static public ArrayList<Vec2<Integer> > apply(int xi, int yi, int xf, int yf)
    {
        Vec2<Integer> Pi = new Vec2<Integer>(xi,yi);
        Vec2<Integer> Pf = new Vec2<Integer>(xf,yf);
        int octant = findOctant(xi,yi,xf,yf);
        Pi = convertToFirstOctant(Pi,octant);
        Pf = convertToFirstOctant(Pf,octant);
        ArrayList<Vec2<Integer> > res = applyFirstOctant(Pi,Pf);
        for(int i = 0; i < res.size(); i++)
        {
            Vec2<Integer> point = res.get(i);
            res.set(i,convertFromFirstOctant(point,octant));
        }
        return res;
    }

    /**
     * Applies the midpoint algorithm for the two points given as parameters, assuming they're on the first octant of the trigonometric circle (theta between 0º and 45º).
     * <p>
     *     This algorithm was presented by Professor António Augusto de Sousa during a CGRA lecture.
     * </p>
     * @param Pi Vector containing the initial point's coordinates.
     * @param Pf Vector containing the final point's coordinates.
     * @return Array containing the pixels that form the line between the two points (it includes the pixels representing the delimiting points)
     */
    static public ArrayList<Vec2<Integer> > applyFirstOctant(Vec2<Integer> Pi, Vec2<Integer> Pf)
    {
        ArrayList<Vec2<Integer> > res = new ArrayList<Vec2<Integer> >();

        int a = Pf.getX() - Pi.getX();
        int b = Pf.getY() - Pi.getY();

        int x = Pi.getX();
        int y = Pi.getY();

        int inc2 = 2*b; //increment to d to pick the Eastern point
        int d = inc2 - a; //value used to decide if we'll draw E or NE
        int inc1 = d - a; //increment to d to pick the North-Eastern point
        for(int i = 0; i < a; i++)
        {
            Vec2<Integer> point = new Vec2<Integer>(x, y);
            res.add(point);
            x += 1;
            if (d >= 0)
            {
                y += 1;
                d += inc1;
            }
            else
            {
                d += inc2;
            }
        }

        //Add the last point
        Vec2<Integer> point = new Vec2<Integer>(x,y);
        res.add(point);

        return res;
    }

    /**
     * Determines in which of the octants the line lies, using the initial point as the origin of the coordinate system.
     *
     * @param xi x coordinate of the initial point.
     * @param yi y coordinate of the initial point.
     * @param xf x coordinate of the final point.
     * @param yf y coordinate of the final point.
     * @return Integer from 1 to 8 with the octant where the line lies.
     */
    public static int findOctant(int xi, int yi, int xf, int yf)
    {
        int octant = 1;
        int a = xf - xi;
        int b = yf - yi;
        if(b < 0)
        {
            octant += 4;
            a = -a;
            b = -b;
        }
        if(a < 0)
        {
            octant += 2;
            int c = a;
            a = b;
            b = -c;
        }
        if(a < b)
        {
            octant += 1;
        }
        return octant;
    }

    /**
     * Returns a point of a specified octant, which corresponds to a rotation of the given point belonging to the first octant.
     *
     * @param point Vector containing the coordinates of the point's to rotate.
     * @param octant Octant where we want to place the point, by applying a rotation.
     * @return Vector containing the coordinates of the point's after being rotated to the desired octant.
     */
    private static Vec2<Integer> convertToFirstOctant(Vec2<Integer> point, int octant)
    {
        Vec2<Integer> newPoint = new Vec2<Integer>(point.getX(),point.getY());
        switch(octant)
        {
            case 2:
                newPoint.setX(point.getY());
                newPoint.setY(point.getX());
                break;
            case 3:
                newPoint.setX(point.getY());
                newPoint.setY(-point.getX());
                break;
            case 4:
                newPoint.setX(-point.getX());
                newPoint.setY(point.getY());
                break;
            case 5:
                newPoint.setX(-point.getX());
                newPoint.setY(-point.getY());
                break;
            case 6:
                newPoint.setX(-point.getY());
                newPoint.setY(-point.getX());
                break;
            case 7:
                newPoint.setX(-point.getY());
                newPoint.setY(point.getX());
                break;
            case 8:
                newPoint.setX(point.getX());
                newPoint.setY(-point.getY());
                break;

        }
        return newPoint;
    }

    /**
     * Returns a point of the first octant, which corresponds to a rotation of the given point belonging to a given octant.
     *
     * @param point Vector containing the coordinates of the point's to rotate.
     * @param octant Octant where the original point was.
     * @return Vector containing the coordinates of the point's after being rotated to the first octant.
     */
    private static Vec2<Integer> convertFromFirstOctant(Vec2<Integer> point, int octant)
    {
        Vec2<Integer> newPoint = new Vec2<Integer>(point.getX(),point.getY());
        switch(octant)
        {
            case 2:
                newPoint.setX(point.getY());
                newPoint.setY(point.getX());
                break;
            case 3:
                newPoint.setX(-point.getY());
                newPoint.setY(point.getX());
                break;
            case 4:
                newPoint.setX(-point.getX());
                newPoint.setY(point.getY());
                break;
            case 5:
                newPoint.setX(-point.getX());
                newPoint.setY(-point.getY());
                break;
            case 6:
                newPoint.setX(-point.getY());
                newPoint.setY(-point.getX());
                break;
            case 7:
                newPoint.setX(point.getY());
                newPoint.setY(-point.getX());
                break;
            case 8:
                newPoint.setX(point.getX());
                newPoint.setY(-point.getY());
                break;

        }
        return newPoint;
    }
}
