package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Glider;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.GliderService;

import java.beans.Transient;

@Controller
public class GliderController {

    @Autowired
    private GliderService gliderService;

    @PostMapping(value = "product/glider")
    public ResponseEntity<Void> addGlider(@RequestBody Glider glider) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        gliderService.addGlider(glider);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "product/glider/{gliderId}")
    public ResponseEntity<Void> updateGlider(@RequestBody Glider glider,
                                             @PathVariable(name = "gliderId") Integer gliderId) throws NotFoundException, IncorrectInputException {
        gliderService.updateGlider(glider, gliderId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "product/glider/{gliderId}")
    public ResponseEntity<Void> deleteGlider(@PathVariable(name = "gliderId") Integer gliderId) throws NotFoundException {
        gliderService.deleteGlider(gliderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "product/glider/{gliderId}")
    public String getGlider(@PathVariable(name = "gliderId") Integer gliderId, Model model) throws NotFoundException {
        Glider glider = gliderService.getGlider(gliderId);
        model.addAttribute("gliderId", glider.getGliderId());
        model.addAttribute("wingspan", glider.getWingspan());
        return "product/glider/gliderInfo";
    }
}
