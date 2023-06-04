package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DateRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EquipmentRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Equipment;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.EquipmentService;

import java.beans.Transient;
import java.util.List;


@Controller
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping(value = "creating/equipment")
    public ResponseEntity<Void> addProductTesting(@RequestBody EquipmentRequest equipmentRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        equipmentService.addEquipment(equipmentRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "creating/equipment/{equipmentId}")
    public ResponseEntity<Void> updateEquipment(@RequestBody Equipment equipment,
                                                @PathVariable(name = "equipmentId") Integer equipmentId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        equipmentService.updateEquipment(equipment, equipmentId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "creating/equipment/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable(name = "equipmentId") Integer equipmentId) throws NotFoundException, IncorrectInputException {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "creating/equipment/{equipmentId}")
    public String getEquipment(@PathVariable(name = "equipmentId") Integer equipmentId, Model model) throws NotFoundException {
        Equipment equipment = equipmentService.getEquipment(equipmentId);
        model.addAttribute("id", equipment.getId());
        model.addAttribute("name", equipment.getName());
        model.addAttribute("laboratoryId", equipment.getLaboratoryId());
        model.addAttribute("laboratoryName", equipmentService.getLaboratoryName(equipmentId));
        return "productCreating/equipment/equipmentInfo";
    }

    @GetMapping(value = "creating/equipment/getAllEquipmentByDate")
    public String getAllEquipmentByDate(@ModelAttribute("dateRequest") DateRequest dateRequest, Model model) {
        List<Laboratory> laboratories = equipmentService.getAllLaboratory();
        model.addAttribute("firstDate", dateRequest.getFirstDate());
        model.addAttribute("secondDate", dateRequest.getSecondDate());
        model.addAttribute("laboratories", laboratories);
        return "sql_requests/equipmentByProducts/equipmentProductByLaboratory";
    }

    @GetMapping("product/getEquipmentByLaboratoryRequest")
    public String getEquipmentByLaboratoryRequest(Model model) {
        model.addAttribute("dateRequest", new DateRequest());
        return "sql_requests/equipmentByProducts/dateInputInAll";
    }

    @GetMapping("product/getEquipmentByLaboratoryIdAnDate/{laboratoryId}/{firstDate}/{secondDate}")
    public String getEquipmentByLaboratoryIdAnDate(@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                   @PathVariable(value = "firstDate") String firstDate,
                                                   @PathVariable(value = "secondDate") String secondDate,
                                                   Model model) throws NotFoundException {
        List<Product> products = equipmentService.getProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        List<ProductType> productTypes = equipmentService.getAllProductTypes();
        model.addAttribute("products", products);
        model.addAttribute("firstDate", firstDate);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("productTypes", productTypes);
        return "sql_requests/equipmentByProducts/equipmentProductsInAll";
    }

    @GetMapping("product/getEquipmentByLaboratoryIdAnDate/{laboratoryId}/{productTypeId}/{firstDate}/{secondDate}")
    public String getEquipmentByLaboratoryIdAnDateAndProductType(@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                                 @PathVariable(value = "productTypeId") Integer productTypeId,
                                                                 @PathVariable(value = "firstDate") String firstDate,
                                                                 @PathVariable(value = "secondDate") String secondDate,
                                                                 Model model) throws NotFoundException {
        List<Equipment> equipmentList = equipmentService.getProductsByLabIdAndDateAndProductType(laboratoryId, productTypeId, firstDate, secondDate);
        model.addAttribute("equipmentList", equipmentList);
        return "sql_requests/equipmentByProducts/equipmentProductType";
    }

    @GetMapping("product/getEquipmentByLaboratoryIdAnDateAndProductId/{laboratoryId}/{productId}/{firstDate}/{secondDate}")
    public String getEquipmentByLaboratoryIdAnDateAndProductId(@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                                 @PathVariable(value = "productId") Integer productTypeId,
                                                                 @PathVariable(value = "firstDate") String firstDate,
                                                                 @PathVariable(value = "secondDate") String secondDate,
                                                                 Model model) throws NotFoundException {
        List<Equipment> equipmentList = equipmentService.getEquipmentByLaboratoryIdAnDateAndProductId(laboratoryId, productTypeId, firstDate, secondDate);
        model.addAttribute("equipmentList", equipmentList);
        return "sql_requests/equipmentByProducts/equipmentProductType";
    }

    @GetMapping("product/getAllEquipmentByLaboratory/{laboratoryId}/{firstDate}/{secondDate}")
    public String getAllEquipmentByLaboratory(@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                               @PathVariable(value = "firstDate") String firstDate,
                                                               @PathVariable(value = "secondDate") String secondDate,
                                                               Model model) throws NotFoundException {
        List<Equipment> equipmentList = equipmentService.getAllProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        model.addAttribute("equipmentList", equipmentList);
        return "sql_requests/equipmentByProducts/equipmentProductType";
    }
}
