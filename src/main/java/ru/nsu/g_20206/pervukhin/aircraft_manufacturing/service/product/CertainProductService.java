package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.CertainProductRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.CertainProduct;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectDateException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.CertainProductRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertainProductService {

    @Autowired
    private CertainProductRepository certainProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    public void addCertainProduct(CertainProductRequest certainProductRequest) throws AlreadyExistException, IncorrectDateException, ParseException, IncorrectInputException, NotFoundException {

        if (certainProductRequest.getSerialNumber() == null || certainProductRequest.getSerialNumber() == 0) {
            throw new IncorrectInputException("Serial number is null");
        }
        if (certainProductRepository.getCertainProductBySerialNumber(certainProductRequest.getSerialNumber()) != null) {
            throw new AlreadyExistException("Certain product with this serial number already exist");
        }


        CertainProduct certainProduct = checkConstraint(certainProductRequest);

        certainProductRepository.save(certainProduct);
    }

    public CertainProduct getCertainProduct(Integer serialNumber) throws NotFoundException {
        CertainProduct certainProduct = certainProductRepository.getCertainProductBySerialNumber(serialNumber);
        if (certainProduct == null) {
            throw new NotFoundException("Certain product not found");
        }
        return certainProduct;
    }

    public void updateCertainProduct(CertainProductRequest certainProductRequest, Integer serialNumber) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException {

        if (certainProductRepository.getCertainProductBySerialNumber(serialNumber) == null) {
            throw new NotFoundException("Certain product not found");
        }

        certainProductRequest.setSerialNumber(serialNumber);
        CertainProduct certainProduct = checkConstraint(certainProductRequest);


        certainProduct.setSerialNumber(serialNumber);
        certainProductRepository.save(certainProduct);
    }

    public void deleteCertainProduct (Integer serialNumber) throws NotFoundException, IncorrectInputException {
        if (certainProductRepository.getCertainProductBySerialNumber(serialNumber) == null) {
            throw new NotFoundException("Certain product with this id not found");
        }

        if (certainProductRepository.getProductDevelopmentContains(serialNumber) != null) {
            throw new IncorrectInputException("Delete or update certain product from development");
        }

        if (certainProductRepository.getProductTestingContains(serialNumber) != null) {
            throw new IncorrectInputException("Delete or update certain product from product testing");
        }

        certainProductRepository.delete(certainProductRepository.getCertainProductBySerialNumber(serialNumber));
    }

    public List<CertainProduct> getAllCertainProducts () {
        return certainProductRepository.getAllCertainProducts();
    }

    public List<CertainProduct> getAllCertainProductsByType (Integer typeId) throws NotFoundException {
        if (productTypeService.getProductType(typeId) == null) {
            throw new NotFoundException("Product type not found");
        }
        List<Integer> productIds = certainProductRepository.getAllProductsByType(typeId);

        ArrayList<CertainProduct> certainProducts = new ArrayList<>();

        for (Integer productId : productIds) {
            certainProducts.addAll(certainProductRepository.getCertainProductByProductId(productId));
        }
        return certainProducts;
    }

    public List<CertainProduct> getCreatingProductByType(Integer productType) throws NotFoundException {
        if (productTypeService.getProductType(productType) == null) {
            throw new NotFoundException("Product type with this id not found");
        }

        List<Integer> certProdSerialNumbers = certainProductRepository.getCreatingProductsByType(productType);
        ArrayList<CertainProduct> certProducts = new ArrayList<>();

        for (Integer serialNumber : certProdSerialNumbers) {
            certProducts.add(certainProductRepository.getCertainProductBySerialNumber(serialNumber));
        }

        return certProducts;
    }

    public List<CertainProduct> getAllCreatingProducts () {
        List<Integer> certProdSerialNumbers = certainProductRepository.getCreatingProducts();
        ArrayList<CertainProduct> certProducts = new ArrayList<>();

        for (Integer serialNumber : certProdSerialNumbers) {
            certProducts.add(certainProductRepository.getCertainProductBySerialNumber(serialNumber));
        }

        return certProducts;
    }

    public List<ProductType> getProductTypes () {
        return productTypeService.getAllProductTypes();
    }

    private CertainProduct checkConstraint(CertainProductRequest certainProductRequest) throws IncorrectInputException, NotFoundException, IncorrectDateException, ParseException {

        if (certainProductRequest.getProductId() == null || certainProductRequest.getProductId() == 0) {
            throw new IncorrectInputException("Null product id");
        }

        if (!productService.existByProductId(certainProductRequest.getProductId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (certainProductRequest.getStartDate() == null || certainProductRequest.getStartDate().length() == 0) {
            throw new IncorrectInputException("Null start date");
        }

        Date startDate = formatDateFromString(certainProductRequest.getStartDate());

        Date minDate = formatDateFromString("01.01.1958");

        if (startDate.before(minDate)) {
            throw new IncorrectInputException("Start date should be more than 01.01.1958");
        }
        if (startDate.after(new Date())) {
            throw new IncorrectInputException("Start date can't be future");
        }

        Date endDate = null;

        if (certainProductRequest.getEndDate() != null && certainProductRequest.getEndDate().length() != 0) {

            endDate = formatDateFromString(certainProductRequest.getEndDate());

            if (endDate.before(startDate)) {
                throw new IncorrectInputException("End date should be after start date");
            }

            Date endTestDate = certainProductRepository.getEndTestDate(certainProductRequest.getSerialNumber());

            if (endTestDate == null) {
                throw new IncorrectInputException("Product testing hasn't been finished yet");
            }

            if (endTestDate.before(endDate) || endTestDate.after(endDate)) {
                throw new IncorrectInputException("Incorrect date, end date should be like end test date in the product testing last step");
            }

        }

        return new CertainProduct(
                certainProductRequest.getSerialNumber(),
                certainProductRequest.getProductId(),
                startDate,
                endDate);
    }

    private Date formatDateFromString(String stringDate) throws IncorrectDateException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if (stringDate == null)
            throw new IncorrectDateException("Nullable date");
        return formatter.parse(stringDate);
    }

    public Integer getTestingCycle(Integer serialId) {
        return certainProductRepository.getTestingCycle(serialId);
    }

    public Integer getDevelopmentCycle(Integer serialNumber) {
        return certainProductRepository.getDevelopmentCycle(serialNumber);
    }

    public Date getStartDate(Integer serialNumber) {
        return certainProductRepository.getStartDate(serialNumber);
    }

    public String getProductName (Integer productId) {
        return productService.getProductName(productId);
    }

    public List<Integer> getProductsIds () {
        return productService.getProductsIds();
    }
}
