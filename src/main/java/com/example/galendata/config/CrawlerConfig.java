package com.example.galendata.config;

import com.example.galendata.service.Crawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Creates and manages Crawler beans
 * @return Crawler
 */
@Configuration
@ComponentScan
public class CrawlerConfig {

    @Bean
    public Crawler crawler(){

        Crawler crawler = new Crawler ();

        //setConcurrency assigns amount of Threads operating within Crawler.
        crawler.setConcurrency(2);
        return crawler;
    }

}
