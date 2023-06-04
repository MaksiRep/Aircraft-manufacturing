package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductInfo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.ProductInfoRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.DevelopmentCycleService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating.TestingCycleService;

import java.rmi.NotBoundException;
import java.util.List;

@Service
public class ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private DevelopmentCycleService developmentCycleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private TestingCycleService testingCycleService;

    public void addProductInfo(ProductInfo productInfo) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (productInfo.getProductId() == null) {
            throw new IncorrectInputException("Product id is null");
        }

        if (productInfoRepository.getByProductId(productInfo.getProductId()) != null) {
            throw new AlreadyExistException("Product info for this product already exist");
        }

        checkConstraint(productInfo);

        productInfoRepository.save(productInfo);
    }

    public ProductInfo getProductInfo(Integer productId) throws NotFoundException {
        ProductInfo productInfo = productInfoRepository.getByProductId(productId);
        if (productInfo == null) {
            throw new NotFoundException("Product info not found");
        }
        return productInfo;
    }

    public void updateProductInfo(ProductInfo productInfo, Integer productInfoId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (productInfoRepository.getByProductId(productInfoId) == null) {
            throw new NotFoundException("Product info with this id not found");
        }
        productInfo.setProductId(productInfoId);

        checkConstraint(productInfo);

        productInfoRepository.save(productInfo);
    }

    public void deleteProductInfo(Integer productInfoId) throws NotFoundException {
        if (productInfoRepository.getByProductId(productInfoId) == null) {
            throw new NotFoundException("Product info with this id not found");
        }

        productInfoRepository.delete(productInfoRepository.getByProductId(productInfoId));
    }

    private void checkConstraint(ProductInfo productInfo) throws IncorrectInputException, NotFoundException {

        if (productInfo.getDevCycleId() == null || productInfo.getTestingCycleId() == null || productInfo.getWorkshopId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (!productService.existByProductId(productInfo.getProductId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (developmentCycleService.getDevelopmentCycle(productInfo.getDevCycleId()) == null) {
            throw new NotFoundException("Development cycle with this id not found");
        }

        if (!workshopService.existByWorkshopId(productInfo.getWorkshopId())) {
            throw new NotFoundException("Workshop with this id not found");
        }

        if (testingCycleService.getTestingCycleId(productInfo.getTestingCycleId()) == null) {
            throw new NotFoundException("Testing cycle with this id not found");
        }

        List<Integer> developmentCycles = developmentCycleService.getDevelopmentCyclesByWorkshop(productInfo.getWorkshopId());
        if (!developmentCycles.contains(productInfo.getDevCycleId())) {
            throw new IncorrectInputException("Incorrect using of cycle id with this workshop");
        }
    }

    public List<ProductInfo> getAllProductInfo() {
        return productInfoRepository.getAllProductInfo();
    }

    public String getWorkshopName(Integer productInfoId) {
        return productInfoRepository.getWorkshopName(productInfoId);
    }

    public String getProductName(Integer productInfoId) {
        return productService.getProductName(productInfoId);
    }

    public List<Integer> getAllProducts() {
        return productService.getProductsIds();
    }

    public List<Integer> getAllWorkshops() {
        return productInfoRepository.getAllWorkshops();
    }

    public List<Integer> getAllTestCycles() {
        return productInfoRepository.getAllTestCycles();
    }

    public List<Integer> getAllDevCycles() {
        return productInfoRepository.getAllDevCycles();
    }
}
