package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DateRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.LaboratoryService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.StaffService;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    @PostMapping(value = "buildings/laboratory")
    public String addLaboratory(@ModelAttribute("laboratoryRequest") LaboratoryRequest laboratoryRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {
        laboratoryService.addLaboratory(laboratoryRequest);
        return "redirect:/buildings/laboratories";
    }

    @GetMapping(value = "buildings/laboratory/{laboratoryId}")
    public String getLaboratory(Model model, @PathVariable(name = "laboratoryId") Integer laboratoryId) throws NotFoundException {
        Laboratory laboratory = laboratoryService.getLaboratory(laboratoryId);
        model.addAttribute("id", laboratory.getId());
        model.addAttribute("name", laboratory.getName());
        model.addAttribute("address", laboratory.getAddress());
        return "buildings/laboratory/laboratoryInfo";
    }

    @GetMapping(value = "buildings/laboratory_product_development/{productId}")
    public ResponseEntity<List<Laboratory>> getLaboratoryByProdId(@PathVariable(name = "productId") Integer productId) throws NotFoundException {
        return new ResponseEntity<>(laboratoryService.getLaboratoriesByProductIdDevelop(productId), HttpStatus.OK);
    }


    @GetMapping(value = "buildings/laboratory/getUpdate/{laboratoryId}")
    public String getLabUpdate(@PathVariable(name = "laboratoryId") Integer laboratoryId, Model model) throws NotFoundException {
        model.addAttribute("laboratory", new Laboratory());
        model.addAttribute("oldLaboratory", laboratoryService.getLaboratory(laboratoryId));
        model.addAttribute("id", laboratoryId);
        return "buildings/laboratory/updateLaboratory";
    }

    @PostMapping(value = "buildings/laboratory/update/{laboratoryId}")
    public String updateLaboratory(@PathVariable(name = "laboratoryId") Integer laboratoryId,
                                   @ModelAttribute("laboratory") Laboratory laboratory) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        laboratoryService.updateLaboratory(laboratory, laboratoryId);
        return "redirect:/buildings/laboratories";
    }

    @GetMapping("buildings/laboratories")
    public String getLaboratories(Model model) {
        List<Laboratory> laboratories = laboratoryService.getLaboratories();
        model.addAttribute("laboratoryList", laboratories);
        return "buildings/laboratory/laboratories";
    }

    @GetMapping("buildings/laboratory/add")
    public String getForm(Model model) {
        model.addAttribute("laboratoryRequest", new LaboratoryRequest());
        return "buildings/laboratory/addLaboratory";
    }

    @Transient
    @PostMapping(value = "buildings/laboratory/delete/{laboratoryId}")
    public String deleteLaboratory(@PathVariable(name = "laboratoryId") Integer laboratoryId) throws NotFoundException, IncorrectInputException {
        laboratoryService.deleteLaboratory(laboratoryId);
        return "redirect:/buildings/laboratories";
    }

    @GetMapping("buildings/getLaboratoryByProduct/{productId}")
    public String getLaboratoryByProduct (@PathVariable(name = "productId") Integer productId, Model model) {
        List<Laboratory> laboratories = laboratoryService.getLaboratoryByProduct(productId);
        model.addAttribute("laboratories", laboratories);
        return "sql_requests/laboratoryByProduct/laboratories";
    }

    @GetMapping("buildings/getProductByLaboratoryRequest")
    public String getProductByLaboratoryRequest (Model model) {
        model.addAttribute("dateRequest", new DateRequest());
        return "sql_requests/productByLaboratory/dateInputInAll";
    }

    @GetMapping("buildings/laboratory/getProductsInAllByDate")
    public String getProductsByLaboratory (@ModelAttribute("dateRequest") DateRequest dateRequest, Model model) {
        List<Laboratory> laboratories = laboratoryService.getLaboratories();
        model.addAttribute("firstDate", dateRequest.getFirstDate());
        model.addAttribute("secondDate", dateRequest.getSecondDate());
        model.addAttribute("laboratories", laboratories);
        return "sql_requests/productByLaboratory/productByLaboratory";
    }

    @GetMapping("buildings/getTestersByLaboratoryRequest")
    public String getTestersByLaboratoryRequest (Model model) {
        model.addAttribute("dateRequest", new DateRequest());
        return "sql_requests/productTestersByLaboratory/dateInputInAll";
    }

    @GetMapping("buildings/laboratory/getTestersProductsInAllByDate")
    public String getTestersProductsInAllByDate (@ModelAttribute("dateRequest") DateRequest dateRequest, Model model) {
        List<Laboratory> laboratories = laboratoryService.getLaboratories();
        List<Integer> laboratoriesIds = new ArrayList<>();
        for (Laboratory laboratory : laboratories) {
            laboratoriesIds.add(laboratory.getId());
        }
        model.addAttribute("firstDate", dateRequest.getFirstDate());
        model.addAttribute("secondDate", dateRequest.getSecondDate());
        model.addAttribute("laboratories", laboratories);
        model.addAttribute("laboratoriesIds", laboratoriesIds);
        return "sql_requests/productTestersByLaboratory/productByLaboratory";
    }
}
