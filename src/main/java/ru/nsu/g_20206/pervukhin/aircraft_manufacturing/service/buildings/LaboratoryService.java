package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.LaboratoryRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings.LaboratoryRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    private ProductService productService;

    public void addLaboratory(LaboratoryRequest laboratoryRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {

        Integer nextVal = laboratoryRepository.getNextLaboratoryId();
        if (nextVal > 50) {
            throw new OverflowException("Laboratory overflow, please delete something");
        }
        Laboratory laboratory = new Laboratory(nextVal, laboratoryRequest.getName(), laboratoryRequest.getAddress());
        checkConstraint(laboratory);

        if (laboratoryRepository.getByName(laboratory.getName()) != null)
            throw new AlreadyExistException("Laboratory with this name already exist");

        laboratoryRepository.save(laboratory);
    }

    public Laboratory getLaboratory(Integer laboratoryId) throws NotFoundException {
        Laboratory laboratory = laboratoryRepository.getLaboratoryById(laboratoryId);
        if (laboratory == null) {
            throw new NotFoundException("Laboratory not found");
        }
        return laboratory;
    }

    public void updateLaboratory(Laboratory laboratory, Integer laboratoryId) throws NotFoundException, IncorrectInputException, AlreadyExistException {

        if (laboratoryRepository.getLaboratoryById(laboratoryId) == null) {
            throw new NotFoundException("Laboratory not found");
        }

        checkConstraint(laboratory);

        if (laboratoryRepository.getByName(laboratory.getName()) != null &&
                laboratoryRepository.getByName(laboratory.getName()).getId() != laboratoryId)
            throw new AlreadyExistException("Laboratory with this name already exist");

        laboratory.setId(laboratoryId);
        laboratoryRepository.save(laboratory);

    }

    public void deleteLaboratory (Integer laboratoryId) throws NotFoundException, IncorrectInputException {
        if (laboratoryRepository.getLaboratoryById(laboratoryId) == null) {
            throw new NotFoundException("Laboratory with this id not found");
        }

        if (laboratoryRepository.getTesterLaboratoryContains(laboratoryId) != null) {
            throw new IncorrectInputException("Delete or update laboratory from testers");
        }

        if (laboratoryRepository.getEquipmentLaboratoryContains(laboratoryId) != null) {
            throw new IncorrectInputException("Delete or update laboratory from equipment");
        }

        if (laboratoryRepository.getWorkshopLaboratoryContains(laboratoryId) != null) {
            throw new IncorrectInputException("Delete or update laboratory from workshop - laboratory connection");
        }

        laboratoryRepository.delete(laboratoryRepository.getLaboratoryById(laboratoryId));
    }
    public List<Laboratory> getLaboratories () {
        return laboratoryRepository.getLaboratories();
    }

    public List<Laboratory> getLaboratoriesByProductIdDevelop (Integer productId) throws NotFoundException {
        if (productService.getProduct(productId) == null) {
            throw new NotFoundException("Product with this id not found");
        }

        List<Integer> laboratoryIds = laboratoryRepository.getLaboratoryByProductId(productId);
        ArrayList<Laboratory> staffList = new ArrayList<>();
        for (Integer laboratoryId : laboratoryIds) {
            staffList.add(laboratoryRepository.getLaboratoryById(laboratoryId));
        }
        return staffList;
    }

    private void checkConstraint(Laboratory laboratory) throws IncorrectInputException {

        if (laboratory.getName() == null || laboratory.getName().length() == 0 || laboratory.getAddress() == null || laboratory.getAddress().length() == 0) {
            throw new IncorrectInputException("Null values");
        }

        if (laboratory.getName().length() > 35) {
            throw new IncorrectInputException("To long name, should be less than \"36\" symbols");
        }

        if (laboratory.getAddress().length() > 35) {
            throw new IncorrectInputException("To long address, should be less than \"36\" symbols");
        }
    }

    public boolean existByLaboratoryId(Integer laboratoryId) {
        return laboratoryRepository.getLaboratoryById(laboratoryId) != null;
    }

    public List<Laboratory> getLaboratoryByProduct (Integer productId) {
        List<Integer> laboratoryIds = laboratoryRepository.getLaboratoryByProductId(productId);
        ArrayList<Laboratory> laboratories = new ArrayList<>();
        for (Integer labId : laboratoryIds) {
            laboratories.add(laboratoryRepository.getLaboratoryById(labId));
        }
        return laboratories;
    }
}
