package it.buniva.strage.utility.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MyCustomDeserializerInstant extends JsonDeserializer<Instant> {

    private final DateTimeFormatter fmt = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm:ss")
            .withZone(ZoneId.of("Europe/Rome"));

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return Instant.from(fmt.parse(p.getText()));
    }
}
