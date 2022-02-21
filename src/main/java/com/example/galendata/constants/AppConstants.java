package com.example.galendata.constants;

import com.example.galendata.model.Page;

import java.util.ArrayList;
import java.util.Arrays;

public final class AppConstants {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1" +
            " (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    public static final String VALID_URL = "https://.*\\.[a-zA-Z0-9]*\\.com.*|http://.*\\.[a-zA-Z0-9]*\\.com.*";
    public static final String SINGLE_MONTH_DATE = "[A-Za-z]*\\s[0-9]*-[0-9]*";
    public static final String DATE_FORMAT = "MMM d uuuu";
    public static final String YEAR = " 2022";
    public static final String PAGE_1 = "https://www.computerworld.com/article/3313417/" +
            "tech-event-calendar-2020-upcoming-shows-conferences-and-it-expos.html";
    public static final String PAGE_1_CONTAINER_ID = "cwsearchabletable";
    public static final String PAGE_2_CONTAINER_ID = "events";
    public static final String PAGE_2 = "https://www.techmeme.com/events";
    public static final ArrayList<Page> PAGES = new ArrayList<>(Arrays.asList(new Page(PAGE_1,PAGE_1_CONTAINER_ID),
            new Page(PAGE_2, PAGE_2_CONTAINER_ID)));

}
