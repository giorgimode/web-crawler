package com.giorgimode;

import java.util.Map;
import java.util.Scanner;

public class WebCrawlerInit {
    private static Scanner scanner = new Scanner(System.in);
    private WebCrawler webCrawler = new WebCrawler();
    private static final int    TOP_LIBRARY_COUNT = 5;

    public static void main(String[] args) {
        System.out.println("Please enter a search string: ");
        String searchString = scanner.nextLine();
        while (searchString == null || searchString.isBlank()) {
            System.out.println("Blank text is not allowed. Try again: ");
            searchString = scanner.nextLine();
        }
        System.out.println("Querying and processing google results for " + searchString);
        new WebCrawlerInit().crawlWeb(searchString);
    }

    private void crawlWeb(String searchString) {
        Map<String, Long> libraryFrequencyMap = webCrawler.retrieveLibraries(searchString, TOP_LIBRARY_COUNT);
        libraryFrequencyMap.forEach((library, frequency) ->
                System.out.println("Library " + library + " has been used " + frequency + " times"));
    }
}
