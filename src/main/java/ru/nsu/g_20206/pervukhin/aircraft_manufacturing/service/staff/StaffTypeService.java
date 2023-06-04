package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffTypeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.StaffType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.StaffTypeRepository;

import java.rmi.NotBoundException;
import java.util.List;

@Service
public class StaffTypeService {

    @Autowired
    private StaffTypeRepository staffTypeRepository;

    public void addStaffType(StaffTypeRequest staffTypeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {

        if (staffTypeRequest.getName() == null) {
            throw new IncorrectInputException("Name is null");
        }

        if (staffTypeRequest.getName().length() > 20) {
            throw new IncorrectInputException("To long name, should be lass than \"21\" symbols");
        }

        if (staffTypeRepository.getByName(staffTypeRequest.getName()) != null)
            throw new AlreadyExistException("Type already exist");

        Integer nextVal = staffTypeRepository.getNextStaffTypeId();
        if (nextVal > 500) {
            throw new OverflowException("Staff type overflow, please delete something");
        }

        staffTypeRepository.save(new StaffType(nextVal, staffTypeRequest.getName()));
    }

    public StaffType getStaffType(Integer typeId) throws NotFoundException {
        StaffType staffType = staffTypeRepository.getStaffTypeByTypeId(typeId);
        if (staffType == null) {
            throw new NotFoundException("Staff type not found");
        }
        return staffType;
    }

    public void updateStaffType(StaffType staffType, Integer staffTypeId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (staffTypeRepository.getStaffTypeByTypeId(staffTypeId) == null) {
            throw new NotFoundException("Staff type with this id not found");
        }
        if (staffType.getName() == null) {
            throw new IncorrectInputException("Name is null");
        }
        if (staffType.getName().length() > 20) {
            throw new IncorrectInputException("To long name, should be lass than \"21\" symbols");
        }
        if (staffTypeRepository.getByName(staffType.getName()) != null &&
                staffTypeRepository.getByName(staffType.getName()).getTypeId() != staffTypeId)
            throw new AlreadyExistException("Type already exist");
        staffType.setTypeId(staffTypeId);
        staffTypeRepository.save(staffType);
    }

    public void deleteStaffType (Integer staffTypeId) throws NotFoundException, IncorrectInputException {
        if (staffTypeRepository.getStaffTypeByTypeId(staffTypeId) == null) {
            throw new NotFoundException("Staff type with this id nit found");
        }

        if (staffTypeRepository.getStaffContains(staffTypeId) != null) {
            throw new IncorrectInputException("Delete or update staff type from staff");
        }

        staffTypeRepository.delete(staffTypeRepository.getStaffTypeByTypeId(staffTypeId));
    }

    public List<StaffType> getAllStaffType () {
        return staffTypeRepository.getAllStaffType();
    }

    public boolean existByStaffTypeId(Integer staffTypeId) {
        return staffTypeRepository.existsStaffTypeByTypeId(staffTypeId) != null;
    }
}
