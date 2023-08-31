package maverick.gleao.wormsworld;

import org.junit.Test;

import java.util.ArrayList;

import maverick.gleao.wormsworld.logic.MidPoint;
import maverick.gleao.wormsworld.logic.Vec2;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TestMidPoint {
    @Test
    public void testFindOctant() throws Exception
    {
        assertEquals(1, MidPoint.findOctant(1, 0, 7, 1));
        assertEquals(2, MidPoint.findOctant(0, 0, 4, 11));

        assertEquals(3, MidPoint.findOctant(0, 0, -4, 11));
        assertEquals(4, MidPoint.findOctant(-1, 0, -7, 1));

        assertEquals(5, MidPoint.findOctant(-1, 0, -7, -1));
        assertEquals(6, MidPoint.findOctant(0, 0, -4, -11));

        assertEquals(7, MidPoint.findOctant(0, 0, 4, -11));
        assertEquals(8, MidPoint.findOctant(1, 0, 7, -1));
    }

    @Test
    public void testMidPoint_Point() throws Exception {
        ArrayList<Vec2<Integer> > res = MidPoint.apply(0,0,0,0);
        assertEquals(1, res.size());

        assertEquals(new Vec2<Integer>(0,0),res.get(0));
    }

    @Test
    public void testMidPoint_Oct1() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(1,0,7,1);
        assertEquals(7, res.size());

        assertEquals(new Vec2<Integer>(1,0),res.get(0));
        assertEquals(new Vec2<Integer>(2,0),res.get(1));
        assertEquals(new Vec2<Integer>(3,0),res.get(2));
        assertEquals(new Vec2<Integer>(4,1),res.get(3));
        assertEquals(new Vec2<Integer>(5,1),res.get(4));
        assertEquals(new Vec2<Integer>(6,1),res.get(5));
        assertEquals(new Vec2<Integer>(7,1),res.get(6));
    }

    @Test
    public void testMidPoint_Oct2() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(0, 0, 4, 11);
        assertEquals(12, res.size());

        assertEquals(new Vec2<Integer>(0, 0), res.get(0));
        assertEquals(new Vec2<Integer>(0, 1), res.get(1));
        assertEquals(new Vec2<Integer>(1, 2), res.get(2));
        assertEquals(new Vec2<Integer>(1, 3), res.get(3));
        assertEquals(new Vec2<Integer>(1, 4), res.get(4));
        assertEquals(new Vec2<Integer>(2, 5), res.get(5));
        assertEquals(new Vec2<Integer>(2, 6), res.get(6));
        assertEquals(new Vec2<Integer>(3, 7), res.get(7));
        assertEquals(new Vec2<Integer>(3, 8), res.get(8));
        assertEquals(new Vec2<Integer>(3, 9), res.get(9));
        assertEquals(new Vec2<Integer>(4, 10), res.get(10));
        assertEquals(new Vec2<Integer>(4, 11), res.get(11));

        res = MidPoint.apply(0,0,5,5);
        assertEquals(6, res.size());

        assertEquals(new Vec2<Integer>(0,0),res.get(0));
        assertEquals(new Vec2<Integer>(1,1),res.get(1));
        assertEquals(new Vec2<Integer>(2,2),res.get(2));
        assertEquals(new Vec2<Integer>(3,3),res.get(3));
        assertEquals(new Vec2<Integer>(4,4),res.get(4));
        assertEquals(new Vec2<Integer>(5,5),res.get(5));
    }

    @Test
    public void testMidPoint_Oct3() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(0, 0, -4, 11);
        assertEquals(12, res.size());


        assertEquals(new Vec2<Integer>(0, 0), res.get(0));
        assertEquals(new Vec2<Integer>(0, 1), res.get(1));
        assertEquals(new Vec2<Integer>(-1, 2), res.get(2));
        assertEquals(new Vec2<Integer>(-1, 3), res.get(3));
        assertEquals(new Vec2<Integer>(-1, 4), res.get(4));
        assertEquals(new Vec2<Integer>(-2, 5), res.get(5));
        assertEquals(new Vec2<Integer>(-2, 6), res.get(6));
        assertEquals(new Vec2<Integer>(-3, 7), res.get(7));
        assertEquals(new Vec2<Integer>(-3, 8), res.get(8));
        assertEquals(new Vec2<Integer>(-3, 9), res.get(9));
        assertEquals(new Vec2<Integer>(-4, 10), res.get(10));
        assertEquals(new Vec2<Integer>(-4, 11), res.get(11));
    }

    @Test
    public void testMidPoint_Oct4() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(-1,0,-7,1);
        assertEquals(7, res.size());

        assertEquals(new Vec2<Integer>(-1,0),res.get(0));
        assertEquals(new Vec2<Integer>(-2,0),res.get(1));
        assertEquals(new Vec2<Integer>(-3,0),res.get(2));
        assertEquals(new Vec2<Integer>(-4,1),res.get(3));
        assertEquals(new Vec2<Integer>(-5,1),res.get(4));
        assertEquals(new Vec2<Integer>(-6,1),res.get(5));
        assertEquals(new Vec2<Integer>(-7,1),res.get(6));
    }

    @Test
    public void testMidPoint_Oct5() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(-1, 0, -7, -1);
        assertEquals(7, res.size());

        assertEquals(new Vec2<Integer>(-1,0),res.get(0));
        assertEquals(new Vec2<Integer>(-2,0),res.get(1));
        assertEquals(new Vec2<Integer>(-3,0),res.get(2));
        assertEquals(new Vec2<Integer>(-4,-1),res.get(3));
        assertEquals(new Vec2<Integer>(-5,-1),res.get(4));
        assertEquals(new Vec2<Integer>(-6,-1),res.get(5));
        assertEquals(new Vec2<Integer>(-7,-1),res.get(6));
    }

    @Test
    public void testMidPoint_Oct6() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(0, 0, -4, -11);
        assertEquals(12, res.size());


        assertEquals(new Vec2<Integer>(0, 0), res.get(0));
        assertEquals(new Vec2<Integer>(0, -1), res.get(1));
        assertEquals(new Vec2<Integer>(-1, -2), res.get(2));
        assertEquals(new Vec2<Integer>(-1, -3), res.get(3));
        assertEquals(new Vec2<Integer>(-1, -4), res.get(4));
        assertEquals(new Vec2<Integer>(-2, -5), res.get(5));
        assertEquals(new Vec2<Integer>(-2, -6), res.get(6));
        assertEquals(new Vec2<Integer>(-3, -7), res.get(7));
        assertEquals(new Vec2<Integer>(-3, -8), res.get(8));
        assertEquals(new Vec2<Integer>(-3, -9), res.get(9));
        assertEquals(new Vec2<Integer>(-4, -10), res.get(10));
        assertEquals(new Vec2<Integer>(-4, -11), res.get(11));
    }

    @Test
    public void testMidPoint_Oct7() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(0, 0, 4, -11);
        assertEquals(12, res.size());

        assertEquals(new Vec2<Integer>(0, 0), res.get(0));
        assertEquals(new Vec2<Integer>(0, -1), res.get(1));
        assertEquals(new Vec2<Integer>(1, -2), res.get(2));
        assertEquals(new Vec2<Integer>(1, -3), res.get(3));
        assertEquals(new Vec2<Integer>(1, -4), res.get(4));
        assertEquals(new Vec2<Integer>(2, -5), res.get(5));
        assertEquals(new Vec2<Integer>(2, -6), res.get(6));
        assertEquals(new Vec2<Integer>(3, -7), res.get(7));
        assertEquals(new Vec2<Integer>(3, -8), res.get(8));
        assertEquals(new Vec2<Integer>(3, -9), res.get(9));
        assertEquals(new Vec2<Integer>(4, -10), res.get(10));
        assertEquals(new Vec2<Integer>(4, -11), res.get(11));
    }

    @Test
    public void testMidPoint_Oct8() throws Exception {
        ArrayList<Vec2<Integer>> res = MidPoint.apply(1,0,7,-1);
        assertEquals(7, res.size());

        assertEquals(new Vec2<Integer>(1,0),res.get(0));
        assertEquals(new Vec2<Integer>(2,0),res.get(1));
        assertEquals(new Vec2<Integer>(3,0),res.get(2));
        assertEquals(new Vec2<Integer>(4,-1),res.get(3));
        assertEquals(new Vec2<Integer>(5,-1),res.get(4));
        assertEquals(new Vec2<Integer>(6,-1),res.get(5));
        assertEquals(new Vec2<Integer>(7,-1),res.get(6));
    }
}