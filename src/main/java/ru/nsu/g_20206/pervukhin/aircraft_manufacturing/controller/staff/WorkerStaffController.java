package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.BrigadeRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.TesterRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.WorkerRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkerStaff;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.*;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.staff.WorkerStaffService;

import java.beans.Transient;
import java.text.ParseException;

@Controller
public class WorkerStaffController {

    @Autowired
    private WorkerStaffService workerStaffService;

    @PostMapping(value = "staff/worker")
    public ResponseEntity<Void> addWorkerStaff(@RequestBody WorkerStaff workerStaff) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        workerStaffService.addWorkerStaff(workerStaff);
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = "staff/worker/{workerId}")
//    public ResponseEntity<WorkerStaff> getWorkerStaff(@PathVariable(name = "workerId") Integer workerId) throws NotFoundException {
//        return new ResponseEntity<>(workerStaffService.getWorker(workerId), HttpStatus.OK);
//    }

    @PutMapping(value = "staff/worker/{workerId}")
    public ResponseEntity<Void> updateWorkerStaff(@RequestBody WorkerStaff workerStaff,
                                            @PathVariable(name = "workerId") Integer workerId) throws NotFoundException, IncorrectInputException {
        workerStaffService.updateWorker(workerStaff, workerId);
        return ResponseEntity.ok().build();
    }

    @Transient
    @DeleteMapping(value = "staff/worker/{workerId}")
    public ResponseEntity<Void> deleteWorkerStaff(@PathVariable(name = "workerId") Integer workerId) throws NotFoundException, IncorrectInputException {
        workerStaffService.deleteWorker(workerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "staff/worker/{workerId}")
    public String getWorkerId(@PathVariable(name = "workerId") Integer workerId, Model model) throws NotFoundException {
        WorkerStaff workerStaff = workerStaffService.getWorker(workerId);
        model.addAttribute("brigadeId", workerStaff.getBrigadeId());
        model.addAttribute("brigadeName", workerStaffService.getBrigadeName(workerStaff.getBrigadeId()));
        model.addAttribute("workerId", workerStaff.getWorkerId());
        return "staff/workerStaff/workerStaffInfo";
    }

    @GetMapping(value = "staff/staff/addWorker")
    public String addFormStaffWorker(Model model) {
        model.addAttribute("workerRequest", new WorkerRequest());
        model.addAttribute("allStaffTypes", workerStaffService.getAllStaffTypes());
        model.addAttribute("allBrigades", workerStaffService.getAllBrigades());
        return "staff/staff/createWorker";
    }

    @PostMapping(value = "staff/staffAddWorker")
    public String addStaffWorker(@ModelAttribute("workerRequest") WorkerRequest workerRequest) throws IncorrectDateException, ParseException, AlreadyExistException, IncorrectInputException, OverflowException, NotFoundException {
        workerStaffService.createStaffWorker(workerRequest);
        return "redirect:/staff/allStaff";
    }
}
