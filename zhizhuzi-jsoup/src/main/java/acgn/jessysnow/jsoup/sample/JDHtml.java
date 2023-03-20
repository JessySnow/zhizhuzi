package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.common.core.annotation.Node;
import acgn.jessysnow.common.core.annotation.Nodes;
import acgn.jessysnow.common.core.enums.NodeTagName;
import acgn.jessysnow.common.core.pojo.WebSite;
import lombok.Getter;

import java.util.List;

public class JDHtml extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeId = "J_searchWrap"),
            @Node(nodeId = "J_container"),
            @Node(nodeId = "J_main"),
            @Node(nodeClassName = "m-list"),
            @Node(nodeClassName = "ml-wrap"),
            @Node(nodeId = "J_goodsList"),
            @Node(nodeClassName = "gl-warp"),
            @Node(nodeTagName = NodeTagName.li),
            @Node(nodeTagName = NodeTagName.div),
            @Node(nodeClassName = "p-price"),
            @Node(nodeTagName = NodeTagName.strong),
            @Node(nodeTagName = NodeTagName.i),
            @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private List<String> price;
}
