/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.covidtracker.services;


import com.example.covidtracker.models.LocationStats;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author I331711
 */

@Service
public class CoronaVirusDataService {
    
    private static String VIRUS_DATA_URL = 
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    
    private List<LocationStats> allStats = new ArrayList<>();
    
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        StringReader csvBodyReader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            
            LocationStats locationStat = new LocationStats();
            
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            
            int latestTotalCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayTotalCases = Integer.parseInt(record.get(record.size()-2));
            locationStat.setLatestTotalCases(latestTotalCases);
            locationStat.setDiffFromPrevDay(latestTotalCases - prevDayTotalCases);
            
            newStats.add(locationStat);
        }
        
        
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
    
    
    
}
