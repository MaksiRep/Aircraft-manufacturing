package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevelopmentCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.TestingCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.TestingCycleService;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;

@Controller
public class TestingCycleController {

    @Autowired
    private TestingCycleService testingCycleService;

    @PostMapping(value = "creating/testing_cycle")
    public ResponseEntity<Void> addTestingCycle(@RequestBody TestingCycle testingCycle) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        testingCycleService.addTestingCycle(testingCycle);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/testing_cycle/{testCycleId}/{testOrdNum}")
    public ResponseEntity<TestingCycle> getTestingCycle(@PathVariable(name = "testCycleId") Integer testCycleId,
                                                        @PathVariable(name = "testOrdNum") Integer testOrdNum) throws NotFoundException {
        return new ResponseEntity<>(testingCycleService.getTestingCycle(testCycleId, testOrdNum), HttpStatus.OK);
    }

    @PutMapping(value = "creating/testing_cycle/{testCycleId}/{testOrdNum}")
    public ResponseEntity<Void> updateTestingCycle(@RequestBody TestingCycle testingCycle,
                                                   @PathVariable(name = "testCycleId") Integer testCycleId,
                                                   @PathVariable(name = "testOrdNum") Integer testOrdNum) throws NotFoundException, IncorrectInputException {
        testingCycleService.updateTestingCycle(testingCycle, testCycleId, testOrdNum);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "creating/testing_cycle/{testCycleId}/{testOrdNum}")
    public ResponseEntity<Void> deleteTestingCycle(@PathVariable(name = "testCycleId") Integer testCycleId,
                                                   @PathVariable(name = "testOrdNum") Integer testOrdNum) throws NotFoundException, IncorrectInputException {
        testingCycleService.deleteTestingCycle(testCycleId, testOrdNum);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/testing_cycle/{testCycleId}")
    public String getDevelopmentCycle(@PathVariable(name = "testCycleId") Integer testCycleId, Model model) throws NotFoundException {
        List<TestingCycle> testingCycleList = testingCycleService.getProductTestingCycleByCycleId(testCycleId);
        List<String> sectionsNamesList = testingCycleService.getEquipmentsNames(testingCycleList.get(0).getCycleId());
        HashMap<Integer, String> equipmentNames = new HashMap<>();
        for (int i = 0; i < testingCycleList.size(); i++) {
            equipmentNames.put(testingCycleList.get(i).getOrdNum(), sectionsNamesList.get(i));
        }
        model.addAttribute("equipmentNames", equipmentNames);
        model.addAttribute("testingCycleList", testingCycleList);
        return "productCreating/testingCycle/testingCycleInfo";
    }

}
