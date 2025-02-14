package it.techannotation.server.tcpserver.config;

import it.techannotation.server.tcpserver.constants.TcpServerConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.*;

@RequiredArgsConstructor
@Configuration
@EnableIntegration
public class TcpServerConfig {

	@Value("${tcp.server.port}")
	private int serverPort;

//	@Bean("clientConnectionFactoryForOutbound")
//	public AbstractServerConnectionFactory clientConnectionFactoryForOutbound() {
//		TcpNioServerConnectionFactory tcpNioServerConnectionFactory = new TcpNioServerConnectionFactory(serverPort);
//		tcpNioServerConnectionFactory.setUsingDirectBuffers(true);
//		tcpNioServerConnectionFactory.setSoTimeout(60_000);
//		AimSerializer serializer = new AimSerializer();
//		serializer.setMaxMessageSize(1048576); // 1MB
//		tcpNioServerConnectionFactory.setSerializer(serializer);
//		tcpNioServerConnectionFactory.setDeserializer(serializer);
//		return tcpNioServerConnectionFactory;
//	}

	@Bean
	public AbstractServerConnectionFactory serverFactory() {
		AbstractServerConnectionFactory factory = new TcpNetServerConnectionFactory(serverPort);
		CustomSerializer serializer = new CustomSerializer();
		serializer.setMaxMessageSize(1048576); // 1MB
		factory.setSerializer(serializer);
		factory.setDeserializer(serializer);
		factory.setSingleUse(false);
//		factory.setSoTimeout(80_000); //Server hang up after 80 seconds

		return factory;
	}

	@Bean
	public TcpInboundGateway inboundGateway(AbstractServerConnectionFactory serverFactory) {
		TcpInboundGateway inbound = new TcpInboundGateway();
		inbound.setConnectionFactory(serverFactory);
		inbound.setRequestChannelName(TcpServerConstants.MESSAGE_CHANNEL);
		inbound.setLoggingEnabled(true);

		return inbound;
	}

}