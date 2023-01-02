package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.jsoup.pojo.WebSite;
import acgn.jessysnow.jsoup.sample.JDHtml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JDParseTest {
    private final Parser<JDHtml> parser = new DomParser<>();
    public static final Document html;

    static {
        try {
            File file = new File("src/test/resources/html/JD.html");
            html = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test(){
        JDHtml jd = parser.parse(html, new JDHtml());
    }
}
