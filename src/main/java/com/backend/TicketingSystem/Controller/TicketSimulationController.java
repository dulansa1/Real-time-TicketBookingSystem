package com.backend.TicketingSystem.Controller;
import com.backend.TicketingSystem.CLI.SystemConfig;
import com.backend.TicketingSystem.Service.TicketSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3005")
@RequestMapping("/api/v1")
public class TicketSimulationController {

    @Autowired
    private TicketSimulationService simulationService;

    @PostMapping("/start")
    public String startSimulation(@RequestBody SystemConfig config) {
        try {
            //Save configuration to config.json
            config.saveToJson("config.json");

            //Start Simulation
            simulationService.startSimulation(
                    config.getTotalTickets(),
                    config.getTicketReleaseRate(),
                    config.getCustomerRetrievalRate(),
                    config.getMaxTicketCapacity()
            );
            return "Simulation started successfully.";
        } catch (Exception e) {
            return "Error starting simulation: " + e.getMessage();
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation stopped successfully.");
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        List<String> logs = simulationService.getLogs();  // Retrieve logs from the service
        return ResponseEntity.ok(logs);  // Return logs to the frontend
    }
}
