package acgn.jessysnow.parser;

import acgn.jessysnow.common.core.annotation.Node;
import acgn.jessysnow.common.core.annotation.Nodes;
import acgn.jessysnow.common.core.enums.NodeTagName;
import acgn.jessysnow.common.core.interfaces.Parser;
import acgn.jessysnow.common.core.pojo.WebSite;
import acgn.jessysnow.common.exception.UnsupportedTypeException;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class DDomParser<T extends WebSite> implements Parser<T> {
    @Override
    public T parse(Object obj, T site) {
        if (!(obj instanceof WebElement html)){
            throw new IllegalArgumentException("Param obj need to be a Jsoup document");
        }

        Field[] fields = site.getClass().getDeclaredFields();
        for(Field f : fields){
            Class<?> type = f.getType();
            Annotation[] annotations = f.getDeclaredAnnotations();
            Nodes nodes = null;
            for(Annotation annotation : annotations){
                if(annotation.annotationType().equals(Nodes.class)){
                    nodes = (Nodes) annotation;
                    break;
                }
            }
            if(null == nodes){
                continue;
            }

            f.setAccessible(true);
            Object res = null;
            if(type.equals(String.class)){
                // Single values
                res =  this.parseSingle(html, nodes);
            }else if(type.equals(List.class)){
                // Multi values
                res = this.parseMulti(html, nodes);
            }else {
                log.error("Unsupported field type");
            }

            // Set field
            try {
                f.set(site, res);
            } catch (IllegalAccessException e) {
                log.error(e);
            }
        }

        return site;
    }

    private String parseSingle(WebElement html, Nodes nodes){
        String res = null;
        WebElement index = html;
        Node[] nodeArray = nodes.domNodes();
        for (Node node : nodeArray){
            // Parse failed, set field to null
            if(index == null){
                res = null;
                break;
            }
            if (isNotBlank(node.nodeId())){
                index = index.findElement(By.id(node.nodeId()));
            }else if (isNotBlank(node.nodeClassName())){
                // If the order is not specified, get the first one
                List<WebElement> webElements = index.findElements(By.className(node.nodeClassName()));
                if (webElements.size() == 0){
                    res = null;
                    break;
                }
                index = webElements.get(node.order());
            }else if(!node.nodeTagName().name().equals("NULL")){
                if (node.nodeTagName().equals(NodeTagName._text)){
                    res = index.getText();
                    break;
                }else if (node.nodeTagName().equals(NodeTagName._document)){
                    // Bozo operation
                    throw new UnsupportedOperationException("Selenium DDomParser unsupported");
                }else {
                    List<WebElement> elements = index.findElements(By.tagName(node.nodeTagName().name()));
                    index = elements.get(node.order());
                }
            }else {
                if(isBlank(node.nodeAttr())){
                    res = null;
                    break;
                }
                res = index.getAttribute(node.nodeAttr());
            }
        }
        return res;
    }

    private List<String> parseMulti(WebElement html, Nodes nodes){
        Deque<List<WebElement>> queue = new ArrayDeque<>();
        List<String> res = new ArrayList<>();
        Node[] nodeArray = nodes.domNodes();
        if(nodeArray == null || nodeArray.length == 0){
            return null;
        }

        // First node enqueue
        Node firstNode = nodeArray[0];
        List<WebElement> elements = fetchElementByNode(html, firstNode);
        // First node broken
        if (elements == null && !firstNode.nodeTagName().equals(NodeTagName._text)
                && firstNode.nodeAttr().equals("")
        ){
            return null;
        }
        queue.offer(elements);

        // BFS DOM Tree
        for (int i = 1; i < nodeArray.length; ++ i){
            Node node = nodeArray[i];
            if (queue.size() > 0){
                int size = queue.size();
                for (int j = 0; j < size; j++) {
                    List<WebElement> elementsParent = queue.poll();
                    for (WebElement e : elementsParent){
                        List<WebElement> elementsSon = fetchElementByNode(e, node);
                        // Cut off node chain in this element
                        if(elementsSon == null && node.nodeTagName().equals(NodeTagName._text)
                                || !node.nodeAttr().equals("")){
                            if (node.nodeTagName().equals(NodeTagName._text)){
                                res.add(e.getText());
                            }else {
                                res.add(e.getAttribute(node.nodeAttr()));
                            }
                        }else if (elementsSon != null){
                            queue.offer(elementsSon);
                        }
                    }
                }
            }
        }

        return res;
    }

    private List<WebElement> fetchElementByNode(WebElement element, Node node){
        if(isNotBlank(node.nodeId())){
            return element.findElements(By.id(node.nodeId()));
        }else if(isNotBlank(node.nodeClassName())){
            return element.findElements(By.className(node.nodeClassName()));
        }else if (!node.nodeTagName().name().equals("NULL")){
            if (node.nodeTagName().equals(NodeTagName._text)){
                return null;
            }else if (node.nodeTagName().equals(NodeTagName._document)){
                log.error(new UnsupportedTypeException("Can't apply #document tagName to multi search"));
            }else {
                return element.findElements(By.tagName(node.nodeTagName().name()));
            }
        }else{
            return null;
        }

        return null;
    }
}
