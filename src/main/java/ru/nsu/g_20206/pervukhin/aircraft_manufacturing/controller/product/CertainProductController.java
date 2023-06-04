package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.CertainProductRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.CertainProduct;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectDateException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.CertainProductService;

import java.beans.Transient;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@Controller
public class CertainProductController {

    @Autowired
    private CertainProductService certainProductService;

    @GetMapping(value = "product/certain_creating_products/{productType}")
    public ResponseEntity<List<CertainProduct>> getCertainCreatingProduct(@PathVariable(name = "productType") Integer productType) throws NotFoundException {
        return new ResponseEntity<>(certainProductService.getCreatingProductByType(productType), HttpStatus.OK);
    }

    @GetMapping(value = "product/certainProducts")
    public String getCertainProducts(Model model) {
        List<CertainProduct> certainProductList = certainProductService.getAllCertainProducts();
        List<ProductType> productTypeList = certainProductService.getProductTypes();
        HashMap<Integer, String> certainProductNames = new HashMap<>();
        for (CertainProduct certainProduct : certainProductList) {
            certainProductNames.put(certainProduct.getProductId(), certainProductService.getProductName(certainProduct.getProductId()));
        }
        model.addAttribute("productTypeList", productTypeList);
        model.addAttribute("certainProductList", certainProductList);
        model.addAttribute("certainProductNames", certainProductNames);
        return "product/certainProduct/certainProducts";
    }

    @GetMapping(value = "product/certainProducts/{productType}")
    public String getCertainProductsByType (@PathVariable(name = "productType") Integer productType,
                                            Model model) throws NotFoundException {
        List<CertainProduct> certainProductList = certainProductService.getAllCertainProductsByType(productType);
        HashMap<Integer, String> certainProductNames = new HashMap<>();
        for (CertainProduct certainProduct : certainProductList) {
            certainProductNames.put(certainProduct.getProductId(), certainProductService.getProductName(certainProduct.getProductId()));
        }
        model.addAttribute("certainProductNames", certainProductNames);
        model.addAttribute("certainProductList", certainProductList);
        return "product/certainProduct/typeCertainProducts";
    }

    @GetMapping(value = "product/finishedCertainProducts")
    public String getFinishedCertainProducts(Model model) {
        List<CertainProduct> certainProductList = certainProductService.getAllCreatingProducts();
        HashMap<Integer, String> certainProductNames = new HashMap<>();
        for (CertainProduct certainProduct : certainProductList) {
            certainProductNames.put(certainProduct.getProductId(), certainProductService.getProductName(certainProduct.getProductId()));
        }
        model.addAttribute("certainProductList", certainProductList);
        model.addAttribute("certainProductNames", certainProductNames);
        return "product/certainProduct/finishedProducts";
    }

    @GetMapping(value = "product/certainProduct/{serialNumber}")
    public String getCertainProduct(@PathVariable(name = "serialNumber") Integer serialNumber, Model model) throws NotFoundException {
        CertainProduct certainProduct = certainProductService.getCertainProduct(serialNumber);
        model.addAttribute("serialNumber", certainProduct.getSerialNumber());
        model.addAttribute("productId", certainProduct.getProductId());
        model.addAttribute("startDate", certainProduct.getStartDate());
        model.addAttribute("endDate", certainProduct.getEndDate());
        model.addAttribute("productName", certainProductService.getProductName(certainProduct.getProductId()));
        return "product/certainProduct/certainProductInfo";
    }

    @Transient
    @PostMapping(value = "product/deleteCertainProduct/{serialNumber}")
    public String deleteCertainProduct(@PathVariable(name = "serialNumber") Integer serialNumber) throws NotFoundException, IncorrectInputException {
        certainProductService.deleteCertainProduct(serialNumber);
        return "redirect:/product/certainProducts";
    }

    @GetMapping(value = "product/addCertainProduct")
    public String getAddForm(Model model) {
        model.addAttribute("certainProductRequest", new CertainProductRequest());
        model.addAttribute("productsIds", certainProductService.getProductsIds());
        return "product/certainProduct/addCertainProduct";
    }

    @PostMapping(value = "product/addCertainProduct")
    public String addCertainProduct(@ModelAttribute("certainProductRequest") CertainProductRequest certainProductRequest) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException, AlreadyExistException {
        certainProductService.addCertainProduct(certainProductRequest);
        return "redirect:/product/certainProducts";
    }

    @PostMapping(value = "product/updateCertainProduct/{serialNumber}")
    public String updateCertainProduct (@PathVariable(name = "serialNumber") Integer serialNumber,
                                @ModelAttribute("certainProductRequest") CertainProductRequest certainProductRequest) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException {
        certainProductService.updateCertainProduct(certainProductRequest, serialNumber);
        return "redirect:/product/certainProducts";
    }

    @GetMapping(value = "product/updateCertainProduct/{serialNumber}")
    public String getUpdatedForm(@PathVariable(name = "serialNumber") Integer serialNumber, Model model) throws NotFoundException {
        model.addAttribute("certainProductRequest", new CertainProductRequest());
        model.addAttribute("oldCertainProductRequest", certainProductService.getCertainProduct(serialNumber));
        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("productsIds", certainProductService.getProductsIds());
        return "product/certainProduct/updateCertainProduct";
    }

}

