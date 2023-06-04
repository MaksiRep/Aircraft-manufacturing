package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.BrigadeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Brigade;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.BrigadeService;

import java.beans.Transient;

@Controller
public class BrigadeController {

    @Autowired
    private BrigadeService brigadeService;

    @PostMapping(value = "staff/brigade")
    public ResponseEntity<Void> addBrigade(@RequestBody BrigadeRequest brigadeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        brigadeService.addBrigade(brigadeRequest);
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = "staff/brigade/{brigadeId}")
//    public ResponseEntity<Brigade> getBrigade(@PathVariable(name = "brigadeId") Integer brigadeId) throws NotFoundException {
//        return new ResponseEntity<>(brigadeService.getBrigade(brigadeId), HttpStatus.OK);
//    }

    @PutMapping(value = "staff/brigade/{brigadeId}")
    public ResponseEntity<Void> updateBrigade(@RequestBody Brigade brigade,
                                              @PathVariable(name = "brigadeId") Integer brigadeId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        brigadeService.updateBrigade(brigade, brigadeId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/brigade/{brigadeId}")
    public ResponseEntity<Void> deleteBrigade(@PathVariable(name = "brigadeId") Integer brigadeId) throws NotFoundException, IncorrectInputException {
        brigadeService.deleteBrigade(brigadeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/brigade/{brigadeId}")
    public String getBrigade(@PathVariable(name = "brigadeId") Integer brigadeId, Model model) throws NotFoundException {
        Brigade brigade = brigadeService.getBrigade(brigadeId);
        model.addAttribute("brigadeId", brigade.getBrigadeId());
        model.addAttribute("name", brigade.getName());
        model.addAttribute("sectionId", brigade.getSectionId());
        model.addAttribute("brigadierId", brigade.getBrigadierId());
        return "staff/brigade/brigadeInfo";
    }

}
