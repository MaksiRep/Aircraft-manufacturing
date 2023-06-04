package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductInfo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductInfoService;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;

@Controller
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService;

    @PostMapping(value = "product/product_info")
    public String addLaboratory(@ModelAttribute("productInfo") ProductInfo productInfo) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        productInfoService.addProductInfo(productInfo);
        return "redirect:/product/allProductInfo";
    }

    @GetMapping(value = "product/product_info/{productId}")
    public String getProductInfo(Model model, @PathVariable(name = "productId") Integer productId) throws NotFoundException {
        ProductInfo productInfo = productInfoService.getProductInfo(productId);
        model.addAttribute("productId", productInfo.getProductId());
        model.addAttribute("devCycleId", productInfo.getDevCycleId());
        model.addAttribute("testCycleId", productInfo.getTestingCycleId());
        model.addAttribute("workshopId", productInfo.getWorkshopId());
        model.addAttribute("workshopName", productInfoService.getWorkshopName(productInfo.getProductId()));
        model.addAttribute("productName", productInfoService.getProductName(productInfo.getProductId()));
        return "product/productInfo/productInfo";
    }

    @GetMapping("product/allProductInfo")
    public String getAllProductInfo(Model model) {
        List<ProductInfo> productInfoList = productInfoService.getAllProductInfo();
        HashMap<Integer, String> workshopNames = new HashMap<>();
        HashMap<Integer, String> productNames = new HashMap<>();
        for (int i = 0; i < productInfoList.size(); i ++) {
            workshopNames.put(productInfoList.get(i).getProductId(), productInfoService.getWorkshopName(productInfoList.get(i).getProductId()));
            productNames.put(productInfoList.get(i).getProductId(), productInfoService.getProductName(productInfoList.get(i).getProductId()));
        }
        model.addAttribute("productInfoList", productInfoList);
        model.addAttribute("workshopNames", workshopNames);
        model.addAttribute("productNames", productNames);
        return "product/productInfo/productInfoList";
    }

    @GetMapping(value = "product/product_info/getUpdate/{productId}")
    public String getProductInfoUpdate(@PathVariable(name = "productId") Integer productId, Model model) throws NotFoundException {
        model.addAttribute("productInfo", new ProductInfo());
        model.addAttribute("oldProductInfo", productInfoService.getProductInfo(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("products", productInfoService.getAllProducts());
        model.addAttribute("devCycles", productInfoService.getAllDevCycles());
        model.addAttribute("testCycles", productInfoService.getAllTestCycles());
        model.addAttribute("workshops", productInfoService.getAllWorkshops());
        return "product/productInfo/updateProductInfo";
    }

    @PostMapping(value = "product/product_info/update/{productId}")
    public String updateProductInfo(@PathVariable(name = "productId") Integer productId,
                                   @ModelAttribute("productInfo") ProductInfo productInfo) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        productInfoService.updateProductInfo(productInfo, productId);
        return "redirect:/product/allProductInfo";
    }

    @GetMapping("product/product_info/add")
    public String getAddProductInfoForm(Model model) {
        model.addAttribute("productInfo", new ProductInfo());
        model.addAttribute("products", productInfoService.getAllProducts());
        model.addAttribute("devCycles", productInfoService.getAllDevCycles());
        model.addAttribute("testCycles", productInfoService.getAllTestCycles());
        model.addAttribute("workshops", productInfoService.getAllWorkshops());
        return "product/productInfo/addProductInfo";
    }

    @Transient
    @PostMapping(value = "product/product_info/delete/{productId}")
    public String deleteProductInfo(@PathVariable(name = "productId") Integer productId) throws NotFoundException {
        productInfoService.deleteProductInfo(productId);
        return "redirect:/product/allProductInfo";
    }
}
