package com.giorgimode;

import java.util.Map;
import java.util.Scanner;

public class WebCrawlerInit {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("Please enter a search string: ");
        String searchString = scanner.nextLine();
        while (searchString == null || searchString.trim().isBlank()) {
            System.out.println("Blank text is not allowed");
            searchString = scanner.nextLine();
        }
        System.out.println("You entered " + searchString);
        Map<String, Long> libraryFrequencyMap = WebCrawler.retrieveLibraries(searchString);
        libraryFrequencyMap.forEach((library, frequency) ->
                System.out.println("Library " + library + " has been used " + frequency + " times"));

    }
}
