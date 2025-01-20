package it.techannotation.server.tcpserver.config;

import org.springframework.core.serializer.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;

public class MyFuckingDeseAll implements Deserializer<byte[]> {
    @Override
    public byte[] deserialize(InputStream inputStream) throws IOException {
        System.out.println(new Date() + " | START DESERIALIZE");
        Instant start = Instant.now();
        System.out.println(new Date() + " | START READING");
        var res = inputStream.readAllBytes();//inputStream.readNBytes(1448);
        if (res.length == 0) throw new RuntimeException("END OF STREAM");
        System.out.println(new Date() + " | Read bytes: " + res.length);
        System.out.println(new Date() + " | END READING");
        Instant end =  Instant.now();
        System.out.println(new Date() + " | Time taken: " + (end.toEpochMilli() - start.toEpochMilli()));
        System.out.println(new Date() + " | END DESERIALIZE");
        return res;
    }
}