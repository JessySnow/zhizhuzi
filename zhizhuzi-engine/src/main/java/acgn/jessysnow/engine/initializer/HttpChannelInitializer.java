package acgn.jessysnow.engine.initializer;

import acgn.jessysnow.engine.helper.SslHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

import javax.net.ssl.SSLException;
import java.util.function.Consumer;

/**
 * Initialize channel-pipeline, codec http protocol
 *      - ssl : Add a SSLHandler to pipeline or not
 *      - compress : Add a HttpDecompress to pipeline or not
 *      - strategy : Logic to deal with exception thrown in channel pipeline, will be added at the tail of pipeline
 *      - bizHandler : Logic to consume the response of http request, will be added before pipeline's exception handler
 * SSLHandler(Optional) --> HttpCodec --> HttpAggregator --> HttpDecompress --> BizHandler(Optional) --> ExceptionHandler
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected final boolean ssl;
    protected final boolean compress;
    protected Consumer<ChannelHandlerContext> strategy;
    protected ChannelInboundHandlerAdapter bizHandler;

    public HttpChannelInitializer(boolean ssl, boolean compress) throws SSLException {
        this.ssl = ssl;
        this.compress = compress;
    }

    public HttpChannelInitializer(boolean ssl, boolean compress, Consumer<ChannelHandlerContext> strategy) throws SSLException {
        this(ssl, compress);
        this.strategy = strategy;
    }

    public HttpChannelInitializer(boolean ssl, boolean compress, ChannelInboundHandlerAdapter bizHandler) throws SSLException {
        this(ssl, compress);
        this.bizHandler = bizHandler;
    }

    public HttpChannelInitializer(boolean ssl, boolean compress, ChannelInboundHandlerAdapter bizHandler,
                                  Consumer<ChannelHandlerContext> strategy){
        this.ssl = ssl;
        this.compress = compress;
        this.bizHandler = bizHandler;
        this.strategy = strategy;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if(ssl) {
            ch.pipeline().addFirst("http-ssl", SslHelper.getHandler());
        }
        ch.pipeline().addLast("http-codec", new HttpClientCodec()).
                addLast("http-aggregator", new HttpObjectAggregator(524288));
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
