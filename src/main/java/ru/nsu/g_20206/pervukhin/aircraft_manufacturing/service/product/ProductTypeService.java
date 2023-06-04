package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductTypeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.ProductTypeRepository;

import java.util.List;

@Service
public class ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    public void addProductType(ProductTypeRequest productTypeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {

        if (productTypeRequest.getName() == null) {
            throw new IncorrectInputException("Name is null");
        }

        if (productTypeRequest.getName().length() > 15) {
            throw new IncorrectInputException("To long name, should be lass than \"16\" symbols");
        }

        if (productTypeRepository.getByName(productTypeRequest.getName()) != null) {
            throw new AlreadyExistException("Product type with this name already exist");
        }

        Integer nextVal = productTypeRepository.getNextProductTypeId();
        if (nextVal > 500) {
            throw new OverflowException("Product type overflow, please delete something");
        }

        productTypeRepository.save(new ProductType(nextVal, productTypeRequest.getName()));
    }

    public ProductType getProductType(Integer typeId) throws NotFoundException {
        ProductType productType = productTypeRepository.getProductTypeByTypeId(typeId);
        if (productType == null) {
            throw new NotFoundException("Product type not found");
        }
        return productType;
    }

    public void updateProductType(ProductType productType, Integer productTypeId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (productTypeRepository.getProductTypeByTypeId(productTypeId) == null) {
            throw new NotFoundException("Product type with this id not found");
        }

        if (productType.getName() == null) {
            throw new IncorrectInputException("Name is null");
        }

        if (productType.getName().length() > 15) {
            throw new IncorrectInputException("To long name, should be lass than \"16\" symbols");
        }

        if (productTypeRepository.getByName(productType.getName()) != null &&
                productTypeRepository.getByName(productType.getName()).getTypeId() != productTypeId) {
            throw new AlreadyExistException("Product type with this name already exist");
        }

        productType.setTypeId(productTypeId);
        productTypeRepository.save(productType);
    }

    public void deleteProductType (Integer productTypeId) throws NotFoundException, IncorrectInputException {
        if (productTypeRepository.getProductTypeByTypeId(productTypeId) == null) {
            throw new NotFoundException("Product type with this id not found");
        }

        if (productTypeRepository.getProductTypeContains(productTypeId) != null) {
            throw new IncorrectInputException("Delete or update product type from product");
        }

        productTypeRepository.delete(productTypeRepository.getProductTypeByTypeId(productTypeId));
    }

    public List<ProductType> getAllProductTypes () {
        return productTypeRepository.getAllTypes();
    }

    public boolean existByTypeId(Integer typeId) {
        return productTypeRepository.getProductTypeByTypeId(typeId) != null;
    }
}
