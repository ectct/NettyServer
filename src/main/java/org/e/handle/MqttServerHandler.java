package org.e.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.e.Vo.MonitorVo;
import org.e.Vo.OximeterVo;
import org.e.Vo.OxyconVo;
import org.e.Vo.VentilatorVo;
import org.e.controller.SensorController;
import org.e.service.OxyconService;
import org.e.utils.MqttMsgBack;
import org.e.utils.ProtostuffUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MqttServerHandler extends SimpleChannelInboundHandler<MqttMessage> {

    private final Logger log =  LoggerFactory.getLogger(this.getClass());
    @Autowired
    SensorController sensorController;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MqttMessage mqttMessage) throws Exception {
        if (null != mqttMessage) {
            log.info("info--"+mqttMessage.toString());
            MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
            Channel channel = ctx.channel();

            if(mqttFixedHeader.messageType().equals(MqttMessageType.CONNECT)){
                //	在一个网络连接上，客户端只能发送一次CONNECT报文。服务端必须将客户端发送的第二个CONNECT报文当作协议违规处理并断开客户端的连接
                MqttMsgBack.connack(channel, mqttMessage);
            }

            switch (mqttFixedHeader.messageType()){
                case PUBLISH:		//	客户端发布消息
                    //	PUBACK报文是对QoS 1等级的PUBLISH报文的响应
                    MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;
                    byte[] b = new byte[mqttPublishMessage.content().readableBytes()];
                    mqttPublishMessage.content().readBytes(b);
                    String topic = mqttPublishMessage.variableHeader().topicName();
                    ObjectMapper objectMapper = new ObjectMapper();
                    if (topic.equals("topic/monitor")) {
                        MonitorVo monitorVo = ProtostuffUtils.deserialize(b, MonitorVo.class);
                        sensorController.monitorProcess(monitorVo);
                    } else if (topic.equals("topic/oxi")) {
                        OximeterVo oximeter = ProtostuffUtils.deserialize(b, OximeterVo.class);
                        sensorController.oximeterProcess(oximeter);
                    } else if (topic.equals("topic/oxy")) {
                        OxyconVo oxyconVo = ProtostuffUtils.deserialize(b, OxyconVo.class);
                        sensorController.oxyconProcess(oxyconVo);
                    } else if (topic.equals("topic/ven")) {
                        VentilatorVo ventilatorVo = ProtostuffUtils.deserialize(b, VentilatorVo.class);
                        sensorController.ventilatorProcess(ventilatorVo);
                    }

                    MqttMsgBack.puback(channel, mqttMessage);
                    break;
                case PUBREL:		//	发布释放
                    //	PUBREL报文是对PUBREC报文的响应
                    MqttMsgBack.pubcomp(channel, mqttMessage);
                    break;
                case SUBSCRIBE:		//	客户端订阅主题
                    //	客户端向服务端发送SUBSCRIBE报文用于创建一个或多个订阅，每个订阅注册客户端关心的一个或多个主题。
                    //	为了将应用消息转发给与那些订阅匹配的主题，服务端发送PUBLISH报文给客户端。
                    //	SUBSCRIBE报文也（为每个订阅）指定了最大的QoS等级，服务端根据这个发送应用消息给客户端
                    MqttMsgBack.suback(channel, mqttMessage);
                    break;
                case UNSUBSCRIBE:	//	客户端取消订阅
                    //	客户端发送UNSUBSCRIBE报文给服务端，用于取消订阅主题
                    MqttMsgBack.unsuback(channel, mqttMessage);
                    break;
                case PINGREQ:		//	客户端发起心跳
                    //	客户端发送PINGREQ报文给服务端的
                    //	在没有任何其它控制报文从客户端发给服务的时，告知服务端客户端还活着
                    //	请求服务端发送 响应确认它还活着，使用网络以确认网络连接没有断开
                    MqttMsgBack.pingresp(channel, mqttMessage);
                    break;
                case DISCONNECT:	//	客户端主动断开连接
                    //	DISCONNECT报文是客户端发给服务端的最后一个控制报文， 服务端必须验证所有的保留位都被设置为0
                    break;
                default:
                    break;
            }
        }
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
