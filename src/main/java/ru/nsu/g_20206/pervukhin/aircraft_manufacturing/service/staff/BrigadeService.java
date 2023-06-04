package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.BrigadeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Brigade;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.BrigadeRepository;

import java.util.List;

@Service
public class BrigadeService {

    @Autowired
    private BrigadeRepository brigadeRepository;

    @Autowired
    private WorkerStaffService workerStaffService;

    public void addBrigade(BrigadeRequest brigadeRequest) throws AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {

        Integer nextVal = brigadeRepository.getNextBrigadeId();
        if (nextVal > 1200) {
            throw new OverflowException("Brigade overflow, please delete something");
        }
        Brigade brigade = new Brigade(nextVal, brigadeRequest.getName(), brigadeRequest.getSectionId(), brigadeRequest.getBrigadierId());
        checkConstraint(brigade);
        if (brigadeRepository.getByName(brigadeRequest.getName()) != null) {
            throw new AlreadyExistException("Brigade with this name already exist");
        }
        if (brigadeRepository.getByBrigadierId(brigade.getBrigadierId()) != null) {
            throw new AlreadyExistException("Brigadier with this id already exist in other section");
        }

        brigadeRepository.save(brigade);
    }

    public Brigade getBrigade(Integer brigadeId) throws NotFoundException {
        Brigade brigade = brigadeRepository.getBrigadeByBrigadeId(brigadeId);
        if (brigade == null) {
            throw new NotFoundException("Brigade not found");
        }
        return brigade;
    }

    public void updateBrigade(Brigade brigade, Integer brigadeId) throws NotFoundException, IncorrectInputException, AlreadyExistException {
        if (brigadeRepository.getBrigadeByBrigadeId(brigadeId) == null) {
            throw new NotFoundException("Brigade with this id not found");
        }
        brigade.setBrigadeId(brigadeId);
        checkConstraint(brigade);

        if (brigadeRepository.getByName(brigade.getName()) != null &&
                brigadeRepository.getByName(brigade.getName()).getBrigadeId() != brigadeId) {
            throw new AlreadyExistException("Brigade with this name already exist");
        }
        if (brigadeRepository.getByBrigadierId(brigade.getBrigadierId()) != null &&
                brigadeRepository.getByBrigadierId(brigade.getBrigadierId()).getBrigadeId() != brigadeId) {
            throw new AlreadyExistException("Brigadier with this id already exist in other section");
        }

        brigadeRepository.save(brigade);
    }

    public void deleteBrigade (Integer brigadeId) throws NotFoundException, IncorrectInputException {
        if (brigadeRepository.getBrigadeByBrigadeId(brigadeId) == null) {
            throw new NotFoundException("Brigade with this id not found");
        }

        if (brigadeRepository.getWorkersContains(brigadeId) != null) {
            throw new IncorrectInputException("Update brigade from workers");
        }

        if (brigadeRepository.getDevelopmentContains(brigadeId) != null) {
            throw new IncorrectInputException("Delete or update brigade from development");
        }

        brigadeRepository.delete(brigadeRepository.getBrigadeByBrigadeId(brigadeId));
    }


    private void checkConstraint(Brigade brigade) throws IncorrectInputException, NotFoundException {
        if (brigade.getName() == null || brigade.getBrigadierId() == null || brigade.getSectionId() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (brigade.getName().length() > 35) {
            throw new IncorrectInputException("To long name, should be lass than \"36\" symbols");
        }
        if (!workerStaffService.existByWorkerId(brigade.getBrigadierId())) {
            throw new NotFoundException("Worker with this id not found");
        }

        if (workerStaffService.getBrigadeIdByWorkerId(brigade.getBrigadierId()) != null) {
            throw new IncorrectInputException("Worker already in other brigade");
        }
    }

    public List<Integer> getBrigadesByDevCycle(Integer devCycle, Integer devStep) {
        return brigadeRepository.getBrigadesByDevCycle(devStep, devCycle);
    }

    public List<Integer> getAllBrigadesIds () {
        return brigadeRepository.getAllBrigadesIds();
    }
}
