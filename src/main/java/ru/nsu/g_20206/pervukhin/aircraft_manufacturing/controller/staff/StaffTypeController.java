package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductTypeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffTypeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.StaffType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.StaffTypeService;

import java.beans.Transient;
import java.util.List;


@Controller
public class StaffTypeController {

    @Autowired
    private StaffTypeService staffTypeService;

    @GetMapping(value = "staff/staff_types")
    public String getStaffTypes(Model model) {
        List<StaffType> staffTypes = staffTypeService.getAllStaffType();
        model.addAttribute("staffTypes", staffTypes);
        return "staff/staffType/staffTypesList";
    }

    @GetMapping(value = "staff/staff_type/{staffTypeId}")
    public String getStaffType(@PathVariable(name = "staffTypeId") Integer staffTypeId, Model model) throws NotFoundException {
        StaffType staffType = staffTypeService.getStaffType(staffTypeId);
        model.addAttribute("id", staffType.getTypeId());
        model.addAttribute("name", staffType.getName());
        return "staff/staffType/staffTypeInfo";
    }

    @Transient
    @PostMapping(value = "product/delete_staff_type/{staffTypeId}")
    public String deleteStaffType(@PathVariable(name = "staffTypeId") Integer staffTypeId) throws NotFoundException, IncorrectInputException {
        staffTypeService.deleteStaffType(staffTypeId);
        return "redirect:/staff/staff_types";
    }

    @PostMapping(value = "product/add_staff_type")
    public String addStaffType(@ModelAttribute(value = "staffTypeRequest") StaffTypeRequest staffTypeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {
        staffTypeService.addStaffType(staffTypeRequest);
        return "redirect:/staff/staff_types";
    }

    @GetMapping(value = "product/add_staff_type")
    public String getFormAddStaffType(Model model) {
        model.addAttribute("staffTypeRequest", new StaffTypeRequest());
        return "staff/staffType/addStaffType";
    }

    @GetMapping(value = "product/update_staff_type/{staffTypeId}")
    public String getFormUpdateStaffType(@PathVariable(value = "staffTypeId") Integer staffTypeId,
                                           Model model) throws NotFoundException {
        model.addAttribute("staffType", new StaffType());
        model.addAttribute("oldStaffType", staffTypeService.getStaffType(staffTypeId));
        model.addAttribute("staffTypeId", staffTypeId);
        return "staff/staffType/updateStaffType";
    }

    @PostMapping(value = "product/update_staff_type/{staffTypeId}")
    public String updateStaffType(@PathVariable(value = "staffTypeId") Integer staffTypeId,
                                    @ModelAttribute("staffType") StaffType staffType) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        staffTypeService.updateStaffType(staffType, staffTypeId);
        return "redirect:/staff/staff_types";
    }
}
