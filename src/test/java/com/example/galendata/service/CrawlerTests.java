package com.example.galendata.service;

import com.example.galendata.constants.AppConstants;
import com.example.galendata.model.GalenData;
import com.example.galendata.model.Page;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class CrawlerTests {
    @Autowired
    Crawler crawler;

    @Test
    public void shouldScrapePage1() {
        Page page = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        List<Page> pages = new ArrayList<>();
        pages.add(page);
        Map<String, GalenData> result = crawler.search("event",pages);
        assertEquals(15,result.size());
    }

    @Test void shouldScrapePage2(){
        Page page2 = new Page(AppConstants.PAGE_2, AppConstants.PAGE_2_CONTAINER_ID);
        List<Page> pages = new ArrayList<>();
        pages.add(page2);
        Map<String, GalenData> result = crawler.search("event",pages);
        assertEquals(110, result.size());
    }

    @Test void shouldScrapeBothPages(){
        Page page1 = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        Page page2 = new Page(AppConstants.PAGE_2, AppConstants.PAGE_2_CONTAINER_ID);
        List<Page> pages = new ArrayList<>();
        pages.add(page1);
        pages.add(page2);
        Map<String, GalenData> result = crawler.search("event",pages);
        assertEquals(123,result.size());
    }
}
