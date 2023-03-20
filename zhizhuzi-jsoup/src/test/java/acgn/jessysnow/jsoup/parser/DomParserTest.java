package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.common.core.interfaces.Parser;
import acgn.jessysnow.jsoup.sample.SimpleHtml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DomParserTest {
    private final Parser<SimpleHtml> parser = new DomParser<>();
    public static final Document html;

    static {
        try {
            File file = new File("src/test/resources/html/SimpleHtml.html");
            html = Jsoup.parse(file,StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOnSimpleHtmlParse(){
        SimpleHtml webSite = parser.parse(html, new SimpleHtml());
        assertEquals(webSite.getTitle(), "DOM 教程");
        assertEquals(webSite.getWelcome(), "Hello world!");
        assertEquals(webSite.getWelcomeTwice(), "Hello world! twice");
        assertEquals(webSite.getOuterLinkTaoBaoText(), "outer link2");
        assertEquals(webSite.getOuterLinkTaobaoLink(), "https://www.taobao.com");
    }
}