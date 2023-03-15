package acgn.jessynow.skuprice.pojo;

import acgn.jessysnow.common.core.annotation.Node;
import acgn.jessysnow.common.core.annotation.Nodes;
import acgn.jessysnow.common.core.enums.NodeTagName;
import acgn.jessysnow.common.core.pojo.WebSite;
import lombok.Getter;

import java.util.List;

public class PriceAndName extends WebSite {
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
            @Node(nodeClassName = "gl-i-wrap"),
            @Node(nodeClassName = "p-price"),
            @Node(nodeTagName = NodeTagName.strong),
            @Node(nodeTagName = NodeTagName.i),
            @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private List<String> skuPrice;

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
            @Node(nodeClassName = "gl-i-wrap"),
            @Node(nodeClassName = "p-name p-name-type-2"),
            @Node(nodeTagName = NodeTagName.a),
            @Node(nodeTagName = NodeTagName.em),
            @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private List<String> skuName;
}
