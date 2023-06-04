package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkshopMaster;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.WorkshopMasterService;

import java.beans.Transient;
import java.util.List;

@Controller
public class WorkshopMasterController {

    @Autowired
    private WorkshopMasterService workshopMasterService;

    @PostMapping(value = "staff/workshop_master")
    public ResponseEntity<Void> addWorkshopMaster(@RequestBody WorkshopMaster workshopMaster) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        workshopMasterService.addWorkshopMaster(workshopMaster);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/masters_workshop/{masterId}")
    public ResponseEntity<Workshop> getMastersWorkshop(@PathVariable(name = "masterId") Integer masterId) throws NotFoundException {
        return new ResponseEntity<>(workshopMasterService.getWorkshop(masterId), HttpStatus.OK);
    }

    @PutMapping(value = "staff/workshop_master/{workshopId}/{masterId}")
    public ResponseEntity<Void> updateWorkshopMaster(@RequestBody WorkshopMaster workshopMaster,
                                                     @PathVariable(name = "workshopId") Integer workshopId,
                                                     @PathVariable(name = "masterId") Integer masterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        workshopMasterService.updateWorkshopMaster(workshopMaster, workshopId, masterId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/workshop_master/{workshopId}/{masterId}")
    public ResponseEntity<Void> deleteWorkshopMaster(@PathVariable(name = "workshopId") Integer workshopId,
                                                     @PathVariable(name = "masterId") Integer masterId) throws NotFoundException {
        workshopMasterService.deleteWorkshopMaster(workshopId, masterId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/workshop_masters/{workshopId}")
    public String getWorkshopMasters(@PathVariable(name = "workshopId") Integer workshopId, Model model) throws NotFoundException {
        List<Staff> staffList = workshopMasterService.getMasters(workshopId);
        model.addAttribute("staffList", staffList);
        return "sql_requests/masters/masters";
    }
}
