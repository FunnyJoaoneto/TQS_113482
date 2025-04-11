/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.sets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import tqs.sets.BoundedSetOfNaturals;

/**
 * @author ico0
 */
class BoundedSetOfNaturalsTest {
    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(4);
        setB = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        setC = BoundedSetOfNaturals.fromArray(new int[] { 50, 60 });
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @Test
    public void testAddElement() {

        setA.add(99);
        assertTrue(setA.contains(99));
        assertEquals(1, setA.size());

        assertThrows(IllegalArgumentException.class, () -> setA.add(99));
        assertEquals(1, setA.size());

        assertThrows(IllegalArgumentException.class, () -> setA.add(-99));
        assertEquals(1, setA.size());

        setA.add(100);
        setA.add(101);
        assertEquals(3, setA.size());

        assertThrows(IllegalArgumentException.class, () -> setB.add(25));
        assertFalse(setB.contains(25));
        assertEquals(6, setB.size());
    }

    @Test
    public void testAddFromBadArray() {
        int[] elems = new int[] { 1, -2, -3 };
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));

        int[] elems2 = new int[] { 4, 4, 5 };
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems2));

        int[] elems3 = new int[] { 6, 7, 8, 9 };
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems3));
    }

    @Test
    public void testIntersection() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });

        assertFalse(setA.intersects(setB));

        assertTrue(setB.intersects(setC));

        assertTrue(setB.intersects(setD));
        assertTrue(setB.intersects(setB));
    }

    @Test
    public void testEquals() {
        BoundedSetOfNaturals setD = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        BoundedSetOfNaturals setE = BoundedSetOfNaturals.fromArray(new int[] { 10, 20, 30, 40, 50, 60 });
        assertTrue(setD.equals(setD));
        assertTrue(setD.equals(setD));
        assertFalse(setD.equals(null));
        assertFalse(setD.equals(1));
    }

}
