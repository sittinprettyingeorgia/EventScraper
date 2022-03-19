package com.example.galendata.model;


import java.util.UUID;

public interface GalenData {
     String getKey();
     UUID getId();
     void setWebsiteName(String webSiteName);
}