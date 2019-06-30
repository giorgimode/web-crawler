package com.giorgimode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.giorgimode.WebCrawlerUtil.getHtmlContent;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;


class WebCrawler {

    private static final String  GOOGLE_RESULT_URL_PATTERN1 = "<a href=\"/url?q=";
    private static final String  GOOGLE_RESULT_URL_PATTERN2 = "\">";
    private static final Pattern GOOGLE_RESULT_URL_PATTERN = Pattern.compile(Pattern.quote(GOOGLE_RESULT_URL_PATTERN1)
                                                                              + "(.*?)" + Pattern.quote(GOOGLE_RESULT_URL_PATTERN2));
    private static final Pattern JS_LIBRARY_PATTERN        = Pattern.compile("\\b(?<![<>][\\s]|\\w)[\\w-]*?\\.js\\b");
    private static final String  QUERY_FORMAT              = "https://www.google.com/search?q=%s&num=10";
    private static final int     TOP_LIBRARY_COUNT         = 5;
    private static final String  AMPERSAND                 = "&amp;";

    static Map<String, Long> retrieveLibraries(String searchTerm) {
        return getHtmlContent(String.format(QUERY_FORMAT, searchTerm))
                .map(WebCrawler::extractResultUrls)
                .stream()
                .flatMap(Set::stream)
                .map(WebCrawlerUtil::getHtmlContent)
                .flatMap(Optional::stream)
                .map(WebCrawler::parseLibraries)
                .flatMap(List::stream)
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .limit(TOP_LIBRARY_COUNT)
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1, LinkedHashMap::new));
    }

    private static Set<String> extractResultUrls(String googleResultHtmlContent) {
        Set<String> urls = new HashSet<>();
        Matcher matcher = GOOGLE_RESULT_URL_PATTERN.matcher(googleResultHtmlContent);

        while (matcher.find()) {
            String url = matcher.group(0).trim();

            url = url.substring(url.indexOf(GOOGLE_RESULT_URL_PATTERN1) + GOOGLE_RESULT_URL_PATTERN1.length())
                     .substring(0, url.indexOf(AMPERSAND));

            urls.add(url);
        }
        return urls;
    }

    private static List<String> parseLibraries(String htmlContent) {
        List<String> libraries = new ArrayList<>();
        Matcher matcher = JS_LIBRARY_PATTERN.matcher(htmlContent);

        while (matcher.find()) {
            libraries.add(matcher.group(0).trim());
        }
        return libraries;
    }
}
