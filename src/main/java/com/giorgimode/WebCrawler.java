package com.giorgimode;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.giorgimode.WebCrawlerUtil.intoString;
import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;


class WebCrawler {

    private static final String QUERY_FORMAT      = "https://www.google.com/search?q=%s&num=10";
    private static final int    TOP_LIBRARY_COUNT = 5;
    private static final String USER_AGENT        = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    Map<String, Long> retrieveLibraries(String searchTerm) {
        return getHtmlContent(String.format(QUERY_FORMAT, URLEncoder.encode(searchTerm, Charset.forName("UTF-8"))))
                .map(WebCrawlerUtil::extractResultUrls)
                .stream()
                .flatMap(Set::stream)
                .map(this::getHtmlContent)
                .flatMap(Optional::stream)
                .map(WebCrawlerUtil::parseLibraries)
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

    Optional<String> getHtmlContent(String path) {
        try {
            URLConnection connection = new URL(path).openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            return Optional.of(intoString(connection.getInputStream()));
        } catch (IOException e) {
            // exceptions should be logged/monitored/not ignored in production. For now ignore 4xx results
            return Optional.empty();
        }
    }
}
