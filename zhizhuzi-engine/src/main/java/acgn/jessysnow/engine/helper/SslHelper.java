package acgn.jessysnow.engine.helper;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

/**
 * Help to build ssl handler
 */
@Log4j2
public class SslHelper {

    public static SslHandler getHandler(){
        try {
            SslContext sslContext = SslContextBuilder.forClient().build();
            SSLEngine sslEngine = sslContext.newEngine(ByteBufAllocator.DEFAULT);
            return new SslHandler(sslEngine);
        } catch (SSLException e) {
            log.error(e);
            System.exit(1);
        }
        return null;
    }
}
