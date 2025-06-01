package com.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CronParserTest {

    @Test
    public void testValidCronExpression() {
        Map<String, String> parsedExpressionMap = CronParser.parseCron("*/15 0 1,15 * 1-5 /usr/bin/find");
        assertEquals("[0, 15, 30, 45]", parsedExpressionMap.get("minute"));
        assertEquals("[0]", parsedExpressionMap.get("hour"));
        assertEquals("[1, 15]", parsedExpressionMap.get("day of month"));
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]", parsedExpressionMap.get("month"));
        assertEquals("[1, 2, 3, 4, 5]", parsedExpressionMap.get("day of week"));
        assertEquals("/usr/bin/find", parsedExpressionMap.get("command"));
    }

    @Test
    public void testAllAsterisks() {
        Map<String, String> parsedExpressionMap = CronParser.parseCron("* * * * * /usr/bin/find");
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59]", parsedExpressionMap.get("minute"));
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]", parsedExpressionMap.get("hour"));
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]", parsedExpressionMap.get("day of month"));
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]", parsedExpressionMap.get("month"));
        assertEquals("[1, 2, 3, 4, 5, 6, 7]", parsedExpressionMap.get("day of week"));
        assertEquals("/usr/bin/find", parsedExpressionMap.get("command"));
    }

    @Test
    public void testStepValues() {
        Map<String, String> parsedExpressionMap =  CronParser.parseCron("*/20 */6 * * * /usr/bin/find");
        assertEquals("[0, 20, 40]", parsedExpressionMap.get("minute"));
        assertEquals("[0, 6, 12, 18]", parsedExpressionMap.get("hour"));
    }

    @Test
    public void testRangeValues() {
        Map<String, String> parsedExpressionMap =  CronParser.parseCron("0 0 * 3-6 * /usr/bin/find");
        assertEquals("[3, 4, 5, 6]", parsedExpressionMap.get("month"));
    }

    @Test
    public void testListValues() {
        Map<String, String> parsedExpressionMap = CronParser.parseCron("0 0 1,15,30 * * /usr/bin/find");
        assertEquals("[1, 15, 30]", parsedExpressionMap.get("day of month"));
    }

    @Test
    public void testInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            CronParser.parseCron("* * * *");
        });
    }

    @Test
    void testInvalidCronExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            CronParser.parseCron("invalid cron expression");
        });
    }
}

