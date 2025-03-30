package finki_ukim.spell_check_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class SpellCheckBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpellCheckBackEndApplication.class, args);
    }
}
