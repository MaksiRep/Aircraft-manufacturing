package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.WorkshopRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings.WorkshopRepository;

import java.util.List;

@Service
public class WorkshopService {

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private SectionService sectionService;

    public void addWorkshop(WorkshopRequest workshopRequest) throws AlreadyExistException, IncorrectInputException, OverflowException {

        Integer nextVal = workshopRepository.getNextWorkshopId();
        if (nextVal > 50) {
            throw new OverflowException("Workshop overflow, please delete something");
        }

        Workshop workshop = new Workshop(nextVal,
                workshopRequest.getName(),
                workshopRequest.getAddress(),
                workshopRequest.getManagerId());

        checkConstraint(workshop);

        if (workshopRepository.getByName(workshop.getName()) != null)
            throw new AlreadyExistException("Workshop with this name already exist");

        if (workshop.getManagerId() != null) {
            if (workshopRepository.getByManagerId(workshop.getManagerId()) != null) {
                throw new IncorrectInputException("Manager with this id already exist in other workshop");
            }
            if (workshopRepository.getMasterById(workshop.getManagerId()) != null) {
                throw new IncorrectInputException("Engineer already master in workshop");
            }
        }

        workshopRepository.save(workshop);
    }

    public Workshop getWorkshop(Integer workshopId) throws NotFoundException {

        Workshop workshop = workshopRepository.getWorkshopById(workshopId);
        if (workshop == null) {
            throw new NotFoundException("Workshop not found");
        }
        return workshop;
    }

    public void updateWorkshop(Workshop workshop, Integer workshopId) throws IncorrectInputException, AlreadyExistException, NotFoundException {

        if (workshopRepository.getWorkshopById(workshopId) == null) {
            throw new NotFoundException("Workshop not found");
        }
        checkConstraint(workshop);

        if (workshopRepository.getByName(workshop.getName()) != null &&
                workshopRepository.getByName(workshop.getName()).getId() != workshopId)
            throw new AlreadyExistException("Workshop with this name already exist");

        if (workshop.getManagerId() != null) {
            if (workshopRepository.getByManagerId(workshop.getManagerId()) != null &&
                    workshopRepository.getByManagerId(workshop.getManagerId()).getId() != workshopId) {
                throw new IncorrectInputException("Manager with this id already exist in other workshop");
            }
            if (workshopRepository.getMasterById(workshop.getManagerId()) != null) {
                throw new IncorrectInputException("Engineer already master in workshop");
            }
        }

        workshop.setId(workshopId);
        workshopRepository.save(workshop);
    }

    public void deleteWorkshop (Integer workshopId) throws NotFoundException, IncorrectInputException {
        if (workshopRepository.getWorkshopById(workshopId) == null) {
            throw new NotFoundException("Workshop with this id not found");
        }

        if (workshopRepository.getWorkshopMasterContains(workshopId) != null) {
            throw new IncorrectInputException("Delete or update workshop from workshop - master connection");
        }

        if (workshopRepository.getWorkshopSectionContains(workshopId) != null) {
            throw new IncorrectInputException("Delete or update workshop from section");
        }

        if (workshopRepository.getWorkshopLaboratoryContains(workshopId) != null) {
            throw new IncorrectInputException("Delete or update workshop from workshop - laboratory connection");
        }

        if (workshopRepository.getWorkshopProductInfoContains(workshopId) != null) {
            throw new IncorrectInputException("Delete or update workshop from product info");
        }

        workshopRepository.delete(workshopRepository.getWorkshopById(workshopId));
    }

    public List<Workshop> getWorkshops() {
        return workshopRepository.getWorkshops();
    }


    private void checkConstraint(Workshop workshop) throws IncorrectInputException {

        if (workshop.getName() == null || workshop.getName().length() == 0 || workshop.getAddress() == null || workshop.getAddress().length() == 0) {
            throw new IncorrectInputException("Null values");
        }

        if (workshop.getName().length() > 30) {
            throw new IncorrectInputException("To long name, should be lass than \"31\" symbols");
        }
        if (workshop.getAddress().length() > 35) {
            throw new IncorrectInputException("to long address, should be lass than \"36\" symbols");
        }
    }

    public boolean existByWorkshopId(Integer workshopId) {
        return workshopRepository.getWorkshopById(workshopId) != null;
    }

    public List<Integer> getManagers() {
        return workshopRepository.getManagers();
    }

    public List<Section> getAllSections () {
        return sectionService.getAllSections();
    }

    public List<Section> getSectionsByWorkshop (Integer workshopId) {
        return sectionService.getSectionsByWorkshopId(workshopId);
    }

    public String getWorkshopManagerName (Integer workshopId) {
        return workshopRepository.getWorkshopManagerName(workshopId).replace(',', ' ');
    }

    public List<Integer> getFreeEngineers () {
        return workshopRepository.getFreeEngineers();
    }

    public String getSectionManagerName (Integer sectionId) {
        return sectionService.getSectionManagerName(sectionId);
    }

}
