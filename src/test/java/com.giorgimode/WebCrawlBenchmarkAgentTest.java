package com.giorgimode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class WebCrawlBenchmarkAgentTest {

    private WebCrawlBenchmarkAgent benchmarkAgent = new WebCrawlBenchmarkAgent();
    private String                 searchWord;
    private int                    libraryCount;

    public WebCrawlBenchmarkAgentTest(String searchWord, int libraryCount) {
        this.searchWord = searchWord;
        this.libraryCount = libraryCount;
    }

    @Parameterized.Parameters(name = "searchWord={0}, libraryCount={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"junit", 10}, {"tesla", 20}, {"java", 30}, {"samurai", 70}
        });
    }


    @Test
    public void benchmarkWithLimitAndNoParallelization() {
        long duration = benchmarkAgent.benchmarkWithLimitAndNoParallelization(searchWord, libraryCount);
        System.out.println("WebCrawler with limit and no parallelization took " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
    }

    @Test
    public void benchmarkWithoutLimitAndNoParallelization() {
        long duration = benchmarkAgent.benchmarkWithoutLimitAndNoParallelization(searchWord, libraryCount);
        System.out.println("WebCrawler without limit and no parallelization took " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
    }

    @Test
    public void benchmarkWithoutLimitAndParallelization() {
        long duration = benchmarkAgent.benchmarkWithoutLimitAndParallelization(searchWord, libraryCount);
        System.out.println("WebCrawler without limit and parallelization took " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds");
    }

}