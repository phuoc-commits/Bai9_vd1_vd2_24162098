package vn.iotstar.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CloudinaryConfig {
    @Bean
    Cloudinary cloudinary(Environment environment) {
        return new Cloudinary(environment.getProperty("cloudinary.url", "cloudinary://key:secret@cloud"));
    }
}