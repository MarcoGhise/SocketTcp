package it.techannotation.server.tcpserver.endpoint;

import java.nio.charset.StandardCharsets;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import it.techannotation.server.tcpserver.constants.TcpServerConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@MessageEndpoint
@Slf4j
@RequiredArgsConstructor
public class TcpServerInboundEndpoint {

	@ServiceActivator(inputChannel = TcpServerConstants.MESSAGE_CHANNEL, requiresReply = "true")
	public byte[] onMessage(Message<byte[]> message) {

		byte[] bytePayload = message.getPayload();

		String payloadString = new String(bytePayload, StandardCharsets.UTF_8);

		log.info("Received {}", payloadString);

//		if (Integer.valueOf(payloadString) % 2 == 0) {
//			return ResultEnum.ERROR.name().getBytes();
//		}

		return payloadString.getBytes();
	}
}
