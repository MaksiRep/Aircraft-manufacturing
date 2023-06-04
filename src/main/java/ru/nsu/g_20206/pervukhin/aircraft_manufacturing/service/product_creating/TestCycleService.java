package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.TestCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.TestCycleRepository;

import java.util.List;

@Service
public class TestCycleService {

    @Autowired
    private TestCycleRepository testCycleRepository;

    public void addTestCycle () throws OverflowException {

        Integer nextVal = testCycleRepository.getNextTestCycleId();
        if (nextVal > 200) {
            throw new OverflowException("Test cycle overflow, please delete something");
        }

        testCycleRepository.save(new TestCycle(nextVal));
    }

    public void deleteTestCycle (Integer cycleId) throws NotFoundException, IncorrectInputException {
        if (testCycleRepository.getTestCycleByCycleId(cycleId) == null) {
            throw new NotFoundException("Test cycle with this id not found");
        }

        if (testCycleRepository.getTestingCycleContains(cycleId) != null) {
            throw new IncorrectInputException("Delete or update test cycle from testing cycle");
        }

        if (testCycleRepository.getProdInfoCycleContains(cycleId) != null) {
            throw new IncorrectInputException("Delete or update test cycle from product info");
        }

        testCycleRepository.delete(testCycleRepository.getTestCycleByCycleId(cycleId));
    }

    public List<Integer> getAllCycles () {
        return testCycleRepository.getAllTestCycles();
    }
}
