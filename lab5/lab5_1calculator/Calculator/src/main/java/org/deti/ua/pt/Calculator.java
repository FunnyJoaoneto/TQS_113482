package org.deti.ua.pt;

import static java.util.Arrays.asList;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Calculator {
    private final Deque<Number> stack = new LinkedList<Number>();
    private static final List<String> OPS = asList("-", "+", "*", "/");
    private Exception lastException;

    public void push(Object arg) {
        try {
            if (OPS.contains(arg)) {
                Number y = stack.removeLast();
                Number x = stack.isEmpty() ? 0 : stack.removeLast();
                Double val = null;
                if (arg.equals("-")) {
                    val = x.doubleValue() - y.doubleValue();
                } else if (arg.equals("+")) {
                    val = x.doubleValue() + y.doubleValue();
                } else if (arg.equals("*")) {
                    val = x.doubleValue() * y.doubleValue();
                } else if (arg.equals("/")) {
                    if (y.doubleValue() == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    val = x.doubleValue() / y.doubleValue();
                }
                if (val == null) {
                    throw new IllegalArgumentException("Invalid operation: " + arg);
                }

                push(val);
            } else {
                stack.add((Number) arg);
            }
        } catch (Exception e) {
            lastException = e;
        }
    }

    public Number value() {
        return stack.getLast();
    }

    public Exception getLastException() {
        return lastException;
    }

}
