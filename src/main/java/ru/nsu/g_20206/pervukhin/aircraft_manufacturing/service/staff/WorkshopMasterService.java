package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkshopMaster;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.WorkshopMasterRepo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopService;

import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkshopMasterService {

    @Autowired
    private WorkshopMasterRepo workshopMasterRepo;

    @Autowired
    private StaffService staffService;

    @Autowired
    private EngineeringStaffService engineeringStaffService;

    @Autowired
    private WorkshopService workshopService;

    public void addWorkshopMaster(WorkshopMaster workshopMaster) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (workshopMaster.getEngineerId() == null || workshopMaster.getWorkshopId() == null) {
            throw new IncorrectInputException("Null values");
        }
        checkConstraint(workshopMaster);
        if (workshopMasterRepo.getByEngineerId(workshopMaster.getEngineerId()) != null) {
            throw new AlreadyExistException("Workshop master already exist");
        }
        workshopMasterRepo.save(workshopMaster);
    }

    public List<Staff> getMasters(Integer workshopId) throws NotFoundException {

        List<Integer> mastersIds = workshopMasterRepo.getWorkshopMasters(workshopId);
        ArrayList<Staff> masters = new ArrayList<>();
        for (Integer masterId : mastersIds) {
            masters.add(staffService.getStaff(masterId));
        }
        return masters;
    }

    public Workshop getWorkshop(Integer masterId) throws NotFoundException {

        if (!workshopMasterRepo.existsByEngineerId(masterId)) {
            throw new NotFoundException("Master for workshop not found");
        }
        Integer workshopId = workshopMasterRepo.getMastersWorkshop(masterId);
        return workshopService.getWorkshop(workshopId);
    }

    public void updateWorkshopMaster (WorkshopMaster workshopMaster, Integer workshopId, Integer masterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if ( workshopMasterRepo.getByWorkshopIdAndEngineerId(workshopId, masterId) == null) {
            throw new NotFoundException("Workshop - master connection not found");
        }
        if (workshopMaster.getEngineerId() == null || workshopMaster.getWorkshopId() == null) {
            throw new IncorrectInputException("Null values");
        }
        if (workshopMasterRepo.getByEngineerId(workshopMaster.getEngineerId()) != null &&
        workshopMasterRepo.getByEngineerId(workshopMaster.getEngineerId()).getWorkshopId() != workshopId) {
            throw new AlreadyExistException("Workshop master already exist");
        }
        checkConstraint(workshopMaster);

        WorkshopMaster oldWorkshopMaster = new WorkshopMaster();
        oldWorkshopMaster.setWorkshopId(workshopId);
        oldWorkshopMaster.setEngineerId(masterId);
        workshopMasterRepo.delete(oldWorkshopMaster);
        workshopMasterRepo.save(workshopMaster);
    }

    public void deleteWorkshopMaster (Integer workshopId, Integer masterId) throws NotFoundException {
        if (workshopMasterRepo.getByWorkshopIdAndEngineerId(workshopId, masterId) == null) {
            throw new NotFoundException("Workshop - master connection with this id not found");
        }

        workshopMasterRepo.delete(workshopMasterRepo.getByWorkshopIdAndEngineerId(workshopId, masterId));
    }

    private void checkConstraint(WorkshopMaster workshopMaster) throws IncorrectInputException, NotFoundException {

        if (!engineeringStaffService.existByEngineeringId(workshopMaster.getEngineerId())) {
            throw new NotFoundException("Engineer with this id not found");
        }

        if (!workshopService.existByWorkshopId(workshopMaster.getWorkshopId())) {
            throw new NotFoundException("Workshop with this id not found");
        }

        if (workshopService.getManagers().contains(workshopMaster.getEngineerId())) {
            throw new IncorrectInputException("Engineer already manager in workshop");
        }
    }
}
