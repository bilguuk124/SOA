package itmo.MainService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public HttpMessageConverter<Object> xmlMessageConverter(){
        ObjectMapper objectMapper = new XmlMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new MappingJackson2XmlHttpMessageConverter(objectMapper);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        converters.add(xmlMessageConverter());
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer contentNegotiationConfigurer){
        contentNegotiationConfigurer.defaultContentType(MediaType.APPLICATION_XML);
    }
}
