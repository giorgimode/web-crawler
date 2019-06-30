package com.giorgimode;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static com.giorgimode.TestUtils.readFromFile;
import static com.giorgimode.WebCrawlerUtil.extractResultUrls;
import static com.giorgimode.WebCrawlerUtil.parseForLibraries;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WebCrawlerUtilTest {

    @Test
    public void testShouldConvertInputStreamIntoString() {

    }

    @Test
    public void testShouldExtractResultUrls() throws IOException, URISyntaxException {
        String googleResultContent = readFromFile("googleResult.html", getClass());
        Set<String> resultUrls = extractResultUrls(googleResultContent);
        assertNotNull(resultUrls);
        assertEquals(resultUrls.size(), 11);
    }

    @Test
    public void testShouldParseForLibraries() throws IOException, URISyntaxException {
        String referredUrlContent = readFromFile("referredUrlResult.html", getClass());
        List<String> libraries = parseForLibraries(referredUrlContent);
        assertNotNull(libraries);
        assertEquals(libraries.size(), 7);
        assertEquals(libraries.get(0), "gtag.js");

        // assertThat() method from com.google.truth can better assert collections
    }

}