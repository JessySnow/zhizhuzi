package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.jsoup.annotation.Node;
import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.enums.NodeTagName;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.Data;

import java.util.List;


/**
 * HTML source code of this website
 * <html>
 *   <head>
 *     <meta charset="utf-8">
 *     <title>DOM 教程</title>
 *   </head>
 *   <body>
 *     <h1>DOM 课程1</h1>
 *     <p>Hello world!</p>
 *   </body>
 * </html>
 */
@Data
public class SimpleHtml extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.head),
            @Node(nodeTagName = NodeTagName.title)
    })
    private String title;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeTagName = NodeTagName.p)
    })
    private String Welcome;
}
