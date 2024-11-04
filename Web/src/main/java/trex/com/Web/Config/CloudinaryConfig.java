package trex.com.Web.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "ddhprb3ad");
        config.put("api_key", "976873522311814");
        config.put("api_secret", "vsmrugl6CG33ES5ivVN2uZZNZFQ");
        return new Cloudinary(config);
    }
}
