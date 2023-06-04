package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DateRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DevelopmentRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Development;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.DevelopmentService;

import java.beans.Transient;
import java.text.ParseException;
import java.util.List;

@Controller
public class DevelopmentController {

    @Autowired
    private DevelopmentService developmentService;

    @GetMapping(value = "creating/development/{developmentId}")
    public String getDevelopment(@PathVariable(name = "developmentId") Integer developmentId, Model model) throws NotFoundException {
        Development development = developmentService.getDevelopment(developmentId);
        String brigadeName = developmentService.getBrigadeName(development.getBrigadeId());
        model.addAttribute("developmentId", development.getDevelopmentId());
        model.addAttribute("serialNumber", development.getSerialNumber());
        model.addAttribute("cycleId", development.getCycleId());
        model.addAttribute("devStep", development.getDevStep());
        model.addAttribute("startDevDate", development.getStartDevDate());
        model.addAttribute("endDevDate", development.getEndDevDate());
        model.addAttribute("brigadeId", development.getBrigadeId());
        model.addAttribute("brigadeName", brigadeName);
        return "productCreating/development/developmentInfo";
    }

    @GetMapping(value = "creating/development")
    public String getDevelopments(Model model) {
        List<Development> developmentList = developmentService.getAllDevelopments();
        model.addAttribute("developmentList", developmentList);
        return "productCreating/development/developmentList";
    }

    @Transient
    @PostMapping(value = "creating/development/{developmentId}")
    public String deleteDevelopment(@PathVariable(name = "developmentId") Integer developmentId) throws NotFoundException {
        developmentService.deleteDevelopment(developmentId);
        return "redirect:/creating/development";
    }

    @GetMapping(value = "creating/getAddFormDevelopment")
    public String getAddFormDevelopment(Model model) {
        model.addAttribute("developmentRequest", new DevelopmentRequest());
        model.addAttribute("devCycles", developmentService.getAllDevCycles());
        model.addAttribute("brigadesIds", developmentService.getAllBrigades());
        return "productCreating/development/addDevelopment";
    }

    @PostMapping(value = "creating/addDevelopment")
    public String addDevelopment(@ModelAttribute("developmentRequest") DevelopmentRequest developmentRequest) throws IncorrectDateException, IncorrectInputException, ParseException, AlreadyExistException, OverflowException {
        developmentService.addDevelopment(developmentRequest);
        return "redirect:/creating/development";
    }

    @GetMapping(value = "creating/getUpdateFromDevelopment/{developmentId}")
    public String getUpdateFromDevelopment(@PathVariable(name = "developmentId") Integer developmentId, Model model) throws NotFoundException {
        Development oldDevelopment = developmentService.getDevelopment(developmentId);
        model.addAttribute("developmentId", developmentId);
        model.addAttribute("oldDevelopment", oldDevelopment);
        model.addAttribute("developmentRequest", new DevelopmentRequest());
        model.addAttribute("devCycles", developmentService.getAllDevCycles());
        model.addAttribute("brigadesIds", developmentService.getAllBrigades());
        return "productCreating/development/updateDevelopment";
    }

    @PostMapping(value = "creating/updateDevelopment/{developmentId}")
    public String updateDevelopment(@ModelAttribute("development") DevelopmentRequest developmentRequest,
                                    @PathVariable(name = "developmentId") Integer developmentId) throws NotFoundException, IncorrectInputException, IncorrectDateException, ParseException, AlreadyExistException {
        developmentService.updateDevelopment(developmentRequest, developmentId);
        return "redirect:/creating/development";
    }
}
