package app.ccb.config;

import app.ccb.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public FileUtil fileUtil() {
        return new FileUtilImpl();
    }

    @Bean
    public Validator validator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl(validator());

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public XmlParser xmlParser(){
        return new XmlParserImpl();
    }
}
