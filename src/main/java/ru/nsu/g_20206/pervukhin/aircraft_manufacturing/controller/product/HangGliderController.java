package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.HangGlider;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.HangGliderService;

import java.beans.Transient;

@Controller
public class HangGliderController {

    @Autowired
    private HangGliderService hangGliderService;

    @PostMapping(value = "product/hang_glider")
    public ResponseEntity<Void> addHangGlider(@RequestBody HangGlider hangGlider) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        hangGliderService.addHangGlider(hangGlider);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "product/hang_glider/{hangGliderId}")
    public ResponseEntity<Void> updateHangGlider(@RequestBody HangGlider hangGlider,
                                                 @PathVariable(name = "hangGliderId") Integer hangGliderId) throws NotFoundException, IncorrectInputException {
        hangGliderService.updateHangGlider(hangGlider, hangGliderId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "product/hang_glider/{hangGliderId}")
    public ResponseEntity<Void> deleteHangGlider(@PathVariable(name = "hangGliderId") Integer hangGliderId) throws NotFoundException {
        hangGliderService.deleteHangGlider(hangGliderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "product/hang_glider/{hangGliderId}")
    public String getHangGlider(@PathVariable(name = "hangGliderId") Integer hangGliderId, Model model) throws NotFoundException {
        HangGlider hangGlider = hangGliderService.getHangGlider(hangGliderId);
        model.addAttribute("hangGliderId", hangGlider.getHangGliderId());
        model.addAttribute("capacity", hangGlider.getCapacity());
        model.addAttribute("wingspan", hangGlider.getWingspan());
        return "product/hangGlider/hangGliderInfo";
    }
}
