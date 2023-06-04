package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product_creating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.DevelopmentRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Development;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating.DevelopmentRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.CertainProductService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.BrigadeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DevelopmentService {

    @Autowired
    private DevelopmentRepository developmentRepository;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private CertainProductService certainProductService;

    public void addDevelopment(DevelopmentRequest developmentRequest) throws IncorrectInputException, IncorrectDateException, ParseException, AlreadyExistException, OverflowException {
        Integer nextVal = developmentRepository.getNextDevelopmentId();
        if (nextVal > 100000) {
            throw new OverflowException("Development overflow, please delete something");
        }

        Development development = checkConstraint(developmentRequest, nextVal);

        if (developmentRepository.getBySerialNumberAndCycleIdAndDevStep(developmentRequest.getSerialNumber(),
                developmentRequest.getCycleId(), developmentRequest.getDevStep()) != null) {
            throw new AlreadyExistException("Development already exist");
        }

        developmentRepository.save(development);

    }

    public Development getDevelopment(Integer developmentId) throws NotFoundException {
        Development development = developmentRepository.getByDevelopmentId(developmentId);
        if (development == null) {
            throw new NotFoundException("Development not found");
        }
        return development;
    }

    public void updateDevelopment(DevelopmentRequest developmentRequest, Integer developmentId) throws IncorrectDateException, IncorrectInputException, ParseException, AlreadyExistException, NotFoundException {
        if (developmentRepository.getByDevelopmentId(developmentId) == null) {
            throw new NotFoundException("Development with this id not found");
        }

        Development development = checkConstraint(developmentRequest, developmentId);

        Development checkExist = developmentRepository.getBySerialNumberAndCycleIdAndDevStep(developmentRequest.getSerialNumber(),
                developmentRequest.getCycleId(), developmentRequest.getDevStep());
        if (checkExist != null && checkExist.getDevelopmentId() != developmentId) {
            throw new AlreadyExistException("Development already exist");
        }

        developmentRepository.save(development);
    }

    public void deleteDevelopment(Integer developmentId) throws NotFoundException {
        if (developmentRepository.getByDevelopmentId(developmentId) == null) {
            throw new NotFoundException("Development with this id not found");
        }

        developmentRepository.delete(developmentRepository.getByDevelopmentId(developmentId));
    }

    private Date formatDateFromString(String stringDate) throws IncorrectDateException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if (stringDate == null)
            throw new IncorrectDateException("Nullable date");
        return formatter.parse(stringDate);
    }


    public Date getEndDevelopmentDate(Integer serialNumber) {
        return developmentRepository.getEndDevelopmentDate(serialNumber);
    }

    private Development checkConstraint(DevelopmentRequest developmentRequest, Integer developmentId) throws IncorrectInputException, IncorrectDateException, ParseException, AlreadyExistException {

        if (developmentRequest.getSerialNumber() == null || developmentRequest.getSerialNumber() == 0 ||
                developmentRequest.getCycleId() == null || developmentRequest.getCycleId() == 0 ||
                developmentRequest.getDevStep() == null || developmentRequest.getDevStep() == 0 ||
                developmentRequest.getStartDevDate() == null || developmentRequest.getStartDevDate().length() == 0 ||
                developmentRequest.getBrigadeId() == null || developmentRequest.getBrigadeId() == 0) {
            throw new IncorrectInputException("Null values");
        }

        Date startDevDate;
        Date endDevDate = null;

        startDevDate = formatDateFromString(developmentRequest.getStartDevDate());
        if (startDevDate.after(new Date())) {
            throw new IncorrectInputException("Start date can't be future");
        }

        Date minDate = formatDateFromString("01.01.1958");

        if (startDevDate.before(minDate)) {
            throw new IncorrectInputException("Start date should be more than 01.01.1958");
        }
        if (developmentRequest.getEndDevDate() != null && developmentRequest.getEndDevDate().length() != 0) {
            if (developmentRequest.getStartDevDate().equals(developmentRequest.getEndDevDate())) {
                throw new IncorrectInputException("End date can't be like start date");
            }
            endDevDate = formatDateFromString(developmentRequest.getEndDevDate());
            if (endDevDate.before(startDevDate)) {
                throw new IncorrectInputException("End date should be after start date");
            }
        }


        Integer developmentCycle = certainProductService.getDevelopmentCycle(developmentRequest.getSerialNumber());

        if (developmentCycle == null || developmentRequest.getCycleId() != developmentCycle) {
            throw new IncorrectInputException("Incorrect using of product with this cycle");
        }

        List<Integer> brigades = brigadeService.getBrigadesByDevCycle(developmentRequest.getCycleId(), developmentRequest.getDevStep());

        if (brigades == null || !brigades.contains(developmentRequest.getBrigadeId())) {
            throw new IncorrectInputException("Incorrect brigade id, no founded brigades in section");
        }

        if (developmentRequest.getDevStep() == 1) {
            Date startDate = certainProductService.getStartDate(developmentRequest.getSerialNumber());
            if (startDate == null) {
                throw new IncorrectInputException("Certain product hasn't start date");
            }
            if (startDate.after(startDevDate) || startDate.before(startDevDate)) {
                throw new IncorrectInputException("Incorrect date, start development date should be like certain product start date");
            }
        }

        if (developmentRequest.getDevStep() != 1) {
            Date endPreviousDate = developmentRepository.getPreviousEndDate(
                    developmentRequest.getSerialNumber(),
                    developmentRequest.getDevStep(),
                    developmentRequest.getCycleId());
            if (endPreviousDate == null) {
                throw new IncorrectInputException("Product don't finished previous step");
            }
            if (endPreviousDate.before(startDevDate) || endPreviousDate.after(startDevDate)) {
                throw new IncorrectInputException("Incorrect start date, start date should be like end date in the previous step");
            }
        }

        return new Development(
                developmentId,
                developmentRequest.getSerialNumber(),
                developmentRequest.getCycleId(),
                developmentRequest.getDevStep(),
                startDevDate,
                endDevDate,
                developmentRequest.getBrigadeId());
    }

    public List<Development> getAllDevelopments() {
        return developmentRepository.getAllDevelopments();
    }

    public String getBrigadeName(Integer brigadeId) throws NotFoundException {
        return brigadeService.getBrigade(brigadeId).getName();
    }

    public List<Integer> getAllDevCycles() {
        return developmentRepository.getAllDevCycles();
    }

    public List<Integer> getAllBrigades() {
        return developmentRepository.getAllBrigades();
    }
}
