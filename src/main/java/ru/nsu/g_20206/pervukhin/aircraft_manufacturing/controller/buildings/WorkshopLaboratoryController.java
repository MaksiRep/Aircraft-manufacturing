package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.WorkshopLaboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopLaboratoryService;

import java.beans.Transient;
import java.util.List;

@RestController
public class WorkshopLaboratoryController {

    @Autowired
    private WorkshopLaboratoryService workshopLaboratoryService;

    @PostMapping(value = "buildings/workshop_laboratory")
    public ResponseEntity<Void> addWorkshopLaboratory(@RequestBody WorkshopLaboratory workshopLaboratory) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        workshopLaboratoryService.addWorkshopLaboratory(workshopLaboratory);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "buildings/workshop_laboratories/{workshopId}")
    public ResponseEntity<List<Laboratory>> getWorkshopLaboratories(@PathVariable(name = "workshopId") Integer workshopId) throws NotFoundException {
        return new ResponseEntity<>(workshopLaboratoryService.getWorkshopLaboratories(workshopId), HttpStatus.OK);
    }

    @GetMapping(value = "buildings/laboratory_workshops/{laboratoryId}")
    public ResponseEntity<List<Workshop>> getLaboratoryWorkshops(@PathVariable(name = "laboratoryId") Integer laboratoryId) throws NotFoundException {
        return new ResponseEntity<>(workshopLaboratoryService.getLaboratoryWorkshops(laboratoryId), HttpStatus.OK);
    }

    @PutMapping(value = "buildings/workshop_laboratory/{workshopId}/{laboratoryId}")
    public ResponseEntity<Void> updateWorkshopLaboratory(@RequestBody WorkshopLaboratory workshopLaboratory,
                                               @PathVariable(name = "workshopId") Integer workshopId,
                                               @PathVariable(name = "laboratoryId") Integer laboratoryId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        workshopLaboratoryService.updateWorkshopLaboratory(workshopLaboratory, workshopId, laboratoryId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "buildings/workshop_laboratory/{workshopId}/{laboratoryId}")
    public ResponseEntity<Void> deleteWorkshopLaboratory(@PathVariable(name = "workshopId") Integer workshopId,
                                               @PathVariable(name = "laboratoryId") Integer laboratoryId) throws NotFoundException, IncorrectInputException {
        workshopLaboratoryService.deleteWorkshopLaboratory(workshopId,laboratoryId);
        return ResponseEntity.ok().build();
    }
}
