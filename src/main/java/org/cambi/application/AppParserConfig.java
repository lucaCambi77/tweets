package org.cambi.application;

import org.cambi.service.TwitterParserRunnableService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppParserConfig {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TwitterParserRunnableService getTwitterServiceRunnable() {
        return new TwitterParserRunnableService();
    }

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

}
