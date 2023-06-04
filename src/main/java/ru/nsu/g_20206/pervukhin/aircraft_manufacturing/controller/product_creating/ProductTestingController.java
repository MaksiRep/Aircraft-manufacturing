package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductTestingRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.ProductTesting;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.ProductTestingService;

import java.beans.Transient;
import java.text.ParseException;
import java.util.List;

@Controller
public class ProductTestingController {

    @Autowired
    private ProductTestingService productTestingService;

    @GetMapping(value = "creating/productTesting/{productId}")
    public String getProductTesting(@PathVariable(name = "productId") Integer productId, Model model) throws NotFoundException {
        ProductTesting productTesting = productTestingService.getProductTesting(productId);
        model.addAttribute("id",productTesting.getId());
        model.addAttribute("serialNumber",productTesting.getSerialNumber());
        model.addAttribute("cycleId",productTesting.getCycleId());
        model.addAttribute("testStep",productTesting.getTestStep());
        model.addAttribute("startTestDate",productTesting.getStartTestDate());
        model.addAttribute("endTestDate",productTesting.getEndTestDate());
        model.addAttribute("testerId",productTesting.getTesterId());
        model.addAttribute("testerName",productTestingService.getTesterName(productTesting.getId()));
        return "productCreating/productTesting/productTestingInfo";
    }

    @GetMapping(value = "creating/productTestings")
    public String getProductTestings(Model model) {
        List<ProductTesting> productTestings = productTestingService.getAllProductTestings();
        model.addAttribute("productTestings", productTestings);
        return "productCreating/productTesting/productTestingList";
    }

    @Transient
    @PostMapping(value = "creating/product_testing/{testingId}")
    public String deleteProductTesting(@PathVariable(name = "testingId") Integer testingId) throws NotFoundException {
        productTestingService.deleteProductTesting(testingId);
        return "redirect:/creating/productTestings";
    }

    @GetMapping(value = "creating/getAddFormProductTesting")
    public String getAddFormProductTesting(Model model) {
        model.addAttribute("productTestingRequest", new ProductTestingRequest());
        model.addAttribute("testCycles", productTestingService.getAllTestCycles());
        model.addAttribute("testerIds", productTestingService.getAllTesters());
        return "productCreating/productTesting/addProductTesting";
    }

    @PostMapping(value = "creating/addProductTesting")
    public String addDevelopment(@ModelAttribute("productTestingRequest") ProductTestingRequest productTestingRequest) throws IncorrectDateException, IncorrectInputException, ParseException, AlreadyExistException, OverflowException {
        productTestingService.addProductTesting(productTestingRequest);
        return "redirect:/creating/productTestings";
    }

    @GetMapping(value = "creating/getUpdateFromProductTesting/{productTestingId}")
    public String getUpdateFromProductTesting(@PathVariable(name = "productTestingId") Integer productTestingId, Model model) throws NotFoundException {
        ProductTesting oldProductTesting = productTestingService.getProductTesting(productTestingId);
        model.addAttribute("productTestingId", productTestingId);
        model.addAttribute("oldProductTesting", oldProductTesting);
        model.addAttribute("productTestingRequest", new ProductTestingRequest());
        model.addAttribute("testCycles", productTestingService.getAllTestCycles());
        model.addAttribute("testerIds", productTestingService.getAllTesters());
        return "productCreating/productTesting/updateProductTesting";
    }

    @PostMapping(value = "creating/updateProductTesting/{productTestingId}")
    public String updateDevelopment(@ModelAttribute("productTestingRequest") ProductTestingRequest productTestingRequest,
                                    @PathVariable(name = "productTestingId") Integer productTestingId) throws NotFoundException, IncorrectInputException, IncorrectDateException, ParseException, AlreadyExistException {
        productTestingService.updateProductTesting(productTestingRequest, productTestingId);
        return "redirect:/creating/productTestings";
    }
}
