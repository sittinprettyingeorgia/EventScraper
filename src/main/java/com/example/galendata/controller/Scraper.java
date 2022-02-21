package com.example.galendata.controller;

import com.example.galendata.model.GalenData;
import com.example.galendata.model.GalenDataFactory;
import com.example.galendata.model.Page;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import com.example.galendata.constants.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Controls HTTP interactions.
 */
@Controller
@NoArgsConstructor
public class Scraper {

    private static final String USER_AGENT = AppConstants.USER_AGENT;

    /**
     * Returns a map whose key is the name of the data and whose value is the data object.
     * @param page the webpage to be scraped
     * @return  Map of events found.
     * @throws Exception in the event we retrieve something that is not an html document.
     */
    public Map<String, GalenData> scrapeData(String typeOfData, Page page) throws Exception{
        if (page == null) return null;

        if(!page.getUrl().matches(AppConstants.VALID_URL))
            throw new Exception("**Malformed URl: url needs to be in format (https://www.walmart.com)" +
                    "or http://www.walmart.com");

        String url = page.getUrl();
        Map<String, GalenData> data = new HashMap<>();

        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDoc = connection.get();

            if (!Objects.requireNonNull(connection.response().contentType()).contains("text/html"))
                throw new Exception("**Failure** Retrieved something other than HTML");

            Element content = htmlDoc.getElementById(page.getContainerId());
            assert content != null;
            Elements rows =  content.getElementsByTag(page.getTagId());

            for(Element row: rows){
                GalenData galenData = getData(typeOfData, row);
                if(galenData == null)continue;

                galenData.setWebsiteName(url);
                data.put(galenData.getKey(), galenData);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private GalenData getData(String typeOfData, Element row) throws Exception {
        if(typeOfData.equals("event")) {
        /*
          If row has attribute title we need to access its parent's siblings to see the rest of our event info.
         */
            if (row.hasAttr("title")) return processParentSiblingsForEvent(row);

            /* If row doesn't have the title attribute we need to process children to see the rest of our event info.*/
            return processSiblingsForEvent(row);
        }

        throw new Exception("We do not currently have the capability to scrape for the specified type of data.");
    }

    private GalenData processParentSiblingsForEvent(Element row){
        String title = row.attr("title");
        Element parent = row.parent();
        Element[] siblings = new Element[4];
        assert parent != null;
        siblings = parent.siblingElements().toArray(siblings);

        LocalDate startDate = LocalDate.parse(siblings[1].ownText());
        LocalDate endDate = LocalDate.parse(siblings[2].ownText());
        String location = siblings[3].ownText();

        return GalenDataFactory.getFactory().getEvent("",title,startDate,endDate,location);
    }

    private GalenData processSiblingsForEvent(Element row){
        if(row.children().isEmpty()) return null;

        Element[] children = new Element[3];
        children = row.children().toArray(children);


        /*
         * get our start and end dates.
         */
        String dateText = children[0].ownText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
        LocalDate startDate;
        LocalDate endDate;

        if(dateText.contains("-")){
            String[] dates = dateText.split("-");
            startDate = LocalDate.from(formatter.parse(dates[0] + AppConstants.YEAR));
            endDate = ( dateText.matches(AppConstants.SINGLE_MONTH_DATE))
                    ? LocalDate.from(formatter.parse(dates[0].substring(0, 3) + " " + dates[1] + AppConstants.YEAR))
                    : LocalDate.from(formatter.parse(dates[1] + AppConstants.YEAR));
        }
        else {
            startDate = LocalDate.from(formatter.parse(dateText + AppConstants.YEAR));
            endDate = startDate;
        }

        /*
         * get the event title.
         */
        String title = children[1].ownText();

        /*
         * get our location
         */
        String location = children[2].ownText();

        return GalenDataFactory.getFactory().getEvent("",title,startDate,endDate,location);
    }
}

