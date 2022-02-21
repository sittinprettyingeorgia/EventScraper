package com.example.galendata.controller;

import com.example.galendata.constants.AppConstants;
import com.example.galendata.model.GalenData;
import com.example.galendata.model.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ScraperTests {

    @Test
    void shouldReturnAMapOfPage1Data() throws Exception {
        Scraper scraper = new Scraper();
        Page page = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        Map<String, GalenData> data = scraper.scrapeData("event", page);
        assertNotNull(data);
        assertEquals(15,data.size());
    }
    @Test
    void shouldReturnAMapOfPage2Data() throws Exception {
        Scraper scraper = new Scraper();
        Page page = new Page(AppConstants.PAGE_2,AppConstants.PAGE_2_CONTAINER_ID);
        Map<String, GalenData> data = scraper.scrapeData("event", page);
        assertNotNull(data);
        assertEquals(110,data.size());
    }
    @Test
    void shouldRemoveDuplicates() throws Exception {
        Scraper scraper = new Scraper();
        Page page1 = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        Map<String, GalenData> data1 = scraper.scrapeData("event", page1);
        assertEquals(15,data1.size());
        Page page2 = new Page(AppConstants.PAGE_2,AppConstants.PAGE_2_CONTAINER_ID);
        Map<String, GalenData> data2 = scraper.scrapeData("event", page2);
        assertEquals(110,data2.size());
        data2.putAll(data1);
        assertEquals(123,data2.size());
    }
}
