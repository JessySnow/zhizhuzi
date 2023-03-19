# Zhuzhuzi
> A simple and easy-to-use crawler framework based on netty

## Project structure
- zhuzhuzi-common : Define common exceptions, pojo, enums and etc
- zhuzhuzi-engine : Download engine and configuration of framework
- zhuzhuzi-jsoup : HTML dom parser
- zhizhuzi-gson : Json parser

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
   Supported Targeting approach:
   - nodeTagName: Locating elements using dom tags, use enum instance instead of String
     - nodeId: Location element using dom element's id
     - nodeClassName: Location elements using element's class name
     - nodeAttr: Get inner attribute value of dom element
   
   Support for using index or offset to locate elements more precisely, see `Node.order()` and `Node.bias()`

2. Crawl item's data-sku from search.jd.com

There are three ways to perform tasks:
- execute: Return immediately after calling, asynchronously consume crawling results
- blockExecute: After calling, wait for the crawling to end before returning
- submit: After calling, wait for the crawling to end before returning, and return crawl result as a pojo

Here is a demo of blockExecute

```java
// Demo
public class JDCrawlTest {
   @Test
   public void test_urlList(){
      // more configuration about CrawlEngineBuilder, see CrawlEngineBuilder class
      try(CrawlEngine<JDUrlSkus> engine =
                  new CrawlEngineBuilder<>(JDUrlSkus.class) // engine will parse html to a JDUrlSkus Object
                          .ssl(true)    // engine ssl support
                          .compress(true)   // engine html-content compress support
                          .resConsumer(WebsiteConsumer::toConsole)  // define how to consume pojo we just crawl
                          .build() // return engine we build
      ){
          // submit task to engine in block mode
         // more configuration about CrawlTask, see CrawlTask class
         engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=GPW")); 
      }catch (Exception ignored){}
   }
}
```

3. Easy and peasy get what we want in console
```text
urlSkus
39689153276 10026493519952 100010255665 8753300 10060147618464 10060147618465 34312424914 10055418617931 10040458414309 10052873738457 100018123120 32921207364 10026488360177 10022946653369 10068182342803 10045384931160 10051774315894 41305680312 10027050953329 10027050953330 10068182342804 10031646184951 10049690328133 10068038009331 10067871522660 10028019288129 10033116126240 10026490619621 10033116126241 10034899387643 
```
