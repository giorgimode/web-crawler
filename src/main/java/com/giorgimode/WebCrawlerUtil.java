package com.giorgimode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

final class WebCrawlerUtil {
    private static final String  GOOGLE_RESULT_URL_PATTERN1 = "<a href=\"/url?q=";
    private static final String  GOOGLE_RESULT_URL_PATTERN2 = "\">";
    private static final Pattern GOOGLE_RESULT_URL_PATTERN  = Pattern.compile(Pattern.quote(GOOGLE_RESULT_URL_PATTERN1) + "(.*?)"
                                                                              + Pattern.quote(GOOGLE_RESULT_URL_PATTERN2));
    private static final Pattern JS_LIBRARY_PATTERN         = Pattern.compile("\\b(?<![<>][\\s]|\\w)[\\w-]*?\\.js\\b");
    private static final String  AMPERSAND                  = "&amp;";

    // Apache IOUtils would come in handy
    static String intoString(InputStream inputStream) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(joining("\n"));
        }
    }

    static Set<String> extractResultUrls(String googleResultHtmlContent) {
        Set<String> urls = new HashSet<>();
        Matcher matcher = GOOGLE_RESULT_URL_PATTERN.matcher(googleResultHtmlContent);

        while (matcher.find()) {
            String url = matcher.group(0).trim();
            url = url.substring(url.indexOf(GOOGLE_RESULT_URL_PATTERN1) + GOOGLE_RESULT_URL_PATTERN1.length());
            urls.add(url.substring(0, url.indexOf(AMPERSAND)));
        }
        return urls;
    }

    // switch List to Set if duplicates from the same page should be ignored
    static List<String> parseLibraries(String htmlContent) {
        List<String> libraries = new ArrayList<>();
        Matcher matcher = JS_LIBRARY_PATTERN.matcher(htmlContent);

        while (matcher.find()) {
            libraries.add(matcher.group(0).trim());
        }
        return libraries;
    }
}
