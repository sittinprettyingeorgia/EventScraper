package com.example.galendata.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Page {
    private String url;
    private String containerId;
    private String tagId = "a";


    public Page (String url, String containerId){
        this.url = url;
        this.containerId = containerId;
    }
}
