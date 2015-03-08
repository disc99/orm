package com.github.disc99.orm.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Databases {
    public static String snakeToCamel(String targetStr) {
        Pattern p = Pattern.compile("_([a-z])");
        Matcher m = p.matcher(targetStr.toLowerCase());

        StringBuffer sb = new StringBuffer(targetStr.length());
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String camelToSnake(String targetStr) {
        String convertedStr = targetStr
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2");
        return convertedStr.toUpperCase();
    }
}
