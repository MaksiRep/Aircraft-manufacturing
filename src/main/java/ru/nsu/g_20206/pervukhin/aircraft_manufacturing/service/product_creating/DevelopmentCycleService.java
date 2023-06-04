package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevelopmentCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.DevelopmentCycleRepo;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.SectionService;

import java.rmi.NotBoundException;
import java.util.List;

@Service
public class DevelopmentCycleService {

    @Autowired
    private DevelopmentCycleRepo developmentCycleRepo;

    @Autowired
    private SectionService sectionService;

    public void addDevelopmentCycle(DevelopmentCycle developmentCycle) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (developmentCycle.getCycleId() == null || developmentCycle.getOrdNum() == null || developmentCycle.getSectionId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (developmentCycleRepo.getByCycleIdAndOrdNum(developmentCycle.getCycleId(), developmentCycle.getOrdNum()) != null) {
            throw new AlreadyExistException("Development cycle already exist");
        }

        if (!sectionService.existBySectionId(developmentCycle.getSectionId())) {
            throw new NotFoundException("Section with this id not found");
        }

        developmentCycleRepo.save(developmentCycle);
    }

    public DevelopmentCycle getDevelopmentCycle(Integer cycleId, Integer ordNum) throws NotFoundException {
        DevelopmentCycle developmentCycle = developmentCycleRepo.getByCycleIdAndOrdNum(cycleId, ordNum);
        if ( developmentCycle == null) {
            throw new NotFoundException("Development cycle not found");
        }
        return developmentCycle;
    }

    public void updateDevelopmentCycle (DevelopmentCycle developmentCycle, Integer cycleId, Integer ordNum) throws NotFoundException, IncorrectInputException {
        if (developmentCycleRepo.getByCycleIdAndOrdNum(cycleId, ordNum) == null) {
            throw new NotFoundException("Development cycle with this ids not found");
        }

        if (developmentCycle.getSectionId() == null) {
            throw new IncorrectInputException("Section id is null");
        }

        if (!sectionService.existBySectionId(developmentCycle.getSectionId())) {
            throw new NotFoundException("Section with this id not found");
        }

        developmentCycle.setCycleId(cycleId);
        developmentCycle.setOrdNum(ordNum);

        developmentCycleRepo.save(developmentCycle);
    }

    public void deleteDevelopmentCycle (Integer cycleId, Integer ordNum) throws NotFoundException, IncorrectInputException {
        if (developmentCycleRepo.getByCycleIdAndOrdNum(cycleId, ordNum) == null) {
            throw new NotFoundException("Development cycle with this id not found");
        }

        if (developmentCycleRepo.getDevelopmentContains(cycleId, ordNum) != null) {
            throw new IncorrectInputException("Delete or update development cycle from development");
        }

        developmentCycleRepo.delete(developmentCycleRepo.getByCycleIdAndOrdNum(cycleId, ordNum));
    }

    public List<Integer> getDevelopmentCyclesByWorkshop(Integer workshopId) {
        return developmentCycleRepo.getCycleIdsByWorkshop(workshopId);
    }

    public Integer getDevelopmentCycle(Integer cycleId) {
        return developmentCycleRepo.getDevelopmentCycle(cycleId);
    }

    public List<DevelopmentCycle> getDevelopmentCycleByCycleId (Integer cycleId) {
        return developmentCycleRepo.getDevelopmentCycleByCycleId(cycleId);
    }

    public List<String> getSectionNames (Integer devCycleId) {
        return developmentCycleRepo.getSectionName(devCycleId);
    }
}
