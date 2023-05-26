package org.e.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import org.e.controller.UserController;
import org.e.utils.MqttMsgBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final Logger log =  LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserController userController;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        JSONObject json = getPostParamsFromChannel(request);
        JSONObject resp = null;
        FullHttpResponse response = null;
        if (json == null) {
            response = responseOk(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
        }
        else {
            log.info(json.toJSONString());
            log.info(request.uri().trim());
            log.info(json.getString("a"));
            resp = userController.select(request.uri().trim(), json);
            ByteBuf buf = copiedBuffer(resp.toJSONString());
            response = responseOk(HttpResponseStatus.OK, buf);
        }
        // 发送响应
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private ByteBuf copiedBuffer(String data) {
        return Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8));
    }

    private FullHttpResponse responseOk(HttpResponseStatus status, ByteBuf buf) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,buf);
        if(buf != null){
            response.headers().set("Content-Type","application/json");
            response.headers().set("Content-Length",response.content().readableBytes());
        }
        return response;
    }


    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();

        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }

        return params;
    }

    private Map<String, Object> getGetParamsFromChannel(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>();
        if (fullHttpRequest.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();

            for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            return params;
        }
        return params;
    }

    private JSONObject getPostParamsFromChannel(FullHttpRequest fullHttpRequest) {
        JSONObject params = null;
        if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理post 请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (StringUtil.isNullOrEmpty(strContentType)) {
                return null;
            }
            if (strContentType.contains("application/json")) {
                params = getJSONParams(fullHttpRequest);
            } else {
                return null;
            }
        }
        return params;
    }
    private JSONObject getJSONParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<>();

        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        String strContent = null;
        strContent = new String(reqContent, StandardCharsets.UTF_8);


        return JSONObject.parseObject(strContent);

    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}