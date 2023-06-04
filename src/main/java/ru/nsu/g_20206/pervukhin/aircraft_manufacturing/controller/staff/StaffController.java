package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DateRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.CertainProduct;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Equipment;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.StaffType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Tester;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.StaffService;

import java.beans.Transient;
import java.text.ParseException;
import java.util.List;

@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;


    @GetMapping(value = "staff/getStaffEngineers")
    public String getStaffEngineers(Model model) {
        List<Staff> staffList = staffService.getEngineers();
        model.addAttribute("staffList", staffList);
        return "staff/staff/staffEngineers";
    }

    @GetMapping(value = "staff/getStaffWorkers")
    public String getStaffWorkers(Model model) {
        List<Staff> staffList = staffService.getWorkers();
        model.addAttribute("staffList", staffList);
        return "staff/staff/staffWorkers";
    }

    @GetMapping(value = "staff/getStaffTesters")
    public String getStaffTesters(Model model) {
        List<Staff> staffList = staffService.getTesters();
        model.addAttribute("staffList", staffList);
        return "staff/staff/staffTesters";
    }

    @GetMapping(value = "staff/allStaff")
    public String getAllStaff(Model model) {
        List<Staff> staffList = staffService.getAllStaff();
        model.addAttribute("staffList", staffList);
        return "staff/staff/staff";
    }

    @GetMapping(value = "staff/staff/{staffId}")
    public String getStaff(Model model, @PathVariable(name = "staffId") Integer staffId) throws NotFoundException {
        Staff staff = staffService.getStaff(staffId);
        StaffType staffType = staffService.getStaffType(staff.getStaffType());
        model.addAttribute("id", staff.getId());
        model.addAttribute("name", staff.getName());
        model.addAttribute("surname", staff.getSurname());
        model.addAttribute("birthDate", staff.getBirthDate());
        model.addAttribute("education", staff.getEducation());
        model.addAttribute("employmentDate", staff.getEmploymentDate());
        model.addAttribute("dismissalDate", staff.getDismissalDate());
        model.addAttribute("salary", staff.getSalary());
        model.addAttribute("staffType", staffType.getTypeId());
        model.addAttribute("staffTypeName", staffType.getName());
        return "staff/staff/staffInfo";
    }

    @GetMapping(value = "staff/staff/add")
    public String addFormStaff(Model model) {
        model.addAttribute("staffRequest", new StaffRequest());
        return "staff/staff/addStaff";
    }

    @PostMapping(value = "staff/add_staff")
    public String addStaff(@ModelAttribute("staffRequest") StaffRequest staffRequest) throws IncorrectDateException, ParseException, AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        staffService.addStaff(staffRequest);
        return "redirect:/staff/allStaff";
    }

    @Transient
    @PostMapping(value = "staff/deleteStaff/{staffId}")
    public String deleteStaff(@PathVariable(name = "staffId") Integer staffId) throws NotFoundException, IncorrectInputException {
        staffService.deleteStaff(staffId);
        return "redirect:/staff/allStaff";
    }

    @GetMapping(value = "staff/getUpdateStaff/{staffId}")
    public String getStaffUpdate(@PathVariable(name = "staffId") Integer staffId, Model model) throws NotFoundException {
        model.addAttribute("staffRequest", new StaffRequest());
        model.addAttribute("oldStaffRequest", staffService.getStaff(staffId));
        model.addAttribute("id", staffId);
        return "staff/staff/updateStaff";
    }

    @PostMapping(value = "staff/updateStaff/{staffId}")
    public String updateStaff(@PathVariable(name = "staffId") Integer staffId,
                              @ModelAttribute("staffRequest") StaffRequest staffRequest) throws NotFoundException, IncorrectInputException, IncorrectDateException, ParseException, AlreadyExistException {
        staffService.updateStaff(staffRequest, staffId);
        return "redirect:/staff/allStaff";
    }

    @GetMapping(value = "staff/workshopEngineersMain")
    public String getWorkshopEngineers(Model model) {
        List<Staff> staffList = staffService.getAllStaff();
        List<Workshop> workshopList = staffService.getWorkshops();
        model.addAttribute("staffList", staffList);
        model.addAttribute("workshopList", workshopList);
        return "sql_requests/workshopStaff/workshopStaff";
    }

    @GetMapping(value = "staff/workshopEngineers/{workshopId}")
    public String getWorkshopEngineers(@PathVariable(name = "workshopId") Integer workshopId, Model model) throws NotFoundException {
        List<Staff> staffList = staffService.getWorkshopStaffByWorkshop(workshopId);
        List<Workshop> workshopList = staffService.getWorkshops();
        model.addAttribute("staffList", staffList);
        model.addAttribute("workshopList", workshopList);
        return "sql_requests/workshopStaff/staff";
    }

    @GetMapping(value = "staff/workshop_workers/{workshopId}")
    public String getWorkshopWorkers(@PathVariable(name = "workshopId") Integer workshopId, Model model) throws NotFoundException {
        List<Staff> staffList = staffService.getWorkshopWorkers(workshopId);
        model.addAttribute("staffList", staffList);
        return "sql_requests/brigadeWorkers/workshopWorkers";
    }

    @GetMapping(value = "staff/section_workers/{sectionId}")
    public String getSectionWorkers(@PathVariable(name = "sectionId") Integer sectionId, Model model) throws NotFoundException {
        List<Staff> staffList = staffService.getSectionWorkers(sectionId);
        model.addAttribute("staffList", staffList);
        return "sql_requests/brigadeWorkers/sectionWorkers";
    }

    @GetMapping(value = "staff/brigade_workers/{productId}")
    public String getBrigadeWorkersByProdId(@PathVariable(name = "productId") Integer productId, Model model) throws NotFoundException {
        List<Staff> staffList = staffService.getBrigadeWorkersByProdId(productId);
        model.addAttribute("staffList", staffList);
        return "sql_requests/brigadeWorkers/productBrigadeWorkers";
    }

    @GetMapping(value = "staff/getStaffByLaboratoryAndProductAndDateAnd/{laboratoryId}/{productId}/{firstDate}/{secondDate}")
    public String getStaffByLaboratoryAndProductAndDateAnd(@PathVariable(name = "laboratoryId") Integer laboratoryId,
                                                           @PathVariable(name = "productId") Integer productId,
                                                           @PathVariable(name = "firstDate") String firstDate,
                                                           @PathVariable(name = "secondDate") String secondDate,
                                                           Model model) {
        List<Staff> staffList = staffService.getStaffByLaboratoryAndProductAndDateAnd(laboratoryId, productId, firstDate, secondDate);
        model.addAttribute("staffList", staffList);
        return "sql_requests/productTestersByLaboratory/certaionProduct";
    }

    @GetMapping(value = "staff/getStaffByLaboratoryAndProductAndDateAndProdType/{laboratoryId}/{productType}/{firstDate}/{secondDate}")
    public String getStaffByLaboratoryAndProductAndDateAndProdType(@PathVariable(name = "laboratoryId") Integer laboratoryId,
                                                                   @PathVariable(name = "productType") Integer productType,
                                                                   @PathVariable(name = "firstDate") String firstDate,
                                                                   @PathVariable(name = "secondDate") String secondDate,
                                                                   Model model) {
        List<Staff> staffList = staffService.getStaffByLaboratoryAndProductAndDateAndProdType(laboratoryId, productType, firstDate, secondDate);
        model.addAttribute("staffList", staffList);
        return "sql_requests/productTestersByLaboratory/productType";
    }

    @GetMapping(value = "staff/getStaffByLaboratoryInAll/{laboratoryId}/{firstDate}/{secondDate}")
    public String getStaffByLaboratoryInAll(@PathVariable(name = "laboratoryId") Integer laboratoryId,
                                            @PathVariable(name = "firstDate") String firstDate,
                                            @PathVariable(name = "secondDate") String secondDate,
                                            Model model) {
        List<Staff> staffList = staffService.getStaffByLaboratoryInAll(laboratoryId, firstDate, secondDate);
        model.addAttribute("staffList", staffList);
        return "sql_requests/productTestersByLaboratory/productType";
    }

}
