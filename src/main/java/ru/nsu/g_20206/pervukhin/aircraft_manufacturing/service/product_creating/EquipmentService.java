package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EquipmentRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Equipment;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.EquipmentRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.LaboratoryService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductTypeService;

import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;

    public void addEquipment(EquipmentRequest equipmentRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {

        if (equipmentRequest.getName() == null || equipmentRequest.getLaboratoryId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (equipmentRepository.getByName(equipmentRequest.getName()) != null) {
            throw new AlreadyExistException("Equipment already exist");
        }

        if (!laboratoryService.existByLaboratoryId(equipmentRequest.getLaboratoryId())) {
            throw new NotFoundException("Laboratory with this id not found");
        }

        if (equipmentRequest.getName().length() > 50) {
            throw new IncorrectInputException("To long name, should be lass than \"51\" symbols");
        }

        Integer nextVal = equipmentRepository.getNextEquipmentId();
        if (nextVal > 200) {
            throw new OverflowException("Equipment overflow, please delete something");
        }

        equipmentRepository.save(new Equipment(nextVal, equipmentRequest.getName(), equipmentRequest.getLaboratoryId()));
    }

    public Equipment getEquipment (Integer equipmentId) throws NotFoundException {
        Equipment equipment = equipmentRepository.getEquipmentById(equipmentId);
        if (equipment == null) {
            throw new NotFoundException("Equipment not found");
        }
        return equipment;
    }

    public void updateEquipment (Equipment equipment, Integer equipmentId) throws IncorrectInputException, AlreadyExistException, NotFoundException {

        if (equipment.getName() == null || equipment.getLaboratoryId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (equipmentRepository.getByName(equipment.getName()) != null &&
        equipmentRepository.getByName(equipment.getName()).getId() != equipmentId) {
            throw new AlreadyExistException("Equipment already exist");
        }

        if (!laboratoryService.existByLaboratoryId(equipment.getLaboratoryId())) {
            throw new NotFoundException("Laboratory with this id not found");
        }

        if (equipment.getName().length() > 50) {
            throw new IncorrectInputException("To long name, should be lass than \"51\" symbols");
        }

        equipment.setId(equipmentId);
        equipmentRepository.save(equipment);
    }

    public void deleteEquipment (Integer equipmentId) throws NotFoundException, IncorrectInputException {
        if (equipmentRepository.getEquipmentById(equipmentId) == null) {
            throw new NotFoundException("Equipment with this id not found");
        }

        if (equipmentRepository.getTestingCycleContains(equipmentId) != null) {
            throw new IncorrectInputException("Delete or update equipment from testing cycle");
        }

        equipmentRepository.delete(equipmentRepository.getEquipmentById(equipmentId));
    }

    public boolean existByEquipmentId (Integer equipmentId) {
        return equipmentRepository.getEquipmentById(equipmentId) != null;
    }

    public List<ProductType> getAllProductTypes () {
        return productTypeService.getAllProductTypes();
    }

    public List<Equipment> getAllEquipment () {
        return equipmentRepository.getAllEquipment();
    }

    public List<Laboratory> getAllLaboratory () {
        return laboratoryService.getLaboratories();
    }

    public List<Product> getProductsByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate) throws NotFoundException {
        Set<Integer> productsIds = equipmentRepository.getProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        ArrayList<Product> productList = new ArrayList<>();
        for(Integer equipmentId : productsIds) {
            productList.add(productService.getProduct(equipmentId));
        }
        return productList;
    }

    public List<Equipment> getProductsByLabIdAndDateAndProductType (Integer laboratoryId,Integer productType,String firstDate, String secondDate) throws NotFoundException {
        List<Integer> equipmentIds = equipmentRepository.getProductsByLabIdAndDateAndProductType(laboratoryId, productType, firstDate, secondDate);
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        for(Integer equipmentId : equipmentIds) {
            equipmentList.add(equipmentRepository.getEquipmentById(equipmentId));
        }
        return equipmentList;
    }

    public List<Equipment> getEquipmentByLaboratoryIdAnDateAndProductId (Integer laboratoryId,Integer productId,String firstDate, String secondDate) throws NotFoundException {
        List<Integer> equipmentIds = equipmentRepository.getEquipmentByLaboratoryIdAnDateAndProductId(laboratoryId, productId, firstDate, secondDate);
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        for(Integer equipmentId : equipmentIds) {
            equipmentList.add(equipmentRepository.getEquipmentById(equipmentId));
        }
        return equipmentList;
    }

    public List<Equipment> getAllProductsByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate) throws NotFoundException {
        List<Integer> equipmentIds = equipmentRepository.getAllProductsByLabIdAndDate(laboratoryId, firstDate, secondDate);
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        for(Integer equipmentId : equipmentIds) {
            equipmentList.add(equipmentRepository.getEquipmentById(equipmentId));
        }
        return equipmentList;
    }

    public String getLaboratoryName (Integer equipmentId) {
        return equipmentRepository.getLaboratoryName(equipmentId);
    }
}
