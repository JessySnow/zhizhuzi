package acgn.jessysnow.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.util.function.Consumer;

/**
 * Initialize channel-pipeline, codec http protocol
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final boolean ssl;
    private final boolean compress;
    private Consumer<ChannelHandlerContext> strategy;
    private SslContext sslContext;
    private ChannelInboundHandlerAdapter bizHandler;

    public HttpChannelInitializer(boolean ssl, boolean compress) throws SSLException {
        this.ssl = ssl;
        this.compress = compress;
        if(ssl){
            this.sslContext = SslContextBuilder.forClient().build();
        }
    }

    public HttpChannelInitializer(boolean ssl, boolean compress, Consumer<ChannelHandlerContext> strategy) throws SSLException {
        this(ssl, compress);
        this.strategy = strategy;
    }

    public HttpChannelInitializer(boolean ssl, boolean compress, ChannelInboundHandlerAdapter bizHandler) throws SSLException {
        this(ssl, compress);
        this.bizHandler = bizHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if(ssl) {
            ch.pipeline().addLast("http-ssl", sslContext.newHandler(ch.alloc()));
        }
        ch.pipeline().addLast("http-codec", new HttpClientCodec()).
                addLast("http-aggregator", new HttpObjectAggregator(65536));
        if(compress){
            ch.pipeline().addLast("http-decompressor", new HttpContentDecompressor());
        }
        if(null != this.bizHandler){
            ch.pipeline().addLast(bizHandler);
        }
        // handle the exception
        // default strategy : catch and print exception and close channel.
        ch.pipeline().addLast("exception-handler", new ChannelInboundHandlerAdapter(){
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if(strategy == null){
                    ctx.channel().close();
                }else{
                    strategy.accept(ctx);
                }
            }
        });
    }
}
