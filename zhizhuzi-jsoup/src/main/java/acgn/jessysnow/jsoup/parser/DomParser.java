package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.jsoup.annotation.Node;
import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Parser a Html document by HTML DOM-Tree
 *
 * Thread safe util class
 */
@Log4j2
public class DomParser implements Parser{

    /**
     * Parse site according to annotation on the field of site
     * @param html Jsoup document object
     * @param site An empty pojo of website, will be updated in place
     * @return site
     */
    @Override
    public WebSite parse(Document html, WebSite site) {
        Element root = html;

        Field[] fields = site.getClass().getFields();
        for(Field f : fields){
            Class<?> type = f.getType();
            Annotation[] annotations = f.getDeclaredAnnotations();
            Nodes nodes = null;
            for(Annotation annotation : annotations){
                if(annotation.annotationType().equals(Node.class)){
                    nodes = (Nodes) annotation;
                    break;
                }
            }
            if(null == nodes){
                continue;
            }

            // TODO SET FIELD
            if(type.equals(String.class)){
                // Single values
                String res = this.parseSingle(html, nodes);
            }else if(type.equals(List.class)){
                // Multi values
                List<String> res = this.parseMulti(html, nodes);
            }
        }

        return site;
    }


    // TODO Finish it
    private String parseSingle(Document html, Nodes nodes){
        throw new UnsupportedOperationException("Not ready");
    }

    // TODO Finish it
    private List<String> parseMulti(Document html, Nodes nodes){
        throw new UnsupportedOperationException("Not ready");
    }
}
