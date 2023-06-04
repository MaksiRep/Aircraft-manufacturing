package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.WorkerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Brigade;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkerStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.WorkerStaffRepository;

import java.text.ParseException;
import java.util.List;

@Service
public class WorkerStaffService {

    @Autowired
    private WorkerStaffRepository workerStaffRepository;

    @Autowired
    private StaffService staffService;

    public void addWorkerStaff (WorkerStaff workerStaff) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (workerStaff.getWorkerId() == null ) {
            throw new IncorrectInputException("Worker id is null");
        }

        if (workerStaffRepository.getWorkerStaffByWorkerId(workerStaff.getWorkerId()) != null) {
            throw new AlreadyExistException("Worker already exist");
        }

        checkConstraint(workerStaff);
        workerStaffRepository.save(workerStaff);
    }

    public WorkerStaff getWorker (Integer workerId) throws NotFoundException {
        WorkerStaff workerStaff = workerStaffRepository.getWorkerStaffByWorkerId(workerId);
        if (workerStaff == null) {
            throw new NotFoundException("Worker not found");
        }
        return workerStaff;
    }

    public void updateWorker (WorkerStaff workerStaff, Integer workerId) throws NotFoundException, IncorrectInputException {
        if (workerStaffRepository.getWorkerStaffByWorkerId(workerId) == null) {
            throw new NotFoundException("Worker with this id not found");
        }
        workerStaff.setWorkerId(workerId);
        checkConstraint(workerStaff);
        workerStaffRepository.save(workerStaff);
    }

    public void deleteWorker (Integer workerId) throws NotFoundException, IncorrectInputException {
        if (workerStaffRepository.getWorkerStaffByWorkerId(workerId) == null) {
            throw new NotFoundException("Worker with this id not found");
        }

        if (workerStaffRepository.getBrigadeContains(workerId) != null) {
            throw new IncorrectInputException("Delete or update worker from brigade");
        }

        workerStaffRepository.delete(workerStaffRepository.getWorkerStaffByWorkerId(workerId));
    }

    private void checkConstraint (WorkerStaff workerStaff) throws NotFoundException, IncorrectInputException {
        if (staffService.existByStaffId(workerStaff.getWorkerId())) {
            throw new NotFoundException("Staff with this id not found");
        }

        if (staffService.getStaffTypeById(workerStaff.getWorkerId()) != 2) {
            throw new IncorrectInputException("Incorrect staff type");
        }

        if (workerStaff.getBrigadeId() != null) {
            if (workerStaffRepository.getBrigadeIdForWorker(workerStaff.getBrigadeId()) == null) {
                throw new NotFoundException("Brigade with this id not found");
            }
            Integer brigadeId = workerStaffRepository.getBrigadeByBrigadierId(workerStaff.getWorkerId());
            if (brigadeId != null && workerStaff.getBrigadeId() != brigadeId) {
                throw new IncorrectInputException("Worker already brigadier in other brigade, you should set the right one brigade id");
            }
        }
    }

    public boolean existByWorkerId (Integer workerId) {
        return workerStaffRepository.existsByWorkerId(workerId);
    }

    public Integer getBrigadeIdByWorkerId (Integer workerId) {
        return workerStaffRepository.getBrigadeIdByWorkerId(workerId);
    }

    public void createStaffWorker (WorkerRequest workerRequest) throws IncorrectDateException, NotFoundException, IncorrectInputException, ParseException, AlreadyExistException, OverflowException {
        StaffRequest staffRequest = new StaffRequest(workerRequest.getName(), workerRequest.getSurname(),
                workerRequest.getBirthDate(), workerRequest.getEducation(), workerRequest.getEmploymentDate(), workerRequest.getDismissalDate(),
                workerRequest.getSalary(), 2);
        staffService.addStaff(staffRequest);

        Staff staff = staffService.getStaffByNameAndSurnameAndBirthDate(workerRequest.getName(), workerRequest.getSurname(), workerRequest.getBirthDate());
        addWorkerStaff(new WorkerStaff(staff.getId(), workerRequest.getBrigadeId()));
    }

    public List<Integer> getAllStaffTypes () {
        return staffService.getAllStaffType();
    }

    public List<Integer> getAllBrigades () {
        return workerStaffRepository.getAllBrigades();
    }

    public String getBrigadeName (Integer brigadeId) {
        return workerStaffRepository.getBrigadeNameById(brigadeId);
    }
}
