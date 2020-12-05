/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.covidtracker.controllers;

import com.example.covidtracker.models.LocationStats;
import com.example.covidtracker.services.CoronaVirusDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author I331711
 */

@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataService covidDataService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = covidDataService.getAllStats();
        
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats );
        model.addAttribute("totalReportedCases", totalReportedCases );
        model.addAttribute("totalNewCases", totalNewCases);
        
        return "./home";
    }
    
    
    
}
