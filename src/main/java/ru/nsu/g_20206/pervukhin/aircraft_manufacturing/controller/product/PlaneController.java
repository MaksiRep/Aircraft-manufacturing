package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Plane;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.PlaneService;

import java.beans.Transient;

@Controller
public class PlaneController {

    @Autowired
    private PlaneService planeService;

    @PostMapping(value = "product/plane")
    public ResponseEntity<Void> addPlane(@RequestBody Plane plane) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        planeService.addPlane(plane);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "product/plane/{planeId}")
    public ResponseEntity<Void> updatePlane(@RequestBody Plane plane,
                                            @PathVariable(name = "planeId") Integer planeId) throws NotFoundException, IncorrectInputException {
        planeService.updatePlane(plane, planeId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "product/plane/{planeId}")
    public ResponseEntity<Void> deletePlane(@PathVariable(name = "planeId") Integer planeId) throws NotFoundException {
        planeService.deletePlane(planeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "product/plane/{planeId}")
    public String getPlane (@PathVariable(name = "planeId") Integer planeId, Model model) throws NotFoundException {
        Plane plane = planeService.getPlane(planeId);
        model.addAttribute("planeId", plane.getPlaneId());
        model.addAttribute("engineCount", plane.getEngineCount());
        model.addAttribute("type", plane.getType());
        model.addAttribute("wingspan", plane.getWingspan());
        return "product/plane/planeInfo";
    }
}
