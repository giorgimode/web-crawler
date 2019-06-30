package com.giorgimode;

import java.util.Scanner;

public class WebCrawlerInit {

    public static void main(String[] args) {
        System.out.println("Please enter a search string: ");
        Scanner scanner = new Scanner(System.in);
        String searchString = scanner.nextLine();
        while (searchString == null || searchString.trim().isBlank()) {
            System.out.println("Blank text is not allowed");
            searchString = scanner.nextLine();
        }
        System.out.println("You entered " + searchString);
    }
}
