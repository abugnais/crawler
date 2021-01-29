package com.github.abugnais.crawler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Collections.emptySet;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;


public class CrawlerTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8987);

    private final Crawler underTest = new Crawler();

    private final String baseUrl = "http://localhost:8987/";

    @Test
    public void should_parse_an_html_page_with_no_links_and_no_assets() throws IOException {
        // given
        final var pagePath = "simple.html";
        final var pageUrl = baseUrl + pagePath;
        final var simplePageContent = readHtml(pagePath);
        stubFor(get(urlEqualTo("/" + pagePath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simplePageContent)));

        // when
        final var result = underTest.crawl(new URL(pageUrl));

        // then
        assertThat(result.getBaseUrl()).isEqualTo(pageUrl);
        assertThat(result.getBroken()).isEmpty();
        assertThat(result.getPages()).containsExactlyInAnyOrder(new Page(pageUrl, emptySet(), emptySet(), emptySet()));
        assertThat(result.getMalformed()).isEmpty();
        assertThat(result.getExternalUrls()).isEmpty();
    }

    @Test
    public void should_parse_an_html_page_with_assets() throws IOException {
        // given
        final var pagePath = "has_assets.html";
        final var pageUrl = baseUrl + pagePath;
        final var simplePageContent = readHtml(pagePath);
        stubFor(get(urlEqualTo("/" + pagePath))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody(simplePageContent)));

        // when
        final var result = underTest.crawl(new URL(pageUrl));

        // then
        assertThat(result.getBaseUrl()).isEqualTo(pageUrl);
        assertThat(result.getBroken()).isEmpty();
        assertThat(result.getPages()).containsExactlyInAnyOrder(new Page(pageUrl, Set.of("http://simple.stylesheet.css"), Set.of("http://simple.script.js"), emptySet()));
        assertThat(result.getMalformed()).isEmpty();
        assertThat(result.getExternalUrls()).isEmpty();
    }

    @Test
    public void should_parse_a_broken_page() throws MalformedURLException {
        // given
        final var pagePath = "not_found.html";
        final var pageUrl = baseUrl + pagePath;
        stubFor(get(urlEqualTo("/" + pagePath)).willReturn(aResponse().withStatus(404).withBody("not found")));

        // when
        final var result = underTest.crawl(new URL(pageUrl));

        // then
        assertThat(result.getBaseUrl()).isEqualTo(pageUrl);
        assertThat(result.getBroken()).containsExactly(pageUrl);
        assertThat(result.getPages()).isEmpty();
        assertThat(result.getMalformed()).isEmpty();
        assertThat(result.getExternalUrls()).isEmpty();
    }

    @Test
    public void should_parse_a_page_with_a_cyclic_link() throws IOException {
        // given
        final var pagePath = "cyclic1.html";
        final var pageUrl = baseUrl + pagePath;
        final var page1Content = readHtml(pagePath);
        stubFor(get(urlEqualTo("/" + pagePath)).willReturn(aResponse().withStatus(200).withBody(page1Content)));

        final var page2Path = "cyclic2.html";
        final var page2Url = baseUrl + page2Path;
        final var page2Content = readHtml(page2Path);
        stubFor(get(urlEqualTo("/" + page2Path)).willReturn(aResponse().withStatus(200).withBody(page2Content)));

        // when
        final var result = underTest.crawl(new URL(pageUrl));

        // then
        assertEquals(result.getPages().size(), 2);
        assertEquals(pageUrl, result.getBaseUrl());
        assertEquals(emptySet(), result.getBroken());
        assertEquals(Set.of(new Page(pageUrl, emptySet(), emptySet(), Set.of(page2Url)), new Page(page2Url, emptySet(), emptySet(), Set.of(pageUrl))), result.getPages());
        assertEquals(emptySet(), result.getMalformed());
        assertEquals(emptySet(), result.getExternalUrls());
    }

    @Test
    public void should_parse_page_with_malformed_url() throws IOException {
        // given
        final var pagePath = "with_external_url.html";
        final var pageUrl = baseUrl + pagePath;
        final var simplePageContent = readHtml(pagePath);
        stubFor(get(urlEqualTo("/" + pagePath)).willReturn(aResponse().withBody(simplePageContent)));

        // when
        final var result = underTest.crawl(new URL(pageUrl));

        // then
        assertThat(result.getBaseUrl()).isEqualTo(pageUrl);
        assertThat(result.getBroken()).isEmpty();
        assertThat(result.getExternalUrls()).containsExactly("https://cuvva.com");
        assertThat(result.getMalformed()).isEmpty();
        assertThat(result.getPages()).hasSize(1);
        assertThat(result.getPages()).containsExactly(new Page(pageUrl, emptySet(), emptySet(), Set.of("https://cuvva.com")));
    }

    @Test
    public void should_parse_page_with_external_url() {

    }

    private String readHtml(String name) throws IOException {
        return Files.readString(Path.of("src/test/resources/" + name));
    }
}
