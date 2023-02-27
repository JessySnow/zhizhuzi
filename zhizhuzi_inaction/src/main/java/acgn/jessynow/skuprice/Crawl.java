package acgn.jessynow.skuprice;

import acgn.jessynow.skuprice.pojo.PriceAndName;
import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.pojo.CrawlTask;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crawl {
    public static void main(String[] args) {
        Set<Pair> res = new HashSet<>();
        try(CrawlEngine<PriceAndName> engine = new CrawlEngineBuilder<>(PriceAndName.class)
                .charSet("UTF-8")
                .ssl(true)
                .compress(true)
                .resConsumer(item -> {
                    List<String> skuName = item.getSkuName();
                    List<String> skuPrice = item.getSkuPrice();
                    for (int i = 0; i < skuName.size(); i++) {
                        res.add(new Pair(skuName.get(i), skuPrice.get(i)));
                    }
                })
                .build()){
            engine.blockExecute(
                    new CrawlTask("https://search.jd.com/Search?keyword=12400F")
                            .appendCookie("__jdv=122270672|direct|-|none|-|1676956347360; jsavif=0; shshshfpa=15efff41-cc88-1a08-569f-707593a9086e-1676956358; shshshfpb=hAoaKAW7I7RoyZDhOP5wIqg; __jdu=16769563473602145256160; rkv=1.0; TrackID=1SRue1iowL-xYzVBxXTkjgVd-hApRqBABrweRbMhN7-KlKie4eV2E8Guesl14yg4TqhEvsFfB9n1rwUeimxTWiNFPprDLu7FYON7EcKBiA5GV8oDmA0AoJvTO-TsqP3Vt; thor=D3833477BDFAB8FAFF4962A616C58F8C0FBC6CD3DA2A29FFA2656ADEA3FBC717A17F05761F981778C30F522F7D863E83717643154382D60D0E3E6FD5167754DA763B50D91EAA36D5CD9145F24A98D8262034F74E9341ABFC903BBB7FDDA214A5A76FA05E78A54DACEAF6C999C56E6F249D211091D8A2864C0B8EAB515A12989E1955850111620AAA2184549693F2301B49612FF470E8E0ED8F0CA96545667606; pinId=LONEqkAtezd1NFzJK7fOtA; pin=jd_iGHlNQUNtvum; unick=砍杀二次元; ceshi3.com=103; _tp=aUNtLaiP0Nmgi+jjhZD1Ng==; _pst=jd_iGHlNQUNtvum; PCSYCityID=CN_330000_330100_0; shshshfpx=15efff41-cc88-1a08-569f-707593a9086e-1676956358; __jda=122270672.16769563473602145256160.1676956347.1676956347.1676956347.1; __jdc=122270672; shshshfp=929cafbe80a099d0d0830b84f89bc962; jsavif=0; qrsc=3; areaId=15; token=ca31eb82e92ff3d9bc8331f50e13d3ff,3,931642; __tk=kcjwlsuokpaFkibTJstzlveEkUAxkzS1jcazlvfhJUn,3,931642; shshshsID=5e0075cbd440cbbf1ffe6c05b7ae9b4d_6_1676956655497; __jdb=122270672.9.16769563473602145256160|1.1676956347; ip_cityCode=1213; ipLoc-djd=15-1213-3038-59931; 3AB9D23F7A4B3C9B=TAR5GEWELL75MLKFU53ITZQTGJ7ZJDNIO3KFH4XKR2NIMFB5466SHDZLFEDAR27YJUNXU2CFDYUB7AWXZDSWRSQPNQ"));
        }catch (Exception e){e.printStackTrace();}
        System.out.println(res);
    }

    @Data
    private static class Pair{
        private final String name;
        private final String price;

        public Pair(String name, String price){
            this.name = name;
            this.price = price;
        }
    }
}
