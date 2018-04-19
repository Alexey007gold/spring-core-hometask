package ua.epam.spring.hometask.configuration;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ua.epam.spring.hometask.*"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
)
public class WebConfiguration implements WebMvcConfigurer {


    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5242880);
        return multipartResolver;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }

    @Bean
    public ResourceBundleViewResolver resourceBundleViewResolver() {
        ResourceBundleViewResolver resourceBundleViewResolver = new ResourceBundleViewResolver();
        resourceBundleViewResolver.setBasename("views");
        return resourceBundleViewResolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
        final FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        factory.setTemplateLoaderPath("classpath:/template/");
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(factory.createConfiguration());
        return freeMarkerConfigurer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/");
    }
}