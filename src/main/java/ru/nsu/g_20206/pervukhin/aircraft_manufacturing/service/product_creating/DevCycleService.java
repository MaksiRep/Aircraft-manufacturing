package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevCycle;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.DevCycleRepository;

import java.util.List;

@Service
public class DevCycleService {

    @Autowired
    private DevCycleRepository devCycleRepository;

    public void addDevCycle () throws OverflowException {

        Integer nextVal = devCycleRepository.getNextDevCycleId();
        if (nextVal > 100) {
            throw new OverflowException("Dev cycle overflow, please delete something");
        }

        devCycleRepository.save(new DevCycle(nextVal));
    }

    public void deleteDevCycle (Integer devCycleId) throws NotFoundException, IncorrectInputException {
        if (devCycleRepository.getDevCycleByCycleId(devCycleId) == null) {
            throw new NotFoundException("Dev cycle with this id not found");
        }

        if (devCycleRepository.getProdInfoCycleContains(devCycleId) != null) {
            throw new IncorrectInputException("Delete or update dev cycle from product info");
        }

        if (devCycleRepository.getDevelopCycleContains(devCycleId) != null) {
            throw new IncorrectInputException("Delete or update dev cycle from development cycle");
        }

        devCycleRepository.delete(devCycleRepository.getDevCycleByCycleId(devCycleId));
    }

    public List<Integer> getAllCycles () {
        return devCycleRepository.getAllDevCycles();
    }
}
