package com.example.galendata.repo;

import com.example.galendata.constants.AppConstants;
import com.example.galendata.model.Event;
import com.example.galendata.model.GalenData;
import com.example.galendata.model.Page;
import com.example.galendata.service.Crawler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventRepoTests {
    @Autowired
    Crawler crawler;
    @Autowired
    EventRepo repo;

    @Test
    public void shouldSavePageToRepo() {
        Page page = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        List<Page> pages = new ArrayList<>();
        pages.add(page);
        Map<String, GalenData> data = crawler.search("event",pages);
        repo.saveAllAndFlush((Collection<Event>)(Collection<?>)data.values());
        List<Event> events = repo.findAll();
        assertEquals(15,events.size());
    }
}
