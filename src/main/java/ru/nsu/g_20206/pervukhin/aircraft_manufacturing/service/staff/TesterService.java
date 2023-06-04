package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.TesterRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Tester;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.TesterRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.LaboratoryService;

import java.rmi.NotBoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TesterService {

    @Autowired
    private TesterRepository testerRepository;

    @Autowired
    private StaffService staffService;

    @Autowired
    private LaboratoryService laboratoryService;

    public void addTester (Tester tester) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (tester.getTesterId() == null) {
            throw new IncorrectInputException("Tester id is null");
        }
        if (testerRepository.getByTesterId(tester.getTesterId()) != null) {
            throw new AlreadyExistException("Tester with this id already exist");
        }
        checkConstraint(tester);
        testerRepository.save(tester);
    }

    public Tester getTester (Integer testerId) throws NotFoundException {
        Tester tester = testerRepository.getByTesterId(testerId);
        if (tester == null) {
            throw new NotFoundException("Tester not found");
        }
        return tester;
    }

    public void updateTester (Tester tester, Integer testerId) throws NotFoundException, IncorrectInputException {

        if (testerRepository.getByTesterId(testerId) == null) {
            throw new NotFoundException("Tester with this id not found");
        }
        tester.setTesterId(testerId);
        checkConstraint(tester);
        testerRepository.save(tester);
    }

    public void deleteTester (Integer testerId) throws NotFoundException, IncorrectInputException {
        if (testerRepository.getByTesterId(testerId) == null) {
            throw new NotFoundException("Tester with this id is null");
        }

        if (testerRepository.getProdTestingContains(testerId) != null) {
            throw new IncorrectInputException("Delete or update tester type from product testing");
        }

        testerRepository.delete(testerRepository.getByTesterId(testerId));
    }

    private void checkConstraint (Tester tester) throws IncorrectInputException, NotFoundException {
        if (tester.getLaboratoryId() == null) {
            throw new IncorrectInputException("Laboratory id is null");
        }

        if (!staffService.existByStaffId(tester.getTesterId())) {
            throw new NotFoundException("Staff with this id not found");
        }

        if (staffService.getStaffTypeById(tester.getTesterId()) != 3) {
            throw new IncorrectInputException("Incorrect staff type");
        }
    }

    public void createStaffTester (TesterRequest testerRequest) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException, AlreadyExistException, OverflowException {
        StaffRequest staffRequest = new StaffRequest(testerRequest.getName(), testerRequest.getSurname(),
                testerRequest.getBirthDate(), testerRequest.getEducation(), testerRequest.getEmploymentDate(), testerRequest.getDismissalDate(),
                testerRequest.getSalary(), 3);
        staffService.addStaff(staffRequest);

        Staff staff = staffService.getStaffByNameAndSurnameAndBirthDate(testerRequest.getName(), testerRequest.getSurname(), testerRequest.getBirthDate());

        addTester(new Tester(staff.getId(), testerRequest.getLaboratoryId()));
    }

    public List<Integer> getAllStaffTypes () {
        return staffService.getAllStaffType();
    }

    public Laboratory getLaboratory (Integer laboratoryId) throws NotFoundException {
        return laboratoryService.getLaboratory(laboratoryId);
    }

    public List<Integer> getAllLaboratories () {
        return testerRepository.getAllLaboratories();
    }
}
