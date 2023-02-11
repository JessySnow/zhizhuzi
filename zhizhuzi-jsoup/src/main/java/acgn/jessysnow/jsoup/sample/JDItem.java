package acgn.jessysnow.jsoup.sample;

import acgn.jessysnow.jsoup.annotation.Node;
import acgn.jessysnow.jsoup.annotation.Nodes;
import acgn.jessysnow.jsoup.enums.NodeTagName;
import acgn.jessysnow.jsoup.pojo.WebSite;
import lombok.Data;
import lombok.Getter;

@Data
public class JDItem extends WebSite {
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeClassName = "w"),
            @Node(nodeClassName = "product-intro clearfix"),
            @Node(nodeClassName = "itemInfo-wrap"),
            @Node(nodeClassName = "sku-name"),
            @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String name;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeClassName = "w"),
            @Node(nodeClassName = "product-intro clearfix"),
            @Node(nodeClassName = "itemInfo-wrap"),
            @Node(nodeClassName = "summary summary-first"),
            @Node(nodeClassName = "summary-price-wrap"),
            @Node(nodeClassName = "summary-price J-summary-price"),
            @Node(nodeClassName = "dd"),
            @Node(nodeClassName = "p-price"),
            @Node(nodeClassName = "price"),
            @Node(nodeTagName = NodeTagName._text)
    })
    @Getter
    private String price;
}
