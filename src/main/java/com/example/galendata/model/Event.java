package com.example.galendata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event implements GalenData {
    @Id
    @GeneratedValue
    private UUID id;

    private String websiteName;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;

    @Override
    public String getKey() {
        return this.name;
    }

    @Override
    public void setWebsiteName(String websiteName){
        this.websiteName = websiteName;
    }
}
