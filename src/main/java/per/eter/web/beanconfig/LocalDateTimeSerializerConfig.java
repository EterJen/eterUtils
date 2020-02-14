package per.eter.web.beanconfig;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class LocalDateTimeSerializerConfig {
    @Value("${spring.jackson.date-format:yyyy-MM-dd}")
    private String dateFormat;
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateTimeFormat;

    // 方案一
    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat));
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone("GMT+8"));
            //builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

        };
    }
}

