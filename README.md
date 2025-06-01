# Cron Parser

A simple Java application to parse cron expressions and display their expanded values.

## Features

- Parses standard cron expressions
- Supports step, range, and list values
- Outputs expanded values for minute, hour, day of month, month, day of week, and command

## Requirements

- Java 21 or higher
- Maven

## Build

```
mvn clean package
```

## Test
```
mvn test
```

## Usage

You can use the CronParser.parseCron(String expression) method in your Java code to parse a cron expression.

Example:
```
Map<String, String> result = CronParser.parseCron("*/15 0 1,15 * 1-5 /usr/bin/find");
System.out.println(result);
```

You can also run the application from the command line.
