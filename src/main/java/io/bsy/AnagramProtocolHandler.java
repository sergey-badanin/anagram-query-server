package io.bsy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import io.bsy.protocol.ProtocolCommand;
import io.bsy.protocol.ProtocolExecutor;
import io.bsy.protocol.ProtocolParser;
import io.bsy.protocol.UnparsableCommandException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class AnagramProtocolHandler extends ChannelInboundHandlerAdapter {

    private ProtocolParser parser;
    private ProtocolExecutor executor;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        parser = new ProtocolParser();
        executor = new ProtocolExecutor();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("greeting.txt");
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {

            var greeting = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                greeting.append(line + "\r\n");
            }
            sendResponse(ctx, greeting.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {
            String input = ((ByteBuf) msg)
                    .toString(io.netty.util.CharsetUtil.ISO_8859_1);

            parser.parse(input).stream()
                    .collect(Collectors.toMap(cmd -> cmd, executor::execute))
                    .entrySet().stream()
                    .map(this::buildResponse)
                    .forEach(resp -> sendResponse(ctx, resp));
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private String buildResponse(Map.Entry<ProtocolCommand, String> entry) {
        return entry.getKey().toString() + "\r\n" + entry.getValue() + "\r\n";
    }

    private void sendResponse(ChannelHandlerContext ctx, String response) {
        var byteResp = response.getBytes(io.netty.util.CharsetUtil.ISO_8859_1);
        var bufResp = ctx.alloc().buffer(byteResp.length).writeBytes(byteResp);

        try {
            ctx.writeAndFlush(bufResp).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof UnparsableCommandException) {
            sendResponse(ctx, cause.getMessage());
        } else {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
