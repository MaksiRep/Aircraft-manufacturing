package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DateRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductService;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "product/updateProduct/{productId}")
    public String updateProduct(@PathVariable(name = "productId") Integer productId,
                                @ModelAttribute("laboratory") Product product) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        productService.updateProduct(product, productId);
        return "redirect:/product/allProducts";
    }

    @GetMapping(value = "product/updateProduct/{productId}")
    public String getUpdateForm(@PathVariable(name = "productId") Integer productId, Model model) throws NotFoundException {
        model.addAttribute("product", new Product());
        model.addAttribute("oldProduct", productService.getProduct(productId));
        model.addAttribute("id", productId);
        return "product/product/updateProduct";
    }


    @GetMapping("product/addProduct")
    public String getAddForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        return "product/product/addProduct";
    }

    @PostMapping(value = "product/addProduct")
    public String addProduct(@ModelAttribute("product") ProductRequest productRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        productService.addProduct(productRequest);
        return "redirect:/product/allProducts";
    }

    @GetMapping(value = "product/product/{productId}")
    public String getProduct(@PathVariable(name = "productId") Integer productId, Model model) throws NotFoundException {
        Product product = productService.getProduct(productId);
        model.addAttribute("id", product.getId());
        model.addAttribute("name", product.getName());
        model.addAttribute("weight", product.getWeight());
        model.addAttribute("maxHeight", product.getMaxHeight());
        model.addAttribute("maxSpeed", product.getMaxSpeed());
        model.addAttribute("productType", product.getProductType());
        model.addAttribute("productTypeName", productService.getProductTypeName(productId));
        return "product/product/productInfo";
    }

    @Transient
    @PostMapping(value = "product/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable(name = "productId") Integer productId) throws NotFoundException, IncorrectInputException {
        productService.deleteProduct(productId);
        return "redirect:/product/allProducts";
    }

    @GetMapping(value = "product/allProducts")
    public String getAllProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<Integer> productsIds = productService.getProductsIds();
        model.addAttribute("productList", productList);
        model.addAttribute("productsIds", productsIds);
        return "product/product/productList";
    }

    @GetMapping(value = "product/productsByWorkshopOrInAll")
    public String getAllRequestProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<ProductType> productTypeList = productService.getProductsTypes();
        List<Workshop> workshopList = productService.getWorkshops();
        model.addAttribute("productList", productList);
        model.addAttribute("productTypeList", productTypeList);
        model.addAttribute("workshopList", workshopList);
        return "sql_requests/productsByWorkshopOrInAll/productsByWorkshopOrInAll";
    }

    @GetMapping(value = "product/productsByType/{typeId}")
    public String getProductsByType(@PathVariable(value = "typeId") Integer typeId, Model model) throws NotFoundException {
        List<Product> productList = productService.getProductsByType(typeId);
        List<Workshop> workshopList = productService.getWorkshops();
        model.addAttribute("productList", productList);
        model.addAttribute("workshopList", workshopList);
        model.addAttribute("productTypeId", typeId);
        return "sql_requests/productsByWorkshopOrInAll/productsByProductType";
    }

    @GetMapping(value = "product/productsByWorkshop/{workshopId}")
    public String getProductsByWorkshopId(@PathVariable(value = "workshopId") Integer workshopId, Model model) throws NotFoundException {
        List<Product> productList = productService.getProductByWorkshop(workshopId);
        List<ProductType> productTypeList = productService.getProductsTypes();
        model.addAttribute("productList", productList);
        model.addAttribute("productTypeList", productTypeList);
        model.addAttribute("workshopId", workshopId);
        return "sql_requests/productsByWorkshopOrInAll/productsByWorkshopId";
    }

    @GetMapping(value = "product/productsByWorkshopAndProdType/{workshopId}/{productTypeId}")
    public String getProductsByWorkshopAndProductType(@PathVariable(value = "workshopId") Integer workshopId,
                                                      @PathVariable(value = "productTypeId") Integer productTypeId,
                                                      Model model) {
        List<Product> productList = productService.getProductsByWorkshopAndProdType(workshopId, productTypeId);
        model.addAttribute("productList", productList);
        return "sql_requests/productsByWorkshopOrInAll/productsByWorkshopAndProdType";
    }

    @GetMapping("product/getDatesInAll")
    public String getDatesInAll(Model model) {
        model.addAttribute("dateRequest", new DateRequest());
        return "sql_requests/productsByWorkshopOrInAllByTime/dateInputInAll";
    }

    @GetMapping("product/getProductsInAllByDate")
    public String getProductsByDatesInAll(@ModelAttribute(value = "dateRequest") DateRequest dateRequest,
                                          Model model) {
        List<Product> productList = productService.getAllProductsByDates(dateRequest.getFirstDate(), dateRequest.getSecondDate());
        List<Workshop> workshopList = productService.getWorkshops();
        List<Section> sectionList = productService.getAllSections();
        List<ProductType> productTypeList = productService.getProductsTypes();
        model.addAttribute("firstDate", dateRequest.getFirstDate());
        model.addAttribute("secondDate", dateRequest.getSecondDate());
        model.addAttribute("productList", productList);
        model.addAttribute("workshopList", workshopList);
        model.addAttribute("sectionList", sectionList);
        model.addAttribute("productTypeList", productTypeList);
        return "sql_requests/productsByWorkshopOrInAllByTime/inAllByDates";
    }

    @GetMapping("product/allProductsByWorkshop/{workshopId}/{firstDate}/{secondDate}")
    public String getProductsByDatesAndWorkshop(@PathVariable(value = "workshopId") Integer workshopId,
                                                @PathVariable(value = "firstDate") String firstDate,
                                                @PathVariable(value = "secondDate") String secondDate,
                                                Model model) {
        List<Product> productList = productService.getProductsByWorkshopIdAndDate(workshopId, firstDate, secondDate);
        List<ProductType> productTypeList = productService.getProductsTypes();
        List<Section> sectionList = productService.getAllSectionsByWorkshop(workshopId);
        model.addAttribute("productList", productList);
        model.addAttribute("firstDate", firstDate);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("productTypeList", productTypeList);
        model.addAttribute("sectionList", sectionList);
        model.addAttribute("workshopId", workshopId);
        return "sql_requests/productsByWorkshopOrInAllByTime/workshops";
    }

    @GetMapping("product/allProductsBySection/{sectionId}/{firstDate}/{secondDate}")
    public String getProductsByDatesAndSection(@PathVariable(value = "sectionId") Integer sectionId,
                                               @PathVariable(value = "firstDate") String firstDate,
                                               @PathVariable(value = "secondDate") String secondDate,
                                               Model model) {
        List<Product> productList = productService.getProductsBySectionIdAndDate(sectionId, firstDate, secondDate);
        List<ProductType> productTypeList = productService.getProductsTypes();
        model.addAttribute("productList", productList);
        model.addAttribute("firstDate", firstDate);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("productTypeList", productTypeList);
        model.addAttribute("sectionId", sectionId);
        return "sql_requests/productsByWorkshopOrInAllByTime/sections";
    }

    @GetMapping("product/productsByTypeAndDate/{typeId}/{firstDate}/{secondDate}")
    public String productsByTypeAndDate(@PathVariable(value = "typeId") Integer typeId,
                                        @PathVariable(value = "firstDate") String firstDate,
                                        @PathVariable(value = "secondDate") String secondDate,
                                        Model model) {
        List<Product> productList = productService.getProductsByTypeAndDate(typeId, firstDate, secondDate);
        model.addAttribute("productList", productList);
        return "sql_requests/productsByWorkshopOrInAllByTime/allProductsType";
    }

    @GetMapping("product/productsByTypeAndWorkshopAndDate/{typeId}/{workshopId}/{firstDate}/{secondDate}")
    public String productsByTypeAndDate(@PathVariable(value = "typeId") Integer typeId,
                                        @PathVariable(value = "workshopId") Integer workshopId,
                                        @PathVariable(value = "firstDate") String firstDate,
                                        @PathVariable(value = "secondDate") String secondDate,
                                        Model model) {
        List<Product> productList = productService.getProductsByTypeAndWorkshopAndDate(typeId, workshopId, firstDate, secondDate);
        model.addAttribute("productList", productList);
        return "sql_requests/productsByWorkshopOrInAllByTime/allProductsType";
    }

    @GetMapping("product/productsByTypeAndSectionAndDate/{typeId}/{sectionId}/{firstDate}/{secondDate}")
    public String productsByTypeAndSectionAndDate(@PathVariable(value = "typeId") Integer typeId,
                                        @PathVariable(value = "sectionId") Integer sectionId,
                                        @PathVariable(value = "firstDate") String firstDate,
                                        @PathVariable(value = "secondDate") String secondDate,
                                        Model model) {
        List<Product> productList = productService.getProductsByTypeAndSectionAndDate(typeId, sectionId, firstDate, secondDate);
        model.addAttribute("productList", productList);
        return "sql_requests/productsByWorkshopOrInAllByTime/allProductsType";
    }

    @GetMapping("product/productCycle")
    public String getProductCycleAll (Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);
        return "sql_requests/productCycle/mainMenu";
    }

    @GetMapping("product/productCycle/{productId}")
    public String getProductCycle (@PathVariable(value = "productId") Integer productId, Model model) {
        List<Section> sectionList = productService.getProductCycle(productId);
        model.addAttribute("sectionList", sectionList);
        return "sql_requests/productCycle/cycles";
    }

    @GetMapping("product/getProductByLaboratoryIdAnDate/{laboratoryId}/{firstDate}/{secondDate}")
    public String getProductByLaboratoryIdAnDate (@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                  @PathVariable(value = "firstDate") String firstDate,
                                                  @PathVariable(value = "secondDate") String secondDate,
                                                  Model model) {
        List<Product> products = productService.getProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        List<ProductType> productTypes = productService.getProductsTypes();
        model.addAttribute("products", products);
        model.addAttribute("firstDate", firstDate);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("productTypes", productTypes);
        return "sql_requests/productByLaboratory/productByLaboratoryId";
    }

    @GetMapping("product/getProductByLaboratoryIdAnDateAndType/{laboratoryId}/{productTypeId}/{firstDate}/{secondDate}")
    public String getProductByLaboratoryIdAnDateAndType (@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                         @PathVariable(value = "productTypeId") Integer productTypeId,
                                                  @PathVariable(value = "firstDate") String firstDate,
                                                  @PathVariable(value = "secondDate") String secondDate,
                                                  Model model) {
        List<Product> products = productService.getProductsByLabIdAndDateAndProductType(laboratoryId, productTypeId, firstDate, secondDate);
        model.addAttribute("products", products);
        return "sql_requests/productByLaboratory/productsInAll";
    }

    @GetMapping("product/getProductByLaboratoryAnDate/{laboratoryId}/{firstDate}/{secondDate}")
    public String getProductByLaboratoryAnDate (@PathVariable(value = "laboratoryId") Integer laboratoryId,
                                                  @PathVariable(value = "firstDate") String firstDate,
                                                  @PathVariable(value = "secondDate") String secondDate,
                                                  Model model) {
        List<Product> products = productService.getProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        List<ProductType> productTypes = productService.getProductsTypes();
        model.addAttribute("products", products);
        model.addAttribute("laboratoryId", laboratoryId);
        model.addAttribute("firstDate", firstDate);
        model.addAttribute("secondDate", secondDate);
        model.addAttribute("productTypes", productTypes);
        return "sql_requests/productTestersByLaboratory/productsInAll";
    }

}
