package me.newalvaro9.withdrawerpro.utils;

import me.newalvaro9.withdrawerpro.WithdrawerPro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberManager {
    private static final WithdrawerPro plugin = WithdrawerPro.getPlugin();

    public static long extractNumber(String input) {
        input = input.replaceAll("[.,]", "");

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Long.parseLong(matcher.group());
        }
        return 0;
    }

    public static String formatNumber(long input) {
        String separatorType = plugin.getConfig().getString("decimal_separator").trim();
        if(separatorType.equalsIgnoreCase("COMMA")) {
            return formatNumberDecimalComma(input);
        } else if(separatorType.equalsIgnoreCase("DOT")) {
            return formatNumberDecimalDot(input);
        } else {
            return String.valueOf(input);
        }
    }

    private static String formatNumberDecimalComma(long input) { // 1.000,0
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);
        return formatter.format(input);
    }

    private static String formatNumberDecimalDot(long input) { // 1,000.0
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat formatter = new DecimalFormat("#,##0.##", symbols);
        return formatter.format(input);
    }
}
