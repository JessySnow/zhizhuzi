package acgn.jessynow.comment;

import acgn.jessynow.comment.pojo.Comments;
import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class CrawlComment {
    private static final String[] searchList = {
//            "一加手机", "三只松鼠礼盒", "海尔冰箱", "乐事薯片", "手持稳定器", "布艺手链",
//            "人体工学椅", "汽车玻璃水", "订书机", "菜刀", "剃须刀", "短袖", "卡西欧",
//        "高达", "男性内裤", "红富士苹果", "劳保鞋","水笔", "内存条", "电脑主板B660"
            "学生凉席"
        };


    private static final String searchUrlBase = "https://search.jd.com/Search?keyword=";
    private static final String COMMENT_API_PREFIX = "https://club.jd.com/comment/productPageComments.action?productId=";
    private static final String POS_URL_SUFFIX = "&score=0&sortType=5&page=0&pageSize=10";
    private static final String NEG_URL_SUFFIX = "&score=1&sortType=5&page=0&pageSize=10";
    private static final String NEG_PATH_PREFIX = "/Users/jessy/cache/comment/neg/";
    private static final String POS_PATH_PREFIX = "/Users/jessy/cache/comment/pos/";

    private static final String NEG_PATH_PREFIX_TEST_SET = "/Users/jessy/cache/comment/test/neg/";
    private static final String POS_PATH_PREFIX_TEST_SET = "/Users/jessy/cache/comment/test/pos/";

    private static final String cookie = "__jdv=76161171|www.bing.com|-|referral|-|1676166393173; __jdu=16761663931721997646491; shshshfpa=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; shshshfpb=rFEj3bizn1y2DKoSD2OS7tw; PCSYCityID=CN_330000_330100_0; user-key=53246ee6-69ee-4a3d-b2b7-2758f3be61a4; shshshfp=929cafbe80a099d0d0830b84f89bc962; shshshfpx=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; TrackID=1S3WUH4lG-10nbGg0x3-hfR5bgRGzmHcYQQqSKQQgkx_Aa63GHtH16ngmEZvK1hxc24fqQ4KlpU-pU0D8rWlwt2UMcuVCAJrb6RIq9-6J-bwd8z7ciaHLr5mPyTynJlGz; pinId=LONEqkAtezd1NFzJK7fOtA; pin=jd_iGHlNQUNtvum; unick=砍杀二次元; _tp=aUNtLaiP0Nmgi+jjhZD1Ng==; _pst=jd_iGHlNQUNtvum; token=7d8422f29f24f244b38880ea98d4e0c9,2,931794; __tk=2YSn2wyYqAyY2YM51uqDqcaCqAxF1YgXqwMDqYMCKc1A1DqD2ca31V,2,931794; jsavif=0; jsavif=0; shshshsID=8a508f11b7e71a0838bb472479c3a584_1_1677230208488; __jda=122270672.16761663931721997646491.1676166393.1676991676.1677230209.22; __jdb=122270672.1.16761663931721997646491|22.1677230209; __jdc=122270672; areaId=15; ip_cityCode=1213; ipLoc-djd=15-1213-3038-59931; 3AB9D23F7A4B3C9B=EUHNEIMGNQXREPNNAHSZFJQR6JTCIDBUJVGCQPRYNWHGDXOLFP2KIKOQYXIC63O7EMUA6TKJUXMEWHEI6CYIQ7OOQ4";
    public static void main(String[] args) {

        try(CrawlEngine<Comments> posCommentsCrawlEngine = new CrawlEngineBuilder<>(Comments.class)
                .ssl(true)
                .compress(true)
                .charSet("GBK")
                .resConsumer(itemComment -> Arrays.stream(itemComment.getComments()).forEach(comment -> {
                    System.out.println(comment.getContent());
                    String fileName = POS_PATH_PREFIX_TEST_SET + UUID.randomUUID() + ".txt";
                    File file = new File(fileName);
                    try {
                        file.createNewFile();
                        try(FileOutputStream fileOut = new FileOutputStream(file)){
                            fileOut.write(comment.getContent().getBytes(StandardCharsets.UTF_8));
                        }
                    } catch (IOException e) {e.printStackTrace();}
                }))
                .build();

            CrawlEngine<Comments> negCommentsCrawlEngine = new CrawlEngineBuilder<>(Comments.class)
                    .ssl(true)
                    .compress(true)
                    .charSet("GBK")
                    .resConsumer(itemComment -> Arrays.stream(itemComment.getComments()).forEach(comment -> {
                        System.out.println(comment.getContent());
                        String fileName = NEG_PATH_PREFIX_TEST_SET + UUID.randomUUID() + ".txt";
                        File file = new File(fileName);
                        try {
                            file.createNewFile();
                            try(FileOutputStream fileOut = new FileOutputStream(file)){
                                fileOut.write(comment.getContent().getBytes(StandardCharsets.UTF_8));
                            }
                        } catch (IOException e) {e.printStackTrace();}
                    }))
                .build();

            CrawlEngine<JDUrlSkus> urlCrawlEngine = new CrawlEngineBuilder<>(JDUrlSkus.class)
                .ssl(true)
                .compress(true)
                .resConsumer(itemId -> itemId.getUrlSkus()
                        .forEach(id -> {
                            String posComment = COMMENT_API_PREFIX + id + POS_URL_SUFFIX;
                            String negComment = COMMENT_API_PREFIX + id + NEG_URL_SUFFIX;
                            System.out.println(posComment);
                            System.out.println(negComment);
                            try {
                                posCommentsCrawlEngine.blockExecute(new CrawlTask(posComment)
                                        .appendCookie(cookie));
                                Thread.sleep(10000);
                                negCommentsCrawlEngine.blockExecute(new CrawlTask(negComment)
                                        .appendCookie(cookie));
                                Thread.sleep(10000);
                            } catch (URISyntaxException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }))
                .build()){

            // crawl item sku-id
            for (String name : searchList){
                urlCrawlEngine.blockExecute(new CrawlTask(searchUrlBase + name)
                        .appendCookie(cookie));
                Thread.sleep(60000);
            }

        }catch (Exception e){e.printStackTrace();}
    }
}
