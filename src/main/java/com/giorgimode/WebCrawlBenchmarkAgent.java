package com.giorgimode;

import java.util.LinkedHashMap;
import java.util.Map;

class WebCrawlBenchmarkAgent {

    private WebCrawler webCrawler = new WebCrawler();

    long benchmarkWithLimitAndNoParallelization(String text, int libraryCount) {
        long start = System.currentTimeMillis();
        webCrawler.retrieveLibraries(text, libraryCount);
        long end = System.currentTimeMillis();
        return end - start;
    }

    long benchmarkWithoutLimitAndNoParallelization(String text, int libraryCount) {
        long start = System.currentTimeMillis();
        getFrequencyMap(libraryCount, webCrawler.retrieveLibrariesWithoutLimit(text));
        long end = System.currentTimeMillis();
        return end - start;
    }

    long benchmarkWithoutLimitAndParallelization(String text, int libraryCount) {
        long start = System.currentTimeMillis();
        getFrequencyMap(libraryCount, webCrawler.retrieveLibrariesWithoutLimitParallelized(text));
        long end = System.currentTimeMillis();
        return end - start;
    }

    private Map<String, Long> getFrequencyMap(int libraryCount, Map<String, Long> libraryFrequencies) {
        Map<String, Long> libraryFrequenciesLimited = new LinkedHashMap<>();
        int count = 0;

        for (Map.Entry<String, Long> entry : libraryFrequencies.entrySet()) {
            if (count >= libraryCount) {
                break;
            }
            libraryFrequencies.put(entry.getKey(), entry.getValue());
        }
        return libraryFrequenciesLimited;
    }

}
