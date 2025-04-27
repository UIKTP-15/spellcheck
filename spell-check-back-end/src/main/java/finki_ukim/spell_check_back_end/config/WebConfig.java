package finki_ukim.spell_check_back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/users/login",
                        "/users/register",
                        "/styles.css",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/static/**",
                        "/webjars/**",
                        "/favicon.ico"
                );
    }
}