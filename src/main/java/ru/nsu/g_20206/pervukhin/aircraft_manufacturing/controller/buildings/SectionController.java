package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.SectionRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.SectionService;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping(value = "buildings/product_sections/{productId}")
    public ResponseEntity<List<Section>> getProductSections(@PathVariable(name = "productId") Integer productId) throws NotFoundException {
        return new ResponseEntity<>(sectionService.getProductSections(productId), HttpStatus.OK);
    }

//    @PutMapping(value = "buildings/section/{sectionId}")
//    public ResponseEntity<Void> updateSection(@RequestBody Section section, @PathVariable(name = "sectionId") Integer sectionId) throws NotFoundException, IncorrectInputException {
//        sectionService.updateSection(section, sectionId);
//        return ResponseEntity.ok().build();
//    }


    @GetMapping(value = "buildings/sections")
    public String getSections(Model model) {
        List<Section> sectionList = sectionService.getSections();
        ArrayList<Integer> sectionIds = new ArrayList<>();
        for (Section section : sectionList) {
            sectionIds.add(section.getId());
        }
        model.addAttribute("sectionList", sectionList);
        model.addAttribute("sectionIds", sectionIds);
        return "buildings/section/sections";
    }

    @GetMapping(value = "buildings/section/{sectionId}")
    public String getSection(Model model, @PathVariable(name = "sectionId") Integer sectionId) throws NotFoundException {
        Section section = sectionService.getSection(sectionId);
        model.addAttribute("id", section.getId());
        model.addAttribute("name", section.getName());
        model.addAttribute("workshopId", section.getWorkshopId());
        model.addAttribute("workshopName",sectionService.getWorkshopName(sectionId));
        model.addAttribute("managerId", section.getManagerId());
        model.addAttribute("managerName", sectionService.getSectionManagerName(sectionId));
        return "buildings/section/sectionInfo";
    }

    @Transient
    @PostMapping(value = "buildings/deleteSection/{sectionId}")
    public String deleteSection(@PathVariable(name = "sectionId") Integer sectionId) throws NotFoundException, IncorrectInputException {
        sectionService.deleteSection(sectionId);
        return "redirect:/buildings/sections";
    }

    @GetMapping(value = "buildings/addSection")
    public String getAddFromSection(Model model) {
        model.addAttribute("sectionRequest", new SectionRequest());
        model.addAttribute("freeManagers", sectionService.getFreeEngineers());
        return "buildings/section/addSection";
    }

    @PostMapping(value = "buildings/addSection")
    public String addSection(@ModelAttribute("sectionRequest") SectionRequest sectionRequest) throws IncorrectInputException, OverflowException, NotFoundException {
        sectionService.addSection(sectionRequest);
        return "redirect:/buildings/sections";
    }

    @GetMapping(value = "buildings/updateSection/{sectionId}")
    public String updateFormSection(Model model, @PathVariable(name = "sectionId") Integer sectionId) throws NotFoundException {
        ArrayList<Integer> freeEngineers = (ArrayList<Integer>) sectionService.getFreeEngineers();
        freeEngineers.add(sectionService.getSection(sectionId).getManagerId());
        model.addAttribute("freeManagers", freeEngineers);
        model.addAttribute("section", new Section());
        model.addAttribute("oldSection", sectionService.getSection(sectionId));
        model.addAttribute("id", sectionId);
        return "buildings/section/updateSection";
    }

    @PostMapping(value = "buildings/updateSection/{sectionId}")
    public String updateSection(@ModelAttribute("section") Section section, @PathVariable(name = "sectionId") Integer sectionId) throws NotFoundException, IncorrectInputException {
        sectionService.updateSection(section, sectionId);
        return "redirect:/buildings/sections";
    }

}
