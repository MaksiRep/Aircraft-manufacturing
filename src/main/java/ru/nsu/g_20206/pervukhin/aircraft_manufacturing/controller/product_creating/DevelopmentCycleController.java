package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevelopmentCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.DevelopmentCycleService;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;

@Controller
public class DevelopmentCycleController {

    @Autowired
    private DevelopmentCycleService developmentCycleService;

    @PostMapping(value = "creating/development_cycle")
    public ResponseEntity<Void> addDevelopmentCycle(@RequestBody DevelopmentCycle developmentCycle) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        developmentCycleService.addDevelopmentCycle(developmentCycle);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/development_cycle/{devCycleId}/{devOrdNum}")
    public ResponseEntity<DevelopmentCycle> getDevelopmentCycle(@PathVariable(name = "devCycleId") Integer devCycleId,
                                                                @PathVariable(name = "devOrdNum") Integer devOrdNum) throws NotFoundException {
        return new ResponseEntity<>(developmentCycleService.getDevelopmentCycle(devCycleId, devOrdNum), HttpStatus.OK);
    }

    @PutMapping(value = "creating/development_cycle/{devCycleId}/{devOrdNum}")
    public ResponseEntity<Void> updateDevelopmentCycle(@RequestBody DevelopmentCycle developmentCycle,
                                                       @PathVariable(name = "devCycleId") Integer devCycleId,
                                                       @PathVariable(name = "devOrdNum") Integer devOrdNum) throws NotFoundException, IncorrectInputException {
        developmentCycleService.updateDevelopmentCycle(developmentCycle, devCycleId, devOrdNum);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "creating/development_cycle/{devCycleId}/{devOrdNum}")
    public ResponseEntity<Void> deleteDevelopmentCycle(@PathVariable(name = "devCycleId") Integer devCycleId,
                                                       @PathVariable(name = "devOrdNum") Integer devOrdNum) throws NotFoundException, IncorrectInputException {
        developmentCycleService.deleteDevelopmentCycle(devCycleId, devOrdNum);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/development_cycle/{devCycleId}")
    public String getDevelopmentCycle(@PathVariable(name = "devCycleId") Integer devCycleId, Model model) {
        List<DevelopmentCycle> developmentCycleList = developmentCycleService.getDevelopmentCycleByCycleId(devCycleId);
        List<String> sectionsNamesList = developmentCycleService.getSectionNames(developmentCycleList.get(0).getCycleId());
        HashMap<Integer, String> sectionNames = new HashMap<>();
        for (int i = 0; i < developmentCycleList.size(); i++) {
            sectionNames.put(developmentCycleList.get(i).getOrdNum(), sectionsNamesList.get(i));
        }
        model.addAttribute("developmentCycleList", developmentCycleList);
        model.addAttribute("sectionNames", sectionNames);
        return "productCreating/developmentCycle/developmentCycleInfo";
    }
}
