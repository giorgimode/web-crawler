package com.giorgimode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.fail;

class TestUtils {

    static String readFromFile(String resourceName, Class<?> clazz) throws IOException, URISyntaxException {
        URL resource = clazz.getClassLoader().getResource(resourceName);
        if (resource == null) {
            fail("Given resource could not be found " + resourceName);
        }
        return Files.readString(Path.of(resource.toURI()));
    }
}
