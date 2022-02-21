package com.example.galendata.controller;


import com.example.galendata.GalenDataApplication;
import com.example.galendata.config.CrawlerConfig;
import com.example.galendata.config.MainConfig;
import com.example.galendata.constants.AppConstants;
import com.example.galendata.model.CrawlInput;
import com.example.galendata.model.Event;
import com.example.galendata.model.Page;
import com.example.galendata.repo.EventRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MainControllerTests {

    @Autowired
    ApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MainController controller;
    @Autowired
    EventRepo repo;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void shouldReturnResponseEntityWithCrawlData() throws Exception {
        Page page = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        List<Page> list = new ArrayList<>();
        list.add(page);
        CrawlInput input = new CrawlInput("event", list);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(input);

        MvcResult result = mockMvc
                .perform(post("/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(request().asyncStarted())
                        .andReturn();

        result.getAsyncResult(3000);

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test void shouldStoreCrawlData() throws Exception {
        Page page = new Page(AppConstants.PAGE_1,AppConstants.PAGE_1_CONTAINER_ID);
        List<Page> list = new ArrayList<>();
        list.add(page);
        CrawlInput input = new CrawlInput("event", list);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(input);

        MvcResult result = mockMvc
                .perform(post("/crawl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(request().asyncStarted())
                .andReturn();


        result.getAsyncResult();
        List<Event> events = repo.findAll();
        assertEquals(15,events.size());
    }
}
