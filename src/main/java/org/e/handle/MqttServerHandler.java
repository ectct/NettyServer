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
                MqttMsgBack.connack(channel, mqttMessage);
            }

            switch (mqttFixedHeader.messageType()){
                case PUBLISH:		//	客户端发布消息
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
                    MqttMsgBack.pubcomp(channel, mqttMessage);
                    break;
                case SUBSCRIBE:		//	客户端订阅主题
                    MqttMsgBack.suback(channel, mqttMessage);
                    break;
                case UNSUBSCRIBE:	//	客户端取消订阅
                    MqttMsgBack.unsuback(channel, mqttMessage);
                    break;
                case PINGREQ:		//	客户端发起心跳
                    MqttMsgBack.pingresp(channel, mqttMessage);
                    break;
                case DISCONNECT:	//	客户端主动断开连接
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
