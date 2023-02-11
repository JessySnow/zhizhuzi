package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.jsoup.annotation.Node;
import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.enums.NodeTagName;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class JDItemSkus extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeClassName = "w"),
            @Node(nodeClassName = "product-intro clearfix"),
            @Node(nodeClassName = "itemInfo-wrap"),
            @Node(nodeClassName = "summary p-choose-wrap"),
            @Node(nodeId = "choose-attrs"),
            @Node(nodeId = "choose-attr-1"),
            @Node(nodeClassName = "dd"),
            @Node(nodeClassName = "item"),
            @Node(nodeAttr = "data-sku")
    })
    @Getter
    private List<String> itemSkus;
}
