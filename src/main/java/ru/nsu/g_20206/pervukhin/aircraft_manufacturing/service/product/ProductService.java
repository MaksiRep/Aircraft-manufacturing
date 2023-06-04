package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.ProductRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.ProductRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.SectionService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.BrigadeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private SectionService sectionService;


    public void addProduct(ProductRequest productRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {


        Integer nextVal = productRepository.getNextProductId();
        if (nextVal > 2500) {
            throw new OverflowException("Product overflow, please delete something");
        }
        Product product = new Product(
                nextVal,
                productRequest.getName(),
                productRequest.getWeight(),
                productRequest.getMaxSpeed(),
                productRequest.getMaxHeight(),
                productRequest.getProductType());

        checkConstraint(product);

        if (productRepository.getByName(product.getName()) != null) {
            throw new AlreadyExistException("Product with this name already exist");
        }
        productRepository.save(product);
    }

    public Product getProduct(Integer productId) throws NotFoundException {
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return product;
    }

    public void updateProduct(Product product, Integer productId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (productRepository.getProductById(productId) == null) {
            throw new IncorrectInputException("Product with this id not found");
        }

        product.setId(productId);

        checkConstraint(product);

        if (productRepository.getByName(product.getName()) != null &&
                productRepository.getByName(product.getName()).getId() != productId) {
            throw new AlreadyExistException("Product with this name already exist");
        }

        productRepository.save(product);
    }

    public void deleteProduct(Integer productId) throws NotFoundException, IncorrectInputException {
        if (productRepository.getProductById(productId) == null) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productRepository.getProductInfoContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from product info");
        }

        if (productRepository.getCertainProductContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from certain product");
        }

        if (productRepository.getProductPlaneContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from plane");
        } else if (productRepository.getProductHelicopterContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from helicopter");
        } else if (productRepository.getProductRocketContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from rocket");
        } else if (productRepository.getProductGliderContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from glider");
        } else if (productRepository.getProductHangGliderContains(productId) != null) {
            throw new IncorrectInputException("Delete or update product from hang glider");
        }

        productRepository.delete(productRepository.getProductById(productId));
    }

    public List<Product> getAllProducts () {
        return productRepository.getAllProducts();
    }

    public List<ProductType> getProductsTypes () {
        return productTypeService.getAllProductTypes();
    }

    public List<Workshop> getWorkshops () {
        return workshopService.getWorkshops();
    }

    public List<Product> getProductsByType (Integer productType) throws NotFoundException {
        if (productTypeService.getProductType(productType) == null) {
            throw new NotFoundException("Product type not found");
        }
        return productRepository.getAllProductsByProdId(productType);
    }

    public List<Product> getProductByWorkshop (Integer workshopId) throws NotFoundException {
        if (workshopService.getWorkshop(workshopId) == null) {
            throw new NotFoundException("Workshop with this id notfound");
        }
        List<Integer> productIds = productRepository.getWorkshopIds(workshopId);
        ArrayList<Product> productList = new ArrayList<>();
        for (Integer productId : productIds) {
            productList.add(productRepository.getProductById(productId));
        }
        return productList;
    }

    public List<Product> getProductsByWorkshopAndProdType (Integer workshopId, Integer productTypeId) {
        return productRepository.getProductsByWorkshopAndProdType(workshopId, productTypeId);
    }

    private void checkConstraint(Product product) throws IncorrectInputException, NotFoundException {
        if (product.getName() == null || product.getName().length() == 0 ||
                product.getProductType() == null || product.getProductType() == 0 ||
                product.getWeight() == null ||  product.getWeight() == 0 ||
                product.getMaxHeight() == null || product.getMaxHeight() == 0 ||
                product.getMaxSpeed() == null || product.getMaxSpeed() == 0) {
            throw new IncorrectInputException("Null values");
        }

        if (product.getName().length() > 15) {
            throw new IncorrectInputException("To long name, should be lass than \"16\" symbols");
        }

        if (!productTypeService.existByTypeId(product.getProductType())) {
            throw new NotFoundException("Product type with this id not found");
        }

        if (product.getWeight() < 0) {
            throw new IncorrectInputException("Weight should be more than \"0\"");
        }

        if (product.getMaxSpeed() < 0) {
            throw new IncorrectInputException("Max speed should be more than \"0\"");
        }

        if (product.getMaxHeight() < 0) {
            throw new IncorrectInputException("Max height should be more than \"0\"");
        }

        if (product.getWeight() > 500000) {
            throw new IncorrectInputException("Weight can't be more than \"500000\" kilo");
        }

        if (product.getMaxSpeed() > 4000) {
            throw new IncorrectInputException("Max speed can't be more than \"4000\" m/s");
        }

        if (product.getMaxHeight() > 90000) {
            throw new IncorrectInputException("Max height can't be more than \"90000\" m");
        }

    }

    public boolean existByProductId(Integer productId) {
        return productRepository.getProductById(productId) != null;
    }

    public Integer getProductTypeById(Integer productId) {
        return productRepository.getProductTypeById(productId);
    }

    public List<Product> getAllProductsByDates (String firstDate, String secondDate) {
        return productRepository.getAllProductsByDates(firstDate, secondDate);
    }

    public List<Product> getAllProductsByDatesAndType (Integer productTypeId, String firstDate, String secondDate) {
        return productRepository.getAllProductsByDatesAndType(productTypeId, firstDate, secondDate);
    }

    public List<Product> getProductsByWorkshopIdAndDate (Integer workshopId, String firstDate, String secondDate) {
        return productRepository.getProductsByWorkshopByDates(firstDate, secondDate, workshopId);
    }

    public List<Product> getProductsBySectionIdAndDate (Integer sectionId, String firstDate, String secondDate) {
        return productRepository.getProductsBySectionByDates(sectionId, firstDate, secondDate);
    }

    public List<Section> getAllSections () {
        return sectionService.getAllSections();
    }

    public List<Section> getAllSectionsByWorkshop (Integer workshopId) {
        return sectionService.getSectionsByWorkshopId(workshopId);
    }

    public List<Product> getProductsByTypeAndDate (Integer typeId, String firstDate, String secondDate) {
        return productRepository.getProductsByTypeAndDate(typeId, firstDate, secondDate);
    }

    public List<Product> getProductsByTypeAndWorkshopAndDate (Integer typeId, Integer workshopId, String firstDate, String secondDate) {
        return productRepository.getProductsByTypeAndWorkshopAndDate(typeId, workshopId, firstDate, secondDate);
    }

    public List<Product> getProductsByTypeAndSectionAndDate (Integer typeId, Integer sectionId, String firstDate, String secondDate) {
        return productRepository.getProductsByTypeAndSectionAndDate(typeId, sectionId, firstDate, secondDate);
    }

    public List<Section> getProductCycle (Integer productId) {
        return sectionService.getProductCycle(productId);
    }

    public List<Product> getProductsByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate) {
        Set<Integer> productIds = productRepository.getProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);

        ArrayList<Product> products = new ArrayList<>();
        for (Integer productId : productIds) {
            products.add(productRepository.getProductById(productId));
        }
        return products;
    }

    public List<Product> getProductsByLabIdAndDateAndProductType (Integer laboratoryId, Integer productTypeId, String firstDate, String secondDate) {
        Set<Integer> productIds = productRepository.getProductsByLabIdAndDateAndProductType(laboratoryId, productTypeId, firstDate, secondDate);

        ArrayList<Product> products = new ArrayList<>();
        for (Integer productId : productIds) {
            products.add(productRepository.getProductById(productId));
        }
        return products;
    }

    public String getProductName (Integer productId) {
        return productRepository.getProductById(productId).getName();
    }

    public List<Integer> getProductsIds () {
        List<Product> productList = productRepository.getAllProducts();
        ArrayList<Integer> productListIds = new ArrayList<>();
        for (Product product : productList) {
            productListIds.add(product.getId());
        }
        return productListIds;
    }

    public String getProductTypeName (Integer productId) {
        return productRepository.getProductTypeName(productId);
    }
}
