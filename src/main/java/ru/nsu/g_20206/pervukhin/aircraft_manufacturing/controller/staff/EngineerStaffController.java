package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.EngineeringStaffService;

import java.beans.Transient;
import java.text.ParseException;
import java.util.List;

@Controller
public class EngineerStaffController {

    @Autowired
    private EngineeringStaffService engineeringStaffService;

    @PostMapping(value = "staff/engineer")
    public ResponseEntity<Void> addEngineer(@RequestBody EngineeringStaff engineeringStaff) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        engineeringStaffService.addEngineer(engineeringStaff);
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = "staff/engineer/{engineerId}")
//    public ResponseEntity<EngineeringStaff> getEngineer(@PathVariable(name = "engineerId") Integer engineerId) throws NotFoundException {
//        return new ResponseEntity<>(engineeringStaffService.getEngineer(engineerId), HttpStatus.OK);
//    }

    @PutMapping(value = "staff/engineer/{engineerId}")
    public ResponseEntity<Void> updateEngineer(@RequestBody EngineeringStaff engineeringStaff,
                                            @PathVariable(name = "engineerId") Integer engineerId) throws NotFoundException, IncorrectInputException {
        engineeringStaffService.updateEngineer(engineeringStaff, engineerId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/engineer/{engineerId}")
    public ResponseEntity<Void> deleteEngineer(@PathVariable(name = "engineerId") Integer engineerId) throws NotFoundException, IncorrectInputException {
        engineeringStaffService.deleteEngineer(engineerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/engineer/{engineerId}")
    public String getEngineer(@PathVariable(name = "engineerId") Integer engineerId, Model model) throws NotFoundException {
        EngineeringStaff engineeringStaff = engineeringStaffService.getEngineer(engineerId);

        model.addAttribute("specialization", engineeringStaff.getSpecialization());
        model.addAttribute("engineeringId", engineeringStaff.getEngineeringId());
        return "staff/engineeringStaff/EngineeringStaffInfo";
    }

    @GetMapping(value = "staff/staff/addEngineer")
    public String addFormStaffEngineer(Model model) {
        model.addAttribute("engineerRequest", new EngineerRequest());
        return "staff/staff/createEngineer";
    }

    @PostMapping(value = "staff/staffAddEngineer")
    public String addStaffEngineer(@ModelAttribute("engineerRequest") EngineerRequest engineerRequest) throws IncorrectDateException, ParseException, AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        engineeringStaffService.createStaffEngineer(engineerRequest);
        return "redirect:/staff/allStaff";
    }
}