package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.TestCycleService;

import java.beans.Transient;
import java.util.List;

@RestController
public class TestCycleController {

    @Autowired
    private TestCycleService testCycleService;

    @PostMapping(value = "creating/test_cycle")
    public ResponseEntity<Void> addTestCycle() throws OverflowException {
        testCycleService.addTestCycle();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/test_cycle")
    public ResponseEntity<List<Integer>> getCycles(){
        return new ResponseEntity<>(testCycleService.getAllCycles(), HttpStatus.OK);
    }

    @Transient
    @DeleteMapping(value = "creating/test_cycle/{cycleId}")
    public ResponseEntity<Void> deleteTestCycle(@PathVariable(name = "cycleId") Integer cycleId) throws NotFoundException, IncorrectInputException {
        testCycleService.deleteTestCycle(cycleId);
        return ResponseEntity.ok().build();
    }
}
