package com.java.ssh.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class SelfDefineEncodeHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bufferIn, List<Object> out) throws Exception {
        if (bufferIn.readableBytes() < 8) {
            return;
        }
        int beginIndex = bufferIn.readerIndex();
        int length = bufferIn.readInt();
        if (bufferIn.readableBytes() < length - 4) {
            bufferIn.readerIndex(beginIndex);
            return;
        }
        bufferIn.readerIndex(beginIndex + length);
        ByteBuf otherByteBufRef = bufferIn.slice(beginIndex, length);
        otherByteBufRef.retain();
        out.add(otherByteBufRef);
    }
}
