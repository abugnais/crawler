package com.github.abugnais.crawler;

import java.util.Objects;
import java.util.Set;

public class Page {
    public final String url;

    public final Set<String> styles;

    public final Set<String> scripts;

    public final Set<String> children;

    public Page(String url, Set<String> styles, Set<String> scripts, Set<String> children) {
        this.url = url;
        this.styles = styles;
        this.scripts = scripts;
        this.children = children;
    }

    public String toString() {
        return "page url: " + url + "\n" +
                "style sheets: " + String.join(",", styles) + "\n" +
                "script files: " + String.join(",", scripts) + "\n\n" +
                "children: " + String.join(",", children);
    }

    public String getUrl() {
        return url;
    }

    public Set<String> getStyles() {
        return styles;
    }

    public Set<String> getScripts() {
        return scripts;
    }

    public Set<String> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return getUrl().equals(page.getUrl()) &&
                getStyles().equals(page.getStyles()) &&
                getScripts().equals(page.getScripts()) &&
                getChildren().equals(page.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, styles, scripts, children);
    }
}
