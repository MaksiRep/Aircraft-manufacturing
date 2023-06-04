package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.TestingCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.TestingCycleRepository;

import java.rmi.NotBoundException;
import java.util.List;

@Service
public class TestingCycleService {

    @Autowired
    private TestingCycleRepository testingCycleRepository;

    @Autowired
    private EquipmentService equipmentService;

    public void addTestingCycle (TestingCycle testingCycle) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (testingCycle.getCycleId() == null || testingCycle.getOrdNum() == null || testingCycle.getEquipmentId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (testingCycleRepository.getByCycleIdAndOrdNum(testingCycle.getCycleId(), testingCycle.getOrdNum()) != null) {
            throw new AlreadyExistException("TestingCycle already exist");
        }

        if (!equipmentService.existByEquipmentId(testingCycle.getEquipmentId())) {
            throw new NotFoundException("Equipment with this id not found");
        }

        testingCycleRepository.save(testingCycle);
    }

    public TestingCycle getTestingCycle (Integer cycleId, Integer ordNum) throws NotFoundException {
        TestingCycle testingCycle = testingCycleRepository.getByCycleIdAndOrdNum(cycleId, ordNum);
        if (testingCycle == null) {
            throw new NotFoundException("Testing cycle not found");
        }
        return testingCycle;
    }

    public void updateTestingCycle (TestingCycle testingCycle, Integer cycleId, Integer ordNum) throws NotFoundException, IncorrectInputException {
        if (testingCycle.getEquipmentId() == null) {
            throw new IncorrectInputException("Equipment id is null");
        }
        if (testingCycleRepository.getByCycleIdAndOrdNum(cycleId, ordNum) == null) {
            throw new NotFoundException("Testing cycle with this id not found");
        }
        if (!equipmentService.existByEquipmentId(testingCycle.getEquipmentId())) {
            throw new NotFoundException("Equipment with this id not found");
        }

        testingCycle.setCycleId(cycleId);
        testingCycle.setOrdNum(ordNum);
        testingCycleRepository.save(testingCycle);
    }

    public void deleteTestingCycle (Integer cycleId, Integer ordNum) throws NotFoundException, IncorrectInputException {
        if (testingCycleRepository.getByCycleIdAndOrdNum(cycleId, ordNum) == null) {
            throw new NotFoundException("Testing cycle with this id not found");
        }

        if (testingCycleRepository.getProductTestingContains(cycleId, ordNum) != null) {
            throw new IncorrectInputException("Delete or update testing cycle from product testing");
        }

        testingCycleRepository.delete(testingCycleRepository.getByCycleIdAndOrdNum(cycleId, ordNum));
    }

    public List<Integer> getTestersId (Integer cycleId, Integer testStep) {
        return testingCycleRepository.getTesters(cycleId, testStep);
    }

    public Integer getTestingCycleId (Integer cycleId) {
        return testingCycleRepository.getTestingCycle(cycleId);
    }

    public List<TestingCycle> getProductTestingCycleByCycleId (Integer cycleId) {
        return testingCycleRepository.getProductTestingCycleByCycleId(cycleId);
    }

    public List<String> getEquipmentsNames (Integer cycleId) {
        return testingCycleRepository.getEquipmentsNames(cycleId);
    }
}
