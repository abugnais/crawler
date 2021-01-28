package com.github.abugnais.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;

public class CrawlerTest {
    public final Crawler underTest = new Crawler();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8987);

    @Test
    public void should_parse_an_html_page_with_no_links_and_no_assets() throws IOException {
        // given
        final var simplePageContent = readHtml("simple.html");
        stubFor(get(urlEqualTo("/simple.html"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simplePageContent)));

        // when
        final var result = underTest.crawl(new URL("http://localhost:8987/simple.html"));

        // then
        assertEquals("http://localhost:8987/simple.html", result.getBaseUrl());
        assertEquals(emptySet(), result.getBroken());
        assertEquals(Set.of(new Page("http://localhost:8987/simple.html", emptySet(), emptySet(), emptySet())), result.getPages());
        assertEquals(emptySet(), result.getMalformed());
        assertEquals(emptySet(), result.getExternalUrls());
    }

    @Test
    public void should_parse_an_html_page_with_assets() throws IOException {
        // given
        final var simplePageContent = readHtml("has_assets.html");
        stubFor(get(urlEqualTo("/has_assets.html"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simplePageContent)));

        // when
        final var result = underTest.crawl(new URL("http://localhost:8987/has_assets.html"));

        // then
        assertEquals("http://localhost:8987/has_assets.html", result.getBaseUrl());
        assertEquals(emptySet(), result.getBroken());
        assertEquals(Set.of(new Page("http://localhost:8987/has_assets.html", Set.of("http://simple.stylesheet.css"), Set.of("http://simple.script.js"), emptySet())), result.getPages());
        assertEquals(emptySet(), result.getMalformed());
        assertEquals(emptySet(), result.getExternalUrls());
    }

    private String readHtml(String name) throws IOException {
        return Files.readString(Path.of("src/test/resources/" + name));
    }
}
