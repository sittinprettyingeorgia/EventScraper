package com.example.galendata.controller;

import com.example.galendata.model.CrawlInput;
import com.example.galendata.model.Event;
import com.example.galendata.model.GalenData;
import com.example.galendata.repo.EventRepo;
import com.example.galendata.service.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * Main RestController class that handles all responses/requests to the Spring Rest API service.
 * 
 */
@RestController
@CrossOrigin("http://localhost:3000")
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

    @PostMapping(value = "/api/crawl", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = "application/json")
    public CompletableFuture<ResponseEntity<Map<String, GalenData>>> crawl(@RequestBody CrawlInput input) {
        Crawler crawler = context.getBean("crawler", Crawler.class);
        return CompletableFuture.supplyAsync(() -> {
            Map<String, GalenData> data = null;
            data = crawler.search(input.getTypeOfData(),input.getPages());
            List<Event> goodData = new ArrayList<>();

            //check if we already have the data within our database.
            data.forEach((k,v) -> {
                if(!dataRepo.existsById(v.getId())) goodData.add((Event)v);
            });

            dataRepo.saveAllAndFlush(goodData);
            return new ResponseEntity<>(data, HttpStatus.OK);
        });
    }

    @GetMapping(value ="/api/getData",
            produces = "application/json")
    public ResponseEntity<List<Event>> getData(){
        List<Event> events = dataRepo.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
