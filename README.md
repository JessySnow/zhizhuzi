# Zhuzhuzi
> A simple and easy-to-use crawler framework based on netty

## Project structure
- zhuzhuzi-common : Define common exceptions, enums and etc.
- zhuzhuzi-engine : Download engine and configuration of framework.
- zhuzhuzi-jsoup : HTML dom parser.
- zhizhuzi-gson : Json parser.

## User guide
Step 1: Define a class to represent elements in HTML
```java
class JDItem extends WebSite {
    // domNodes represent the hierarchical structure of elements in HTML 
    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeClassName = "w", order = 3),
            @Node(nodeClassName = "product-intro clearfix"),
            @Node(nodeClassName = "itemInfo-wrap"),
            @Node(nodeClassName = "sku-name"),
            @Node(nodeTagName = NodeTagName._text)
    })
    private String name;

    @Nodes(domNodes = {
            @Node(nodeTagName = NodeTagName.body),
            @Node(nodeClassName = "w", order = 3),
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
    private String price;
}
```

Step 2: Define a crawl task
```java
CrawlTask task = new CrawlTask("item.jd.com", 443,
                    new URI("https://item.jd.com/100048428267.html"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
```

Step 3: Submit the task and Class to an engine which get from engine builder, the util class WebsiteConsumer will print
crawl result to console
```java
NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                JDItem.class);

nettyClientEngine.execute(task);
```
