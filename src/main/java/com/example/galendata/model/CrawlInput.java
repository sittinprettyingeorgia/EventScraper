package com.example.galendata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawlInput {
    private String typeOfData;
    private List<Page> pages;


    public void addPage(Page page){
        this.pages.add(page);
    }

}
