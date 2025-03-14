Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Subtraction
    When I subtract 7 to 2
    Then the result is 5

  Scenario: Multiplication
    When I multiply 6 by 3
    Then the result is 18

  Scenario: Division
    When I divide 10 by 2
    Then the result is 5

  Scenario: Division by zero
    When I divide 8 by 0
    Then the result is an error

  Scenario: Invalid operation
    When I perform an invalid operation with 4 and 2
    Then the result is an error

  Scenario Outline: Several calculations
    When I <operation> <a> <type> <b>
    Then the result is <c>

  Examples: Various calculations
    | operation   | a  | type | b  | c  |
    | add         | 1  | and  | 2  | 3  |
    | subtract    | 9  | to   | 4  | 5  |
    | multiply    | 2  | by   | 3  | 6  |
    | divide      | 8  | by   | 4  | 2  |
