package me.newalvaro9.withdrawerpro.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberExtractor {
    public static long extractNumber(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Long.parseLong(matcher.group());
        }
        return 0;
    }
}
