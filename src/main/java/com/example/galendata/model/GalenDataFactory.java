package com.example.galendata.model;

import java.time.LocalDate;
import java.util.UUID;

public final class GalenDataFactory {

    private static GalenDataFactory galenDataFactory = null;

    private GalenDataFactory(){}

    public GalenData getInstanceOfGalenData(String type) {
        if(type.equals("event"))return new Event();

        return null;
    }

    public static GalenDataFactory getFactory(){
        if(galenDataFactory == null){
            return new GalenDataFactory();
        }
        else return galenDataFactory;
    }

    public GalenData getEvent(String websiteName, String name, LocalDate start, LocalDate end, String location){
        UUID id = UUID.randomUUID();
        return new Event(id,websiteName,name,start,end,location);
    }

}
