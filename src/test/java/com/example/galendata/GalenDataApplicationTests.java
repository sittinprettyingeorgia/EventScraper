package com.example.galendata;


import com.example.galendata.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class GalenDataApplicationTests {

    @Autowired
    MainController controller;
    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
