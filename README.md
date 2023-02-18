# Zhuzhuzi
> A simple and easy-to-use crawler framework based on netty

## Project structure
- zhuzhuzi-common : Define common exceptions, enums and etc.
- zhuzhuzi-engine : Download engine and configuration of framework.
- zhuzhuzi-jsoup : HTML dom parser.
- zhizhuzi-gson : Json parser.

## User guide
1. How to position HTML elements
   - Use pojo and annotation
  ```java
    public class JDUrlSkus extends WebSite {
        @Nodes(domNodes = {
        @Node(nodeTagName = NodeTagName.body), // traverse to body
        @Node(nodeId = "J_searchWrap"),        // traverse to one or more dom node with this id
        @Node(nodeId = "J_container"),
        @Node(nodeId = "J_main"),
        @Node(nodeClassName = "m-list"),       // traverse to one or more dom node with this className
        @Node(nodeClassName = "ml-wrap"),      
        @Node(nodeId = "J_goodsList"),
        @Node(nodeClassName = "gl-warp clearfix"),
        @Node(nodeClassName = "gl-item"),
        @Node(nodeAttr = "data-sku")            // get data-sku attribute from all candidate DOM node
        })
        private List<String> urlSkus;
    }
  ```

2. Crawl item's data-sku from search.jd.com
```java
// Demo
public class JDCrawlTest {
    @Test
    public void test_urlList(){
        // Build a crawlengine from engine builder 
        try(CrawlEngine<JDUrlSkus> engine = 
                    new CrawlEngineBuilder<JDUrlSkus>(JDUrlSkus.class) // engine will get a JDUrlSKu pojo from HTML
                            .ssl(true)  // SSL support
                            .compress(true) // compress support
                            .resConsumer(WebsiteConsumer::toConsole) // define how to consume pojo we crawl
                            .build() // will return a CrawlEngine
        ){
            // Submit tasks in blocking mode
            engine.blockExecute(new CrawlTask("search.jd.com", 443,
                    new URI("https://search.jd.com/Search?keyword=分形工艺"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
            // Submit tasks in a non-blocking mode
            engine.execute(new CrawlTask("search.jd.com", 443,
                    new URI("https://search.jd.com/Search?keyword=分形工艺"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
        }catch (Exception ignored){;}
    }
}
```

