package com.example.galendata.service;

import com.example.galendata.controller.Scraper;
import com.example.galendata.model.Event;
import com.example.galendata.model.GalenData;
import com.example.galendata.model.GalenDataFactory;
import com.example.galendata.model.Page;
import org.aspectj.util.SoftHashMap;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * Crawler Objects can crawl multiple websites concurrently. They utilize the scraper class to retrieve and process the
 * data from each website which is then stored in the data map.
 */

public class Crawler {
    /** The reason we are using a GalenData interface here instead of just the Event class is in the case we want to
     * search a page for multiple kinds of data. Suppose we wanted to search a list of webpages for events(same as
     * requirements), hospitals(including name, location and hours of operation), and movies(including show times,
     * purchase price, and movie name). Implementing the GalenData interface will allow these classes to be used
     * interchangeably so our crawler can retrieve different kinds of data.
     */
    private ExecutorService execService;
    private Integer threads;
    private final GalenDataFactory factory;

    public Crawler(){
        this.factory = GalenDataFactory.getFactory();
    }
    /**
     * Synchronized method to retrieve the next Url in the Queue of urls to be visited.
     * @return Page (next page in pageQueue)
     */
    private synchronized Page nextPage(List<Page> pageQueue, Map<String, Page> pagesVisited){
        if (pageQueue.isEmpty()) return null;

        Page nextPage = pageQueue.remove(0);
        pagesVisited.put(nextPage.getUrl(),nextPage);

        return nextPage;
    }

    private synchronized int getPagesVisited(Map<String,Page> pages){
        return pages.size();
    }

    private synchronized void addData(Map<String, GalenData> dataSum, Map<String, GalenData> dataPoint){
        dataSum.putAll(dataPoint);
    }
    /**
     * Sets concurrency of crawler.
     * @param num of threads
     */
    public void setConcurrency(Integer num){
        this.execService = Executors.newFixedThreadPool(num);
        this.threads = num;
    }
    /**
     * Conducts a search of the current url for the appropriate data.
     * @param typeOfData A string that specifies the kind of data we are looking for ie.(event, location, etc).
     * @param pages list of pages we want to search for data
     */
    public Map<String, GalenData> search(String typeOfData, List<Page> pages) {
        if(pages.isEmpty())return null;

        //we do not want to run into stale data, so we should clear our caches.
        List<Page> pageQueue = new ArrayList<>(pages);
        int PAGE_LIMIT = pageQueue.size();
        Map<String, GalenData> result = new HashMap<>();
        Map<String, Page> pagesVisited = new HashMap<>();

        //We do not want to assign more threads than there are pages.
        for(int i = 0; i<PAGE_LIMIT && i<threads; i++) {
            //Each thread within our pool will attempt to scrape a page until the page limit is reached.
            Future<Map<String, GalenData>> future = execService.submit(() -> {
                Map<String, GalenData> temp = new HashMap<>();
                Scraper scraper = new Scraper();
                Page currentPage = this.nextPage(pageQueue, pagesVisited);

                try {
                    temp = scraper.scrapeData(typeOfData, currentPage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return temp;
            });

            try{
                 this.addData(result,future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        execService.shutdown();
        try{
            execService.awaitTermination(3000,TimeUnit.MILLISECONDS);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return result;
    }



}
