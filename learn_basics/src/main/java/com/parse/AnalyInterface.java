package com.parse;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface AnalyInterface {
    List<Object> parse(String url, Map<String, Object> param) throws Exception;

    default String getpat(String str, String reg) {
        String t = "";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        if (m.find()) {
            t = m.group(1);
        }
        return t;
    }
}
