package com.giorgimode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebCrawler {

    private static final String GOOGLE_RESULT_URL_PATTERN1 = "<a href=\"/url?q=";
    private static final String GOOGLE_RESULT_URL_PATTERN2 = "\">";
    private static final Pattern GOOGLE_RESULT_URL_PATTERN = Pattern.compile(Pattern.quote(GOOGLE_RESULT_URL_PATTERN1) + "(.*?)" + Pattern.quote(GOOGLE_RESULT_URL_PATTERN2));
    private static final Pattern JS_LIBRARY_PATTERN = Pattern.compile("\\b(?<![<>][\\s]|\\w)[\\w-]*?\\.js\\b");

    public static void crawl(String searchTerm) throws Exception {
        String query = "https://www.google.com/search?q=" + searchTerm + "&num=10";

        parseLinks(getSearchContent(query)).forEach(returnUrl -> {
            System.out.println(returnUrl);
            try {
                List<String> strings = parseLibraries(getSearchContent(returnUrl));
                System.out.println(strings);
            } catch (Exception e) {
                System.out.println(e);
            }

        });
    }

    private static String getSearchContent(String path) throws Exception {
        final String agent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        URL url = new URL(path);
        final URLConnection connection = url.openConnection();

        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return getString(stream);
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

    private static Set<String> parseLinks(final String html) {
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

    private static List<String> parseLibraries(final String html) {
        List<String> result = new ArrayList<>();
        Matcher matcher = JS_LIBRARY_PATTERN.matcher(html);

        while (matcher.find()) {
            String library = matcher.group(0).trim();
            result.add(library);
        }
        return result;
    }
}
