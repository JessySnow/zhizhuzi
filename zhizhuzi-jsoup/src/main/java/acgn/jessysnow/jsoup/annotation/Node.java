package acgn.jessysnow.jsoup.annotation;

import acgn.jessysnow.jsoup.enums.NodeTagName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Dom Node def
 *     Supported approach to traverse html document
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    String nodeName();
    String nodeId();
    NodeTagName nodeTagName();
    String nodeClassName();
}
