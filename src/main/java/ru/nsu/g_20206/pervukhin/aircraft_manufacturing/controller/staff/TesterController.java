package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.TesterRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Tester;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkerStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.TesterService;

import java.beans.Transient;
import java.text.ParseException;

@Controller
public class TesterController {

    @Autowired
    private TesterService testerService;

    @PostMapping(value = "staff/tester")
    public ResponseEntity<Void> addTester (@RequestBody Tester tester) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        testerService.addTester(tester);
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = "staff/tester/{testerId}")
//    public ResponseEntity<Tester> getTester(@PathVariable(name = "testerId") Integer testerId) throws NotFoundException {
//        return new ResponseEntity<>(testerService.getTester(testerId), HttpStatus.OK);
//    }

    @PutMapping(value = "staff/tester/{testerId}")
    public ResponseEntity<Void> updateTester(@RequestBody Tester tester,
                                            @PathVariable(name = "testerId") Integer testerId) throws NotFoundException, IncorrectInputException {
        testerService.updateTester(tester, testerId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/tester/{testerId}")
    public ResponseEntity<Void> deleteTester(@PathVariable(name = "testerId") Integer testerId) throws NotFoundException, IncorrectInputException {
        testerService.deleteTester(testerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/tester/{testerId}")
    public String getTesterId(@PathVariable(name = "testerId") Integer testerId, Model model) throws NotFoundException {
        Tester tester = testerService.getTester(testerId);
        Laboratory laboratory = testerService.getLaboratory(tester.getLaboratoryId());
        model.addAttribute("laboratoryId", laboratory.getId());
        model.addAttribute("laboratoryName", laboratory.getName());
        model.addAttribute("testerId", tester.getTesterId());
        return "staff/tester/testerInfo";
    }

    @GetMapping(value = "staff/staff/addTester")
    public String addFormStaffTester(Model model) {
        model.addAttribute("testerRequest", new TesterRequest());
        model.addAttribute("allStaffTypes", testerService.getAllStaffTypes());
        model.addAttribute("laboratoriesIds", testerService.getAllLaboratories());
        return "staff/staff/createTester";
    }

    @PostMapping(value = "staff/staffAddTester")
    public String addStaffTester(@ModelAttribute("testerRequest") TesterRequest testerRequest) throws IncorrectDateException, ParseException, AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        testerService.createStaffTester(testerRequest);
        return "redirect:/staff/allStaff";
    }

}
