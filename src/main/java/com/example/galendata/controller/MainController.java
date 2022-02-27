package com.example.galendata.controller;

import com.example.galendata.model.CrawlInput;
import com.example.galendata.model.Event;
import com.example.galendata.model.GalenData;
import com.example.galendata.repo.EventRepo;
import com.example.galendata.service.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * Main RestController class that handles all responses/requests to the Spring Rest API service.
 * 
 */
@RestController
public class MainController {

    @Autowired
    ApplicationContext context;
    @Autowired
    EventRepo dataRepo;


    /**
     * non-blocking endpoint scrapes the provided pages for the type of data.
     * @param input A CrawlInput object with typeofData and list of pages.
     * @return
     */
    @PostMapping("/crawl")
    public CompletableFuture<ResponseEntity<Map<String, GalenData>>> crawl(@RequestBody CrawlInput input){
        Crawler crawler = context.getBean("crawler", Crawler.class);
        return CompletableFuture.supplyAsync(() -> {
            Map<String, GalenData> data = null;

            data = crawler.search(input.getTypeOfData(),input.getPages());
            dataRepo.saveAllAndFlush((Collection<Event>)(Collection<?>)data.values());
            return new ResponseEntity<>(data, HttpStatus.OK);
        });
    }
}
