package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.SectionMaster;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.SectionMasterService;

import java.beans.Transient;
import java.util.List;

@Controller
public class SectionMasterController {

    @Autowired
    private SectionMasterService sectionMasterService;

    @PostMapping(value = "staff/section_master")
    public ResponseEntity<Void> addSectionMaster(@RequestBody SectionMaster sectionMaster) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        sectionMasterService.addSectionMaster(sectionMaster);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/masters_section/{masterId}")
    public ResponseEntity<Section> getMastersSection(@PathVariable(name = "masterId") Integer masterId) throws NotFoundException {
        return new ResponseEntity<>(sectionMasterService.getSection(masterId), HttpStatus.OK);
    }

    @PutMapping(value = "staff/section_master/{sectionId}/{masterId}")
    public ResponseEntity<Void> updateSectionMaster(@RequestBody SectionMaster sectionMaster,
                                                    @PathVariable(name = "sectionId") Integer sectionId,
                                                    @PathVariable(name = "masterId") Integer masterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        sectionMasterService.updateSectionMaster(sectionMaster, sectionId, masterId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/section_master/{sectionId}/{masterId}")
    public ResponseEntity<Void> deleteSectionMaster(@PathVariable(name = "sectionId") Integer sectionId,
                                                    @PathVariable(name = "masterId") Integer masterId) throws NotFoundException, IncorrectInputException {
        sectionMasterService.deleteSectionMaster(sectionId, masterId);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "staff/section_masters/{sectionId}")
    public String getWorkshopMasters(@PathVariable(name = "sectionId") Integer sectionId, Model model) throws NotFoundException {
        List<Staff> staffList = sectionMasterService.getMasters(sectionId);
        model.addAttribute("staffList", staffList);
        return "sql_requests/masters/masters";
    }
}
