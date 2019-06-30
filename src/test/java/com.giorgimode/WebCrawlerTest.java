package com.giorgimode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class WebCrawlerTest {

    @Spy
    private WebCrawler webCrawler = new WebCrawler();

    @Test
    public void testShouldParsePagesAndCountLibraries() throws URISyntaxException, IOException {
        String searchTerm = "dracarys";
        String googleResultContent = readFromFile("googleResult.html");
        String referredUrlContent1 = readFromFile("referredUrlResult.html");
        String referredUrlContent2 = readFromFile("referredUrlResult2.html");

        doReturn(Optional.empty()).when(webCrawler).getHtmlContent(any());
        doReturn(Optional.of(googleResultContent)).when(webCrawler).getHtmlContent(contains(searchTerm));
        doReturn(Optional.of(referredUrlContent1)).when(webCrawler).getHtmlContent(contains("www.youtube.com"));
        doReturn(Optional.of(referredUrlContent2)).when(webCrawler).getHtmlContent(contains("gameofthrones.fandom.com"));

        Map<String, Long> searchResults = webCrawler.retrieveLibraries(searchTerm, 5);

        assertEquals(searchResults.size(), 5);
        Map.Entry<String, Long> mostFrequentLibrary = searchResults.entrySet().iterator().next();
        assertEquals(mostFrequentLibrary.getKey(), "code.js");
        assertEquals(7L, mostFrequentLibrary.getValue().longValue());
    }

    @Test
    public void testShouldHandleEmptyResults() {
    }

    private String readFromFile(String resourceName) throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        if (resource == null) {
            fail("Given resource could not be found " + resourceName);
        }
        return Files.readString(Path.of(resource.toURI()));
    }

}