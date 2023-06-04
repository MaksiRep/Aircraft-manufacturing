package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Helicopter;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.HelicopterService;

import java.beans.Transient;

@Controller
public class HelicopterController {

    @Autowired
    private HelicopterService helicopterService;

    @PostMapping(value = "product/helicopter")
    public ResponseEntity<Void> addHelicopter(@RequestBody Helicopter helicopter) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        helicopterService.addHelicopter(helicopter);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "product/helicopter/{helicopterId}")
    public ResponseEntity<Void> updateHelicopter(@RequestBody Helicopter helicopter,
                                                 @PathVariable(name = "helicopterId") Integer helicopterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        helicopterService.updateHelicopter(helicopter, helicopterId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "product/helicopter/{helicopterId}")
    public ResponseEntity<Void> deleteHelicopter(@PathVariable(name = "helicopterId") Integer helicopterId) throws NotFoundException {
        helicopterService.deleteHelicopter(helicopterId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "product/helicopter/{helicopterId}")
    public String getHelicopter (@PathVariable(name = "helicopterId") Integer helicopterId, Model model) throws NotFoundException {
        Helicopter helicopter = helicopterService.getHelicopter(helicopterId);
        model.addAttribute("helicopterId", helicopter.getHelicopterId());
        model.addAttribute("propellersSize", helicopter.getPropellersSize());
        return "product/helicopter/helicopterInfo";
    }
}
