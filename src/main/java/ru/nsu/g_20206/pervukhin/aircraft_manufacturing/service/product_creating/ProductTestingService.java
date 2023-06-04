package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductTestingRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.CertainProduct;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevelopmentCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.ProductTesting;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.ProductTestingRepo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.CertainProductService;

import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProductTestingService {

    @Autowired
    private ProductTestingRepo productTestingRepo;

    @Autowired
    private DevelopmentService developmentService;

    @Autowired
    private CertainProductService certainProductService;

    @Autowired
    private TestingCycleService testingCycleService;

    public void addProductTesting(ProductTestingRequest productTestingRequest) throws IncorrectInputException, AlreadyExistException, IncorrectDateException, ParseException, OverflowException {


        Integer nextVal = productTestingRepo.getNextProductTestingId();
        if (nextVal > 100000) {
            throw new OverflowException("Development overflow, please delete something");
        }

        ProductTesting productTesting = checkConstraint(productTestingRequest, nextVal);

        if (productTestingRepo.getBySerialNumberAndCycleIdAndTestStep(productTestingRequest.getSerialNumber(),
                productTestingRequest.getCycleId(), productTestingRequest.getTestStep()) != null) {
            throw new AlreadyExistException("Product testing already exist");
        }

        productTestingRepo.save(productTesting);

    }

    public ProductTesting getProductTesting(Integer productTestingId) throws NotFoundException {
        ProductTesting productTesting = productTestingRepo.getProductTestingById(productTestingId);
        if (productTesting == null) {
            throw new NotFoundException("Product testing not found");
        }
        return productTesting;
    }

    public void updateProductTesting(ProductTestingRequest productTestingRequest, Integer productTestingId) throws NotFoundException, IncorrectDateException, IncorrectInputException, ParseException, AlreadyExistException {
        if (productTestingRepo.getProductTestingById(productTestingId) == null) {
            throw new NotFoundException("Product testing with this id not found");
        }

        ProductTesting certainProduct = checkConstraint(productTestingRequest, productTestingId);

        ProductTesting checkExist = productTestingRepo.getBySerialNumberAndCycleIdAndTestStep(productTestingRequest.getSerialNumber(),
                productTestingRequest.getCycleId(), productTestingRequest.getTestStep());
        if (checkExist != null && checkExist.getId() != productTestingId) {
            throw new AlreadyExistException("Product testing already exist");
        }

        productTestingRepo.save(certainProduct);
    }

    public void deleteProductTesting(Integer productTestingId) throws NotFoundException {
        if (productTestingRepo.getProductTestingById(productTestingId) == null) {
            throw new NotFoundException("Product testing with this id not found");
        }

        productTestingRepo.delete(productTestingRepo.getProductTestingById(productTestingId));
    }

    private Date formatDateFromString(String stringDate) throws IncorrectDateException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if (stringDate == null)
            throw new IncorrectDateException("Nullable date");
        return formatter.parse(stringDate);
    }

    private ProductTesting checkConstraint(ProductTestingRequest productTestingRequest, Integer productTestingId) throws IncorrectDateException, ParseException, IncorrectInputException, AlreadyExistException {

        if (productTestingRequest.getSerialNumber() == null || productTestingRequest.getSerialNumber() == 0 ||
                productTestingRequest.getCycleId() == null || productTestingRequest.getCycleId() == 0 ||
                productTestingRequest.getTestStep() == null || productTestingRequest.getTestStep() == 0 ||
                productTestingRequest.getStartTestDate() == null || productTestingRequest.getStartTestDate().length() == 0 ||
                productTestingRequest.getTesterId() == null || productTestingRequest.getTesterId() == 0) {
            throw new IncorrectInputException("Incorrect values");
        }

        Date startTestDate;
        Date endTestDate = null;

        startTestDate = formatDateFromString(productTestingRequest.getStartTestDate());

        Date minDate = formatDateFromString("01.01.1958");

        if (startTestDate.before(minDate)) {
            throw new IncorrectInputException("Start date should be more than 01.01.1958");
        }
        if (startTestDate.after(new Date())) {
            throw new IncorrectInputException("Start date can't be future");
        }
        if (productTestingRequest.getEndTestDate() != null && productTestingRequest.getEndTestDate().length() != 0) {
            if (productTestingRequest.getStartTestDate().equals(productTestingRequest.getEndTestDate())) {
                throw new IncorrectInputException("End date can't be like start date");
            }
            endTestDate = formatDateFromString(productTestingRequest.getEndTestDate());
            if (endTestDate.before(startTestDate)) {
                throw new IncorrectInputException("End date should be after start date");
            }
        }

        Integer correctTestingCycle = certainProductService.getTestingCycle(productTestingRequest.getSerialNumber());
        if (correctTestingCycle == null || correctTestingCycle != productTestingRequest.getCycleId())
            throw new IncorrectInputException("Incorrect using of product with this cycle");

        List<Integer> testerIds = testingCycleService.getTestersId(productTestingRequest.getCycleId(), productTestingRequest.getTestStep());
        if (!testerIds.contains(productTestingRequest.getTesterId())) {
            throw new IncorrectInputException("Incorrect tester id, no founded testers from laboratory");
        }

        Date startDate = formatDateFromString(productTestingRequest.getStartTestDate());
        if (productTestingRequest.getTestStep() != 1) {
            Date previousEndDate = productTestingRepo.getPreviousEndDate(
                    productTestingRequest.getSerialNumber(),
                    productTestingRequest.getTestStep(),
                    productTestingRequest.getCycleId());
            if (previousEndDate == null) {
                throw new IncorrectInputException("Product don't finished previous step");
            }
            if (startDate.after(previousEndDate) || startDate.before(previousEndDate)) {
                throw new IncorrectInputException("Incorrect start date value, start date should be like end date at the previous step");
            }
        }

        if (productTestingRequest.getTestStep() == 1) {
            Date endDevelopmentDate = developmentService.getEndDevelopmentDate(productTestingRequest.getSerialNumber());
            if (endDevelopmentDate == null) {
                throw new IncorrectInputException("Product isn't creating");
            }
            if (startDate.after(endDevelopmentDate) || startDate.before(endDevelopmentDate)) {
                throw new IncorrectInputException("Incorrect start date value, start date should be like end date at development");
            }
        }
        return new ProductTesting(
                productTestingId,
                productTestingRequest.getSerialNumber(),
                productTestingRequest.getCycleId(),
                productTestingRequest.getTestStep(),
                productTestingRequest.getTesterId(),
                startTestDate,
                endTestDate);
    }

    public List<ProductTesting> getAllProductTestings() {
        return productTestingRepo.getAllProductTestings();
    }

    public String getTesterName (Integer productTestingId) {
        return productTestingRepo.getTesterName(productTestingId).replace(',', ' ');
    }

    public List<Integer> getAllTestCycles () {
        return productTestingRepo.getAllTestCycles();
    }

    public List<Integer> getAllTesters () {
        return productTestingRepo.getAllTesters();
    }
}
