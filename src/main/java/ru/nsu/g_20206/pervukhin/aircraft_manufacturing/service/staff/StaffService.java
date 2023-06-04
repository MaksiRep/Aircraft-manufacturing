package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.EngineerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.StaffRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.StaffType;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff.StaffRepository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.SectionService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings.WorkshopService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.CertainProductService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductService;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product.ProductTypeService;

import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffTypeService staffTypeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorkshopService workshopService;

    public void addStaff(StaffRequest staffRequest) throws ParseException, IncorrectDateException, AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {


        Integer nextVal = staffRepository.getNextStaffId();
        if (nextVal > 500000) {
            throw new OverflowException("Staff overflow, please delete something");
        }

        Staff staff = checkConstraint(staffRequest, nextVal);
        if (!staffTypeService.existByStaffTypeId(staffRequest.getStaffType())) {
            throw new NotFoundException("Staff type with this id not found");
        }
        Date birthDate = formatDateFromString(staffRequest.getBirthDate());
        if (staffRepository.getByNameAndSurnameAndBirthDate(staffRequest.getName(), staffRequest.getSurname(), birthDate) != null) {
            throw new AlreadyExistException("Staff already exist");
        }
        staffRepository.save(staff);
    }

    public Staff getStaff(Integer staffId) throws NotFoundException {
        Staff staff = staffRepository.getStaffById(staffId);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }
        return staff;
    }

    public void updateStaff(StaffRequest staffRequest, Integer staffId) throws NotFoundException, IncorrectDateException, IncorrectInputException, ParseException, AlreadyExistException {
        if (staffRepository.getStaffById(staffId) == null) {
            throw new NotFoundException("Staff with this id not found");
        }
        Staff staff = checkConstraint(staffRequest, staffId);
        if (!staffTypeService.existByStaffTypeId(staffRequest.getStaffType())) {
            throw new NotFoundException("Staff type with this id not found");
        }
        Date birthDate = formatDateFromString(staffRequest.getBirthDate());
        Staff checkStaff = staffRepository.getByNameAndSurnameAndBirthDate(staffRequest.getName(), staffRequest.getSurname(), birthDate);
        if (checkStaff != null && checkStaff.getId() != staffId) {
            throw new AlreadyExistException("Staff already exist");
        }

        staffRepository.save(staff);
    }

    public void deleteStaff(Integer staffId) throws NotFoundException {
        if (staffRepository.getStaffById(staffId) == null) {
            throw new NotFoundException("Staff with this id not found");
        }

        if (staffRepository.getEngineer(staffId) != null) {
            staffRepository.deleteEngineer(staffId);
        } else if (staffRepository.getWorker(staffId) != null) {
            staffRepository.deleteWorker(staffId);
        } else if (staffRepository.getTester(staffId) != null) {
            staffRepository.deleteTester(staffId);
        }

        staffRepository.delete(staffRepository.getStaffById(staffId));
    }

    public List<Staff> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    public List<Staff> getWorkshopStaffByWorkshop(Integer workshopId) throws NotFoundException {

        if (workshopService.getWorkshop(workshopId) == null) {
            throw new NotFoundException("Workshop with this id not found");
        }

        List<Integer> staffIds = staffRepository.getWorkshopStaffByType(workshopId);
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer staffId : staffIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }
        return staffList;
    }

    public List<Staff> getBrigadeWorkersByProdId(Integer productId) throws NotFoundException {
        if (productService.getProduct(productId) == null) {
            throw new NotFoundException("Product with this id not found");
        }
        return staffRepository.getBrigadeWorkersByProductId(productId);
    }

    public List<Staff> getSectionWorkers(Integer sectionId) throws NotFoundException {

        if (staffRepository.getSections() == null || !staffRepository.getSections().contains(sectionId)) {
            throw new NotFoundException("Section with this id not found");
        }

        List<Integer> workerIds = staffRepository.getSectionBrigadeWorkers(sectionId);
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer staffId : workerIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }
        return staffList;
    }

    public List<Staff> getWorkshopWorkers(Integer workshopId) throws NotFoundException {

        if (workshopService.getWorkshop(workshopId) == null) {
            throw new NotFoundException("Workshop with this id not found");
        }

        List<Integer> workerIds = staffRepository.getWorkshopBrigadeWorkers(workshopId);
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer staffId : workerIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }
        return staffList;
    }

    public List<Staff> getEngineers() {
        List<Integer> engineersIds = staffRepository.getEngineers();
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer engineerId : engineersIds) {
            staffList.add(staffRepository.getStaffById(engineerId));
        }
        return staffList;
    }

    public List<Staff> getWorkers() {
        List<Integer> workersIds = staffRepository.getWorkers();
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer workerId : workersIds) {
            staffList.add(staffRepository.getStaffById(workerId));
        }
        return staffList;
    }

    public List<Staff> getTesters() {
        List<Integer> testersIds = staffRepository.getTesters();
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer testerId : testersIds) {
            staffList.add(staffRepository.getStaffById(testerId));
        }
        return staffList;
    }

    private Staff checkConstraint(StaffRequest staffRequest, Integer staffId) throws IncorrectInputException, NotFoundException, IncorrectDateException, ParseException, AlreadyExistException {

        Date birthDate;
        Date dismissalDate = null;
        Date employmentDate;

        if (staffRequest.getName() == null || staffRequest.getName().length() == 0 ||
                staffRequest.getSurname() == null || staffRequest.getSurname().length() == 0 ||
                staffRequest.getBirthDate() == null || staffRequest.getBirthDate().length() == 0 ||
                staffRequest.getEducation() == null || staffRequest.getEducation().length() == 0 ||
                staffRequest.getEmploymentDate() == null || staffRequest.getEmploymentDate().length() == 0 ||
                staffRequest.getSalary() == null || staffRequest.getSalary() == 0 ||
                staffRequest.getStaffType() == null || staffRequest.getStaffType() == 0) {
            throw new IncorrectInputException("Incorrect values");
        }

        if (staffRequest.getSalary() <= 0) {
            throw new IncorrectInputException("Salary should be more than \"0\"");
        }

        if (staffRequest.getSalary() > 800000) {
            throw new IncorrectInputException("Salary can't be more than \"800000\"");
        }

        if (staffRequest.getName().length() > 10) {
            throw new IncorrectInputException("To long name, should be lass than \"11\" symbols");
        }

        if (staffRequest.getSurname().length() > 15) {
            throw new IncorrectInputException("To long surname, should be lass than \"16\" symbols");
        }

        if (staffRequest.getEducation().length() > 40) {
            throw new IncorrectInputException("To long education, should be lass than \"40\" symbols");
        }

        birthDate = formatDateFromString(staffRequest.getBirthDate());
        Date minBirthDate = formatDateFromString("01.01.1900");
        if (birthDate.after(new Date()) || birthDate.before(minBirthDate)) {
            throw new IncorrectInputException("Incorrect birth date");
        }

        employmentDate = formatDateFromString(staffRequest.getEmploymentDate());
        Date minEmployDate = formatDateFromString("01.01.1900");
        if (employmentDate.after(new Date()) || birthDate.before(minEmployDate)) {
            throw new IncorrectInputException("Incorrect employ date");
        }
        if (staffRequest.getDismissalDate() != null && staffRequest.getDismissalDate().length() != 0) {
            dismissalDate = formatDateFromString(staffRequest.getDismissalDate());
            if (dismissalDate.after(employmentDate)) {
                throw new IncorrectInputException("Dismissal date should be after employ date");
            }
        }


        long diffMillis = new Date().getTime() - birthDate.getTime();
        long diffHours = diffMillis / (60 * 1000 * 60 * 24);
        long diffYears = diffHours / 365;

        if (diffYears < 18) {
            throw new IncorrectInputException("Staff should be more than 17 years");
        }

        return new Staff(
                staffId,
                staffRequest.getName(),
                staffRequest.getSurname(),
                birthDate,
                staffRequest.getEducation(),
                employmentDate,
                dismissalDate,
                staffRequest.getSalary(),
                staffRequest.getStaffType());
    }

    private Date formatDateFromString(String stringDate) throws IncorrectDateException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if (stringDate == null)
            throw new IncorrectDateException("Nullable date");
        return formatter.parse(stringDate);
    }

    public boolean existByStaffId(Integer staffId) {
        return staffRepository.getStaffById(staffId) == null;
    }

    public Integer getStaffTypeById(Integer typeId) {
        return staffRepository.getStaffTypeById(typeId);
    }

    public List<Workshop> getWorkshops() {
        return workshopService.getWorkshops();
    }

    public List<Staff> getStaffByLaboratoryAndProductAndDateAnd(Integer laboratoryId, Integer productId, String firstDate, String secondDate) {
        List<Integer> staffIds = staffRepository.getStaffByLaboratoryAndProductAndDateAnd(laboratoryId, productId, firstDate, secondDate);
        ArrayList<Staff> staffList = new ArrayList<>();

        for (Integer staffId : staffIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }

        return staffList;
    }

    public List<Staff> getStaffByLaboratoryAndProductAndDateAndProdType(Integer laboratoryId, Integer productType, String firstDate, String secondDate) {
        List<Integer> staffIds = staffRepository.getStaffByLaboratoryAndProductAndDateAndProdType(laboratoryId, productType, firstDate, secondDate);
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer staffId : staffIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }
        return staffList;
    }

    public List<Staff> getStaffByLaboratoryInAll(Integer laboratoryId, String firstDate, String secondDate) {
        List<Integer> staffIds = staffRepository.getStaffByLaboratoryInAll(laboratoryId, firstDate, secondDate);
        ArrayList<Staff> staffList = new ArrayList<>();
        for (Integer staffId : staffIds) {
            staffList.add(staffRepository.getStaffById(staffId));
        }
        return staffList;
    }

    public Staff getStaffByNameAndSurnameAndBirthDate(String name, String surname, String birthDate) throws IncorrectDateException, ParseException {
        return staffRepository.getStaffByNameAndSurnameAndBirthDate(name, surname, birthDate);
    }

    public StaffType getStaffType (Integer staffTypeId) throws NotFoundException {
        return staffTypeService.getStaffType(staffTypeId);
    }

    public List<Integer> getAllStaffType () {
        List<StaffType> staffTypes = staffTypeService.getAllStaffType();
        ArrayList<Integer> staffTypeIds = new ArrayList<>();
        for (StaffType staffType : staffTypes) {
            staffTypeIds.add(staffType.getTypeId());
        }
        return staffTypeIds;
    }

    public List<Staff> getAllTestersByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate) {
        Set<Integer> testerIds = staffRepository.getTestersByLabIdAndDate(laboratoryId, firstDate, secondDate);

        ArrayList<Staff> staff = new ArrayList<>();
        for (Integer staffId : testerIds) {
            staff.add(staffRepository.getStaffById(staffId));
        }
        return staff;
    }

    public List<Staff> getTesterByLaboratoryIdAndProdTypeAndDate (Integer laboratoryId, Integer productTypeId, String firstDate, String secondDate) {
        Set<Integer> testerIds = staffRepository.getTesterByLaboratoryIdAndProdTypeAndDate(laboratoryId, productTypeId, firstDate, secondDate);

        ArrayList<Staff> staff = new ArrayList<>();
        for (Integer staffId : testerIds) {
            staff.add(staffRepository.getStaffById(staffId));
        }
        return staff;
    }
}
