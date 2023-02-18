package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.jsoup.annotation.Node;
import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.enums.NodeTagName;
import acgn.jessysnow.jsoup.pojo.WebSite;

import java.util.List;

public class JDUrlSkus extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeId = "J_searchWrap"),
            @Node(nodeId = "J_container"),
            @Node(nodeId = "J_main"),
            @Node(nodeClassName = "m-list"),
            @Node(nodeClassName = "ml-wrap"),
            @Node(nodeId = "J_goodsList"),
            @Node(nodeClassName = "gl-warp clearfix"),
            @Node(nodeClassName = "gl-item"),
            @Node(nodeAttr = "data-sku")
    })
    private List<String> urlSkus;
}