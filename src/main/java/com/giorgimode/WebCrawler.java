package com.giorgimode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;


public class WebCrawler {

    private static final String GOOGLE_RESULT_URL_PATTERN1 = "<a href=\"/url?q=";
    private static final String GOOGLE_RESULT_URL_PATTERN2 = "\">";
    private static final Pattern GOOGLE_RESULT_URL_PATTERN = Pattern.compile(Pattern.quote(GOOGLE_RESULT_URL_PATTERN1)
            + "(.*?)" + Pattern.quote(GOOGLE_RESULT_URL_PATTERN2));
    private static final Pattern JS_LIBRARY_PATTERN = Pattern.compile("\\b(?<![<>][\\s]|\\w)[\\w-]*?\\.js\\b");
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private static final String QUERY_FORMAT = "https://www.google.com/search?q=%s&num=10";
    private static final int TOP_LIBRARY_COUNT = 5;

    static Map<String, Long> retrieveLibraries(String searchTerm) {
        return getHtmlContent(String.format(QUERY_FORMAT, searchTerm))
                .map(WebCrawler::parseLinks)
                .stream()
                .flatMap(Set::stream)
                .map(WebCrawler::getHtmlContent)
                .flatMap(Optional::stream)
                .map(WebCrawler::parseLibraries)
                .flatMap(List::stream)
                .peek(System.out::println)
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .limit(TOP_LIBRARY_COUNT)
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    private static Optional<String> getHtmlContent(String path) {
        try {
            URLConnection connection = new URL(path).openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            return Optional.of(getString(connection.getInputStream()));
        } catch (IOException e) {
            // exceptions should be logged/monitored/not ignored in production
            return Optional.empty();
        }
    }

    private static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static Set<String> parseLinks(String html) {
        Set<String> result = new HashSet<>();
        Matcher matcher = GOOGLE_RESULT_URL_PATTERN.matcher(html);

        while (matcher.find()) {
            String domainName = matcher.group(0).trim();

            domainName = domainName.substring(domainName.indexOf(GOOGLE_RESULT_URL_PATTERN1) + GOOGLE_RESULT_URL_PATTERN1.length());
            domainName = domainName.substring(0, domainName.indexOf("&amp;"));

            result.add(domainName);
        }
        return result;
    }

    private static List<String> parseLibraries(String html) {
        List<String> result = new ArrayList<>();
        Matcher matcher = JS_LIBRARY_PATTERN.matcher(html);

        while (matcher.find()) {
            String library = matcher.group(0).trim();
            result.add(library);
        }
        return result;
    }
}
