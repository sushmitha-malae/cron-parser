package com.parser;

import java.util.*;
import java.util.logging.Logger;

public class CronParser {
    private static final int MINUTES_MAX = 59;
    private static final int HOURS_MAX = 23;
    private static final int DAYS_OF_MONTH_MAX = 31;
    private static final int MONTHS_MAX = 12;
    private static final int DAYS_OF_WEEK_MAX = 7;
    private static final Logger logger = Logger.getLogger(CronParser.class.getName());

    static {
        // Remove default handlers
        Logger rootLogger = Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        // Add a simple handler that only prints the message
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                return record.getMessage() + System.lineSeparator();
            }
        });
        rootLogger.addHandler(handler);
    }

    /*    ToDo: Questions to ask

        Do we need to validate cron expression by the number of days and month selected?
        Do we need to consider any other special character apart from *,/,- in string expression? like ?,L, etc

    */

    public static Map<String, String> parseCron(String cronExpression) {
        Map<String, String> cronParts = new LinkedHashMap<>();

        String[] parts = cronExpression.trim().split("\\s+");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid cron expression. Expected 6 parts.");
        }

        // Parse each field
        List<Integer> minutes = parseField(parts[0], 0, MINUTES_MAX);
        List<Integer> hours = parseField(parts[1], 0, HOURS_MAX);
        List<Integer> daysOfMonth = parseField(parts[2], 1, DAYS_OF_MONTH_MAX);
        List<Integer> months = parseField(parts[3], 1, MONTHS_MAX);
        List<Integer> daysOfWeek = parseField(parts[4], 1, DAYS_OF_WEEK_MAX);
        String command = parts[5];

        // Print formatted output
        cronParts.put("minute", minutes.toString());
        cronParts.put("hour", hours.toString());
        cronParts.put("day of month", daysOfMonth.toString());
        cronParts.put("month", months.toString());
        cronParts.put("day of week", daysOfWeek.toString());
        cronParts.put("command", command);

        return cronParts;
    }

    private static List<Integer> parseField(String field, int min, int max) {
        List<Integer> values = new ArrayList<>();

        // Handle asterisk
        if (field.equals("*")) {
            for (int i = min; i <= max; i++) {
                values.add(i);
            }
            return values;
        }

        // Handle step values
        if (field.contains("/")) {
            String[] parts = field.split("/");
            int step = Integer.parseInt(parts[1]);
            int start = parts[0].equals("*") ? min : Integer.parseInt(parts[0]);
            for (int i = start; i <= max; i += step) {
                values.add(i);
            }
            return values;
        }

        // Handle ranges and lists
        String[] ranges = field.split(",");
        for (String range : ranges) {
            if (range.contains("-")) {
                String[] bounds = range.split("-");
                int start = Integer.parseInt(bounds[0]);
                int end = Integer.parseInt(bounds[1]);
                for (int i = start; i <= end; i++) {
                    values.add(i);
                }
            } else {
                values.add(Integer.parseInt(range));
            }
        }

        Collections.sort(values);
        return values;
    }

    private static void printOutput(Map<String, String> cronParts) {
        StringBuilder output = new StringBuilder();
        for(Map.Entry<String, String> entry : cronParts.entrySet()) {
            logger.info(String.format("%-14s %s", entry.getKey(), entry.getValue()));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                logger.info("Enter cron expression (or type 'exit' to quit):");
                String cronExpression = scanner.nextLine();
                if (cronExpression.equalsIgnoreCase("exit")) {
                    logger.info("Exiting...");
                    break;
                }
                try {
                    Map<String, String> parsedCron  = parseCron(cronExpression);
                    printOutput(parsedCron);
                } catch (IllegalArgumentException e) {
                    logger.severe("Error: " + e.getMessage());
                    logger.info("Usage example: */15 0 1,15 * 1-5 /usr/bin/find");
                }
            }
        } finally {
            scanner.close();
        }
    }
}

