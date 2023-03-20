package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.common.core.annotation.Node;
import acgn.jessysnow.common.core.annotation.Nodes;
import acgn.jessysnow.common.core.enums.NodeTagName;
import acgn.jessysnow.common.core.pojo.WebSite;
import lombok.Getter;

import java.util.List;


/**
 * HTML source code of this website
 <html>
 <head>
     <meta charset="utf-8">
     <title>DOM 教程</title>
 </head>
     <body>
         <h1>DOM 课程1</h1>
         <p>Hello world!</p>
         <p>Hello world! twice</p>
         <a href="https://www.baidu.com">outer link1</a>
         <a href="https://www.taobao.com">outer link2</a>
     </body>
 </html>
 */
public class SimpleHtml extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.head),
                @Node(nodeTagName = NodeTagName.title),
                    @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String title;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
                @Node(nodeTagName = NodeTagName.p),
                    @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String Welcome;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
                @Node(nodeTagName = NodeTagName.p, order = 1),
                    @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String WelcomeTwice;

    @Nodes(domNodes = {
            @Node(nodeId = "taobao_a"),
                @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String outerLinkTaoBaoText;

    @Nodes(domNodes = {
            @Node(nodeId = "taobao_a"),
                @Node(nodeAttr = "href")
    })
    @Getter
    private String outerLinkTaobaoLink;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
                @Node(nodeClassName = "multi_div"),
                    @Node(nodeTagName = NodeTagName.a),
                        @Node(nodeAttr = "href")
    })
    @Getter
    private List<String> allLinks;
}
