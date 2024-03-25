package com.gisnet.erpp.config;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

import io.tracee.binding.springmvc.TraceeInterceptor;

/**
 * = WebMvcConfiguration
 TODO Auto-generated class documentation
 *
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {


    /**
     * TODO Auto-generated attribute documentation
     *
     */
    private ApplicationContext applicationContext;

    /**
     * TODO Auto-generated method documentation
     *
     * @return LocalValidatorFactoryBean
     */
    @Primary
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * TODO Auto-generated method documentation
     *
     * @return LocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en"));
        return localeResolver;
    }

    /**
     * TODO Auto-generated method documentation
     *
     * @return LocaleChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * TODO Auto-generated method documentation
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new TraceeInterceptor());
    }



    /**
     * TODO Auto-generated method documentation
     *
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * TODO Auto-generated method documentation
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
   
   
}
