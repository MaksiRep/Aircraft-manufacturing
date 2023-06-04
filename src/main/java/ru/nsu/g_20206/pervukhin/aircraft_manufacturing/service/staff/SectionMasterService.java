package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.SectionMaster;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.SectionMasterRepo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.SectionService;

import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SectionMasterService {

    @Autowired
    private SectionMasterRepo sectionMasterRepo;

    @Autowired
    private EngineeringStaffService engineeringStaffService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private SectionService sectionService;

    public void addSectionMaster(SectionMaster sectionMaster) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (sectionMaster.getEngineerId() == null || sectionMaster.getSectionId() == null) {
            throw new IncorrectInputException("Null values");
        }

        checkConstraint(sectionMaster);

        if (sectionMasterRepo.getByEngineerId(sectionMaster.getEngineerId()) != null) {
            throw new AlreadyExistException("Section master already exist");
        }
        sectionMasterRepo.save(sectionMaster);
    }

    public List<Staff> getMasters(Integer sectionId) throws NotFoundException {

        List<Integer> mastersIds = sectionMasterRepo.getSectionMasters(sectionId);
        ArrayList<Staff> masters = new ArrayList<>();
        for (Integer masterId : mastersIds) {
            masters.add(staffService.getStaff(masterId));
        }
        return masters;
    }

    public Section getSection(Integer masterId) throws NotFoundException {
        if (!sectionMasterRepo.existsByEngineerId(masterId)) {
            throw new NotFoundException("Master for section not found");
        }
        Integer sectionId = sectionMasterRepo.getMastersSection(masterId);
        return sectionService.getSection(sectionId);
    }

    public void updateSectionMaster(SectionMaster sectionMaster, Integer sectionId, Integer masterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {

        if (sectionMasterRepo.getBySectionIdAndEngineerId(sectionId, masterId) == null) {
            throw new NotFoundException("Section - master connection not found");
        }
        if (sectionMaster.getEngineerId() == null || sectionMaster.getSectionId() == null) {
            throw new IncorrectInputException("Null values");
        }
        if (sectionMasterRepo.getByEngineerId(sectionMaster.getEngineerId()) != null &&
                sectionMasterRepo.getByEngineerId(sectionMaster.getEngineerId()).getSectionId() != sectionId) {
            throw new AlreadyExistException("Section master already exist");
        }
        checkConstraint(sectionMaster);

        SectionMaster oldSectionMaster = new SectionMaster();
        oldSectionMaster.setSectionId(sectionId);
        oldSectionMaster.setEngineerId(masterId);
        sectionMasterRepo.delete(oldSectionMaster);
        sectionMasterRepo.save(sectionMaster);
    }

    public void deleteSectionMaster (Integer sectionId, Integer masterId) throws NotFoundException {
        if (sectionMasterRepo.getBySectionIdAndEngineerId(sectionId, masterId) == null) {
            throw new NotFoundException("Master with this id not found");
        }

        sectionMasterRepo.delete(sectionMasterRepo.getBySectionIdAndEngineerId(sectionId, masterId));
    }

    private void checkConstraint(SectionMaster sectionMaster) throws NotFoundException, IncorrectInputException {

        if (!sectionService.existBySectionId(sectionMaster.getSectionId())) {
            throw new NotFoundException("Section with this id not found");
        }

        if (sectionService.getManagers().contains(sectionMaster.getEngineerId())) {
            throw new IncorrectInputException("Engineer already manager in section");
        }
    }
}
