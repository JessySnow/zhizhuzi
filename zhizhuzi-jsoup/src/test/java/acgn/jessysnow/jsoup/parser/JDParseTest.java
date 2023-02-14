package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.jsoup.sample.JDHtml;
import acgn.jessysnow.jsoup.sample.JDItem;
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

    @Test
    public void test_1() throws IOException {
        File _file = new File("src/test/resources/html/jditem-sku.html");
        Parser<JDItem> _parser = new DomParser<>();
        JDItem parse = _parser.parse(Jsoup.parse(_file, StandardCharsets.UTF_8.name()), new JDItem());
        System.out.println(parse);
    }
}
