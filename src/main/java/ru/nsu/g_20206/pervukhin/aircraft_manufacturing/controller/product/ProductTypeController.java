package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductTypeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductTypeService;

import java.beans.Transient;
import java.util.List;

@Controller
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping(value = "product/product_types")
    public String getProductTypes(Model model) {
        List<ProductType> productTypeList = productTypeService.getAllProductTypes();
        model.addAttribute("productTypeList", productTypeList);
        return "product/productType/productTypeList";
    }

    @GetMapping(value = "product/product_type/{prodTypeId}")
    public String getProductType(@PathVariable(name = "prodTypeId") Integer prodTypeId, Model model) throws NotFoundException {
        ProductType productType = productTypeService.getProductType(prodTypeId);
        model.addAttribute("id", productType.getTypeId());
        model.addAttribute("name", productType.getName());
        return "product/productType/productTypeInfo";
    }

    @Transient
    @PostMapping(value = "product/delete_product_type/{prodTypeId}")
    public String deleteProductType(@PathVariable(name = "prodTypeId") Integer prodTypeId) throws NotFoundException, IncorrectInputException {
        productTypeService.deleteProductType(prodTypeId);
        return "redirect:/product/product_types";
    }

    @PostMapping(value = "product/add_product_type")
    public String addProductType(@ModelAttribute(value = "productTypeRequest") ProductTypeRequest productTypeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {
        productTypeService.addProductType(productTypeRequest);
        return "redirect:/product/product_types";
    }

    @GetMapping(value = "product/add_product_type")
    public String getFormAddProductType(Model model) {
        model.addAttribute("productTypeRequest", new ProductTypeRequest());
        return "product/productType/addProductType";
    }

    @GetMapping(value = "product/update_product_type/{productTypeId}")
    public String getFormUpdateProductType(@PathVariable(value = "productTypeId") Integer productTypeId,
                                           Model model) throws NotFoundException {
        model.addAttribute("productType", new ProductType());
        model.addAttribute("oldProductType", productTypeService.getProductType(productTypeId));
        model.addAttribute("productTypeId", productTypeId);
        return "product/productType/updateProductType";
    }

    @PostMapping(value = "product/update_product_type/{productTypeId}")
    public String updateProductType(@PathVariable(value = "productTypeId") Integer productTypeId,
                                    @ModelAttribute("productType") ProductType productType) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        productTypeService.updateProductType(productType, productTypeId);
        return "redirect:/product/product_types";
    }

}
