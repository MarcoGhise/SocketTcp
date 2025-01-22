package it.techannotation.client.tcpclient.config;

import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CustomSerializer extends ByteArrayCrLfSerializer {

	/*
	https://stackoverflow.com/a/44359638/26035613
	the gateway can only handle one reply which is why you see that ERROR message.
	 */
	@Override
	public int fillToCrLf(InputStream inputStream, byte[] buffer) throws IOException {
		int bucketSize = 8;
		int n = 0;
		int bite;
		int available = inputStream.available();
		logger.debug(() -> "Available to read: " + available);
		try {
			while (true) {
				bite = inputStream.read();
				if (bite < 0 && n == 0) {
					throw new SoftEndOfStreamException("Stream closed between payloads");
				}

				checkClosure(bite);
				buffer[n++] = (byte) bite;

				if (n >= bucketSize) {
					break;
				}

				if (n >= getMaxMessageSize()) {
					throw new IOException("CRLF not found before max message length: " + getMaxMessageSize());
				}
			}
			return n;
		}
		catch (SoftEndOfStreamException e) { // NOSONAR catch and throw
			throw e; // it's an IO exception and we don't want an event for this
		}
		catch (IOException | RuntimeException e) {
			publishEvent(e, buffer, n);
			throw e;
		}
	}

	@Override
	public void serialize(byte[] bytes, OutputStream outputStream) throws IOException {
		outputStream.write(bytes);
	}

}