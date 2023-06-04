package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.WorkshopRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopService;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    @PostMapping(value = "buildings/workshop")
    public String addWorkshop (@ModelAttribute("workshopRequest") WorkshopRequest workshopRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {
        workshopService.addWorkshop(workshopRequest);
        return "redirect:/buildings/workshops";
    }

    @GetMapping("buildings/workshop/add")
    public String getAddForm(Model model) {
        model.addAttribute("workshopRequest", new WorkshopRequest());
        model.addAttribute("freeManagers", workshopService.getFreeEngineers());
        return "buildings/workshop/addWorkshop";
    }

    @GetMapping("buildings/workshops")
    public String getWorkshops (Model model) {
        List<Workshop> workshops = workshopService.getWorkshops();
        HashMap<Integer, String> workshopManagersNames = new HashMap<>();
        ArrayList<Integer> workshopIds = new ArrayList<>();
        for (Workshop workshop : workshops) {
            workshopManagersNames.put(workshop.getId(), workshopService.getWorkshopManagerName(workshop.getId()));
            workshopIds.add(workshop.getId());
        }
        model.addAttribute("workshopList", workshops);
        model.addAttribute("workshopIds", workshopIds);
        model.addAttribute("workshopManagersNames", workshopManagersNames);
        return "buildings/workshop/workshops";
    }

    @GetMapping("buildings/workshop/{workshopId}")
    public String getWorkshop (Model model, @PathVariable(name = "workshopId") Integer workshopId) throws NotFoundException {
        Workshop workshop = workshopService.getWorkshop(workshopId);
        model.addAttribute("id", workshop.getId());
        model.addAttribute("name", workshop.getName());
        model.addAttribute("address", workshop.getAddress());
        model.addAttribute("managerId", workshop.getManagerId());
        model.addAttribute("managerName", workshopService.getWorkshopManagerName(workshop.getId()));
        return "buildings/workshop/workshopInfo";
    }

    @Transient
    @PostMapping(value = "buildings/workshop/delete/{workshopId}")
    public String deleteWorkshop(@PathVariable(name = "workshopId") Integer workshopId) throws NotFoundException, IncorrectInputException {
        workshopService.deleteWorkshop(workshopId);
        return "redirect:/buildings/workshops";
    }

    @GetMapping(value = "buildings/workshop/getUpdate/{workshopId}")
    public String getWorkshopUpdate(@PathVariable(name = "workshopId") Integer workshopId, Model model) throws NotFoundException {
        ArrayList<Integer> freeEngineers = (ArrayList<Integer>) workshopService.getFreeEngineers();
        freeEngineers.add(workshopService.getWorkshop(workshopId).getManagerId());
        model.addAttribute("freeManagers", freeEngineers);
        model.addAttribute("workshop", new Workshop());
        model.addAttribute("oldWorkshop", workshopService.getWorkshop(workshopId));
        model.addAttribute("id", workshopId);
        return "buildings/workshop/updateWorkshop";
    }

    @PostMapping(value = "buildings/workshop/update/{workshopId}")
    public String updateWorkshop (@PathVariable(name = "workshopId") Integer workshopId,
                                  @ModelAttribute("workshop") Workshop workshop) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        workshopService.updateWorkshop(workshop, workshopId);
        return "redirect:/buildings/workshops";
    }

    @GetMapping(value = "buildings/sectionsByWorkshopMain")
    public String sectionsByWorkshopMain(Model model) {
        List<Section> sectionList = workshopService.getAllSections();
        List<Workshop> workshopList = workshopService.getWorkshops();
        Integer sectionCount = sectionList.size();
        model.addAttribute("sectionList", sectionList);
        model.addAttribute("workshopList", workshopList);
        model.addAttribute("sectionCount", sectionCount);
        return "sql_requests/sectionsManagersByWorkshopInAll/main";
    }

    @GetMapping(value = "buildings/sectionsByWorkshop/{workshopId}")
    public String getSectionsByWorkshop (@PathVariable(name = "workshopId") Integer workshopId, Model model) {
        List<Section> sectionList = workshopService.getSectionsByWorkshop(workshopId);
        HashMap<Integer, String> sectionManagers = new HashMap<>();
        for (Section section : sectionList) {
            sectionManagers.put(section.getId(), workshopService.getSectionManagerName(section.getId()));
        }
        Integer sectionCount = sectionList.size();
        model.addAttribute("sectionList", sectionList);
        model.addAttribute("sectionManagers", sectionManagers);
        model.addAttribute("sectionCount", sectionCount);
        return "sql_requests/sectionsManagersByWorkshopInAll/sections";
    }
}
