package cwchoiit.gibungab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("cwchoiit.gibungab.infrastructure.properties")
public class GibungabApplication {

    public static void main(String[] args) {
        SpringApplication.run(GibungabApplication.class, args);
    }

}
