package me.moye.hew.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static int date2yyyyMMddNumber(LocalDate date) {
        // Define the desired output pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // Format the date object into a string
        String dateString = date.format(formatter);
        return Integer.parseInt(dateString);
    }

    public static String now2yyyyMMddHHmmss() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the desired format pattern
        // Note: Use 'HH' for 24-hour time (00-23)
        // Use 'hh' for 12-hour time (01-12)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return now.format(formatter);
    }
}
