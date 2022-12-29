package acgn.jessysnow.helper;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

/**
 * Help to build ssl handler
 */
public class SslHelper {
    private static SSLEngine sslEngine;

    static {
        try {
            SslContext sslContext = SslContextBuilder.forClient().build();
            sslEngine = sslContext.newEngine(ByteBufAllocator.DEFAULT);
        } catch (SSLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static SslHandler getHandler(){
        return new SslHandler(sslEngine);
    }
}
