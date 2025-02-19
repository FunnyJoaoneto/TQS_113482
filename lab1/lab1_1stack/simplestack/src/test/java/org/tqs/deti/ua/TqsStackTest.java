package org.tqs.deti.ua;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

class TqsStackTest {
    private TqsStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new TqsStack<>();
    }

    @Test
    void testStackIsEmptyOnConstruction() {
        assertTrue(stack.isEmpty());
    }

    @Test
    void testStackHasSizeZeroOnConstruction() {
        assertEquals(0, stack.size());
    }

    @Test
    void testStackIsNotEmptyAfterPushes() {
        stack.push(10);
        stack.push(20);
        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
    }

    @Test
    void testPushThenPopReturnsSameValue() {
        stack.push(42);
        assertEquals(42, stack.pop());
    }

    @Test
    void testPushThenPeekReturnsSameValueWithoutChangingSize() {
        stack.push(99);
        assertEquals(99, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void testStackBecomesEmptyAfterPoppingAllElements() {
        stack.push(1);
        stack.push(2);
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void testPopOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    void testPeekOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    @Test
    void testPushingOntoFullStackThrowsException() {
        TqsStack<Integer> boundedStack = new TqsStack<>(2);
        boundedStack.push(1);
        boundedStack.push(2);
        assertThrows(IllegalStateException.class, () -> boundedStack.push(3));
    }

    @Test
    public void testPopToN() {
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertEquals(20, stack.popTopN(2));
        assertEquals(1, stack.size());
    }
}
