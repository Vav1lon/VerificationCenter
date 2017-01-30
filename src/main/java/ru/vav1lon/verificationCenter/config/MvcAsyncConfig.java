package ru.vav1lon.verificationCenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MvcAsyncConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        super.configureAsyncSupport(configurer);
    }

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(8);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("spring-mvc-");
        executor.setThreadGroupName("spring-mvc-tg");
        return executor;
    }
}