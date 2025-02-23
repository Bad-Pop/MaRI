package io.github.badpop.mari.lib.http.monitoring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static io.vavr.API.Try;

public abstract class IOLogParser {

    private static final Logger LOGGER = Logger.getLogger(IOLogParser.class);
    private static final ObjectMapper objectMapper = _initMapper();

    public static String parse(Object objectToLog) {
        return Try(() -> objectMapper.writeValueAsString(objectToLog))
                .onFailure(t -> LOGGER.error("Unable to serialize io log to json...", t))
                .getOrElse(() -> "---ERROR---");
    }

    private static ObjectMapper _initMapper() {
        val mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new VavrModule())
                .configure(FAIL_ON_EMPTY_BEANS, false)
                .configure(WRITE_DATES_AS_TIMESTAMPS, false);

        val module = new SimpleModule()
                .addSerializer(ByteArrayOutputStream.class, new OutputSerializer(mapper))
                .addSerializer(ByteArrayInputStream.class, new InputSerializer(mapper));

        mapper.registerModule(module);

        return mapper;
    }

    private static class OutputSerializer extends StdSerializer<ByteArrayOutputStream> {

        private final ObjectMapper objectMapper;

        public OutputSerializer(ObjectMapper objectMapper) {
            super(ByteArrayOutputStream.class);
            this.objectMapper = objectMapper;
        }

        @Override
        public void serialize(ByteArrayOutputStream value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            val content = value.toString();
            try {
                gen.writeObject(objectMapper.readTree(content));
            } catch (IOException ioe) {
                gen.writeString(content);
            }
        }
    }

    private static class InputSerializer extends StdSerializer<ByteArrayInputStream> {

        private final ObjectMapper objectMapper;

        public InputSerializer(ObjectMapper objectMapper) {
            super(ByteArrayInputStream.class);
            this.objectMapper = objectMapper;
        }

        @Override
        public void serialize(ByteArrayInputStream value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            val content = value.toString();
            try {
                gen.writeObject(objectMapper.readTree(content));
            } catch (IOException ioe) {
                gen.writeString(content);
            }
        }
    }
}
