package edu.catolica.config;

import edu.catolica.infra.AutenticacaoInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AutenticacaoInterceptor autenticacaoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autenticacaoInterceptor)
                .addPathPatterns("/administradores/**")
                .addPathPatterns("/pacientes/**")
                .addPathPatterns("/profissionais/**")
                .excludePathPatterns("/pacientes")
                .excludePathPatterns("/clinicas")
                .excludePathPatterns("/usuarios/login");
    }
}
