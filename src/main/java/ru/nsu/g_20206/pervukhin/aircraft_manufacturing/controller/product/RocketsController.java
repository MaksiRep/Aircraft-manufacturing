package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Rockets;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.RocketsService;

import java.beans.Transient;

@Controller
public class RocketsController {

    @Autowired
    private RocketsService rocketsService;

    @PostMapping(value = "product/rockets")
    public ResponseEntity<Void> addRockets(@RequestBody Rockets rockets) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        rocketsService.addRockets(rockets);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "product/rockets/{rocketsId}")
    public ResponseEntity<Void> updateRockets(@RequestBody Rockets rockets,
                                            @PathVariable(name = "rocketsId") Integer rocketsId) throws NotFoundException, IncorrectInputException {
        rocketsService.updateRockets(rockets, rocketsId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "product/rockets/{rocketsId}")
    public ResponseEntity<Void> deleteRockets(@PathVariable(name = "rocketsId") Integer rocketsId) throws NotFoundException {
        rocketsService.deleteRocket(rocketsId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "product/rockets/{rocketsId}")
    public String getRockets (@PathVariable(name = "rocketsId") Integer rocketsId, Model model) throws NotFoundException {
        Rockets rockets = rocketsService.getRockets(rocketsId);
        model.addAttribute("rocketsId", rockets.getRocketsId());
        model.addAttribute("chargePower", rockets.getChargePower());
        model.addAttribute("type", rockets.getType());
        return "product/rockets/rocketsInfo";
    }
}
