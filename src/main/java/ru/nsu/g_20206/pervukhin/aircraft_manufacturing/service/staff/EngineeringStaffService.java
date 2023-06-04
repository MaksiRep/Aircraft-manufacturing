package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.EngineeringStaffRepository;

import java.rmi.NotBoundException;
import java.text.ParseException;
import java.util.List;

@Service
public class EngineeringStaffService {

    @Autowired
    private EngineeringStaffRepository engineeringStaffRepository;

    @Autowired
    private StaffService staffService;

    public void addEngineer(EngineeringStaff engineeringStaff) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        if (engineeringStaff.getEngineeringId() == null) {
            throw new IncorrectInputException("Engineer id is null");
        }
        if (engineeringStaffRepository.getByEngineeringId(engineeringStaff.getEngineeringId()) != null) {
            throw new AlreadyExistException("Engineer already exist");
        }
        checkConstraint(engineeringStaff);
        engineeringStaffRepository.save(engineeringStaff);
    }

    public EngineeringStaff getEngineer(Integer engineerId) throws NotFoundException {
        EngineeringStaff engineeringStaff = engineeringStaffRepository.getByEngineeringId(engineerId);
        if (engineeringStaff == null) {
            throw new NotFoundException("Engineering staff not found");
        }
        return engineeringStaff;
    }

    public void updateEngineer(EngineeringStaff engineeringStaff, Integer engineerId) throws NotFoundException, IncorrectInputException {
        if (engineeringStaffRepository.getByEngineeringId(engineerId) == null) {
            throw new NotFoundException("Engineer with this id not found");
        }
        engineeringStaff.setEngineeringId(engineerId);
        checkConstraint(engineeringStaff);
        engineeringStaffRepository.save(engineeringStaff);
    }

    public void deleteEngineer (Integer engineerId) throws NotFoundException, IncorrectInputException {
        if (engineeringStaffRepository.getByEngineeringId(engineerId) == null) {
            throw new NotFoundException("Engineer with this id not found");
        }

        if (engineeringStaffRepository.getSectionManagerContains(engineerId) != null) {
            throw new IncorrectInputException("Delete or update engineer from section");
        }

        if (engineeringStaffRepository.getWorkshopManagerContains(engineerId) != null) {
            throw new IncorrectInputException("Delete or update engineer from workshop");
        }

        if (engineeringStaffRepository.getSectionMasterContains(engineerId) != null) {
            throw new IncorrectInputException("Delete or update engineer from section master");
        }

        if (engineeringStaffRepository.getWorkshopMasterContains(engineerId) != null) {
            throw new IncorrectInputException("Delete or update engineer from workshop master");
        }

        engineeringStaffRepository.delete(engineeringStaffRepository.getByEngineeringId(engineerId));
    }

    private void checkConstraint(EngineeringStaff engineeringStaff) throws IncorrectInputException, NotFoundException {
        if (engineeringStaff.getSpecialization() == null) {
            throw new IncorrectInputException("Specialization is null");
        }

        if (engineeringStaff.getSpecialization().length() > 22) {
            throw new IncorrectInputException("To long specialization, should be lass than \"23\" symbols");
        }

        if (!staffService.existByStaffId(engineeringStaff.getEngineeringId())) {
            throw new NotFoundException("Staff with this id not found");
        }

        if (staffService.getStaffTypeById(engineeringStaff.getEngineeringId()) != 1) {
            throw new IncorrectInputException("Incorrect staff type");
        }

    }

    public boolean existByEngineeringId(Integer engineeringId) {
        return engineeringStaffRepository.existsByEngineeringId(engineeringId);
    }

    public void createStaffEngineer (EngineerRequest engineerRequest) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException, AlreadyExistException, OverflowException {
        StaffRequest staffRequest = new StaffRequest(engineerRequest.getName(), engineerRequest.getSurname(),
                engineerRequest.getBirthDate(), engineerRequest.getEducation(), engineerRequest.getEmploymentDate(), engineerRequest.getDismissalDate(),
                engineerRequest.getSalary(), 1);
        staffService.addStaff(staffRequest);

        Staff staff = staffService.getStaffByNameAndSurnameAndBirthDate(engineerRequest.getName(), engineerRequest.getSurname(), engineerRequest.getBirthDate());

        addEngineer(new EngineeringStaff(staff.getId(), engineerRequest.getSpecialization()));
    }

    public List<Integer> getAllStaffTypes () {
        return staffService.getAllStaffType();
    }

}
