package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.DevCycleService;

import java.beans.Transient;
import java.util.List;

@RestController
public class DevCycleController {

    @Autowired
    private DevCycleService devCycleService;

    @PostMapping(value = "creating/dev_cycle")
    public ResponseEntity<Void> addDevCycle() throws OverflowException {
        devCycleService.addDevCycle();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/dev_cycle")
    public ResponseEntity<List<Integer>> getCycles(){
        return new ResponseEntity<>(devCycleService.getAllCycles(), HttpStatus.OK);
    }

    @Transient
    @DeleteMapping(value = "creating/dev_cycle/{cycleId}")
    public ResponseEntity<Void> deleteDevCycle(@PathVariable(name = "cycleId") Integer cycleId) throws NotFoundException, IncorrectInputException {
        devCycleService.deleteDevCycle(cycleId);
        return ResponseEntity.ok().build();
    }
}
