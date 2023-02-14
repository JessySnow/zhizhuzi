package acgn.jessysnow.gson.sample;

import acgn.jessysnow.gson.pojo.Json;

public class JDPriceAndName extends Json {
    public static class Price{
        private String p;

        @Override
        public String toString() {
            return "Price{" +
                    "p='" + p + '\'' +
                    '}';
        }
    }

    public static class WareInfo{
        private String wname;

        @Override
        public String toString() {
            return "WareInfo{" +
                    "wname='" + wname + '\'' +
                    '}';
        }
    }

    private Price price;
    private WareInfo wareInfo;

    @Override
    public String toString() {
        return "JDPriceAndName{" +
                "price=" + price +
                ", wareInfo=" + wareInfo +
                '}';
    }
}
