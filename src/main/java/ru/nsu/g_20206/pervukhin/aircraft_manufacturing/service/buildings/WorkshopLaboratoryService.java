package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.WorkshopLaboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings.WorkshopLaboratoryRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkshopLaboratoryService {

    @Autowired
    private WorkshopLaboratoryRepo workshopLaboratoryRepo;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private LaboratoryService laboratoryService;

    public void addWorkshopLaboratory(WorkshopLaboratory workshopLaboratory) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        checkConstraint(workshopLaboratory);
        workshopLaboratoryRepo.save(workshopLaboratory);
    }


    public List<Laboratory> getWorkshopLaboratories (Integer workshopId) throws NotFoundException {

        if (!workshopLaboratoryRepo.existsByWorkshopId(workshopId)) {
            throw new NotFoundException("Workshop for laboratory not found");
        }

        List<Integer> laboratoriesIds = workshopLaboratoryRepo.getWorkshopLaboratories(workshopId);
        ArrayList<Laboratory> laboratories = new ArrayList<>();
        for (Integer laboratoryId : laboratoriesIds) {
            laboratories.add(laboratoryService.getLaboratory(laboratoryId));
        }
        return laboratories;
    }

    public List<Workshop> getLaboratoryWorkshops (Integer laboratoryId) throws NotFoundException {

        if (!workshopLaboratoryRepo.existsByLaboratoryId(laboratoryId)) {
            throw new NotFoundException("Laboratory for workshop not found");
        }
        List<Integer> workshopsIds =  workshopLaboratoryRepo.getLaboratoryWorkshops(laboratoryId);
        ArrayList<Workshop> workshops = new ArrayList<>();
        for (Integer workshopId : workshopsIds) {
            workshops.add(workshopService.getWorkshop(workshopId));
        }
        return workshops;
    }

    public void updateWorkshopLaboratory (WorkshopLaboratory workshopLaboratory, Integer workshopId, Integer laboratoryId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (workshopLaboratoryRepo.getByWorkshopIdAndLaboratoryId(workshopId, laboratoryId) == null) {
            throw new NotFoundException("Workshop - laboratory connection not found");
        }


        checkConstraint(workshopLaboratory);
        WorkshopLaboratory oldWorkshopLab = new WorkshopLaboratory();
        oldWorkshopLab.setLaboratoryId(laboratoryId);
        oldWorkshopLab.setWorkshopId(workshopId);
        workshopLaboratoryRepo.delete(oldWorkshopLab);
        workshopLaboratoryRepo.save(workshopLaboratory);
    }

    public void deleteWorkshopLaboratory (Integer workshopId, Integer laboratoryId) throws NotFoundException {
        if (workshopLaboratoryRepo.getByWorkshopIdAndLaboratoryId(workshopId, laboratoryId) == null) {
            throw new NotFoundException("Workshop - laboratory connection not found");
        }

        workshopLaboratoryRepo.delete(workshopLaboratoryRepo.getByWorkshopIdAndLaboratoryId(workshopId, laboratoryId));
    }

    private void checkConstraint (WorkshopLaboratory workshopLaboratory) throws IncorrectInputException, AlreadyExistException, NotFoundException {
        if (workshopLaboratory.getWorkshopId() == null || workshopLaboratory.getLaboratoryId() == null) {
            throw new IncorrectInputException("Null values");
        }
        if (workshopLaboratoryRepo
                .getByWorkshopIdAndLaboratoryId(workshopLaboratory.getWorkshopId(), workshopLaboratory.getLaboratoryId()) != null) {
            throw new AlreadyExistException("This connection already exist");
        }
        if (!workshopService.existByWorkshopId(workshopLaboratory.getWorkshopId())) {
            throw new NotFoundException("Workshop with this id not found");
        }
        if (!laboratoryService.existByLaboratoryId(workshopLaboratory.getLaboratoryId())) {
            throw new NotFoundException("Laboratory with this id not found");
        }
    }
}
