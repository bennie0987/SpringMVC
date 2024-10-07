package com.app.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebConfig {

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setOrder(1); // Set the order of view resolvers if you have multiple
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver);
        // Add other configurations if needed
        return templateEngine;
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/"); // This should match your template location
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
}
