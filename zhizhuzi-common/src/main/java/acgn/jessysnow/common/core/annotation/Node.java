package acgn.jessysnow.common.core.annotation;

import acgn.jessysnow.common.core.enums.NodeTagName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static acgn.jessysnow.common.core.enums.NodeTagName.*;
/**
 * Dom Node def
 *     Supported approach to traverse html document
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    // Priority order
    String nodeId() default "";
    String nodeClassName() default "";
    NodeTagName nodeTagName() default NULL;
    String nodeAttr() default "";

    // Priority order
    int order() default 0;
    int bias() default 0; // bias from top, supported in list search
}
