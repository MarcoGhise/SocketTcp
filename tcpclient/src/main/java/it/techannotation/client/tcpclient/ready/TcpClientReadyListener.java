package it.techannotation.client.tcpclient.ready;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import it.techannotation.client.tcpclient.config.TcpClientConfig.TcpClientGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TcpClientReadyListener {

	private final TcpClientGateway tcpClientGateway;

	@EventListener(ApplicationReadyEvent.class)
	public void start() throws InterruptedException {
		for (int i = 0; i < 3; i++) {
			Thread.sleep(50);
			String payload = "prov" + i;
			if (i>9)
				payload = payload.substring(1, 6);

			log.info("Sending ... {}", payload);

			byte[] response = tcpClientGateway.send(payload.getBytes());
			log.info(new String(response, StandardCharsets.UTF_8));

//			Thread.sleep(30_000);
		}
	}

}
