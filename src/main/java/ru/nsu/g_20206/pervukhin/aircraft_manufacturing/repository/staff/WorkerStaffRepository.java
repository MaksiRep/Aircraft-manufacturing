package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkerStaff;

import java.util.List;

@Repository
public interface WorkerStaffRepository extends JpaRepository<WorkerStaff, Integer> {

    WorkerStaff getWorkerStaffByWorkerId (Integer workerId);

    boolean existsByWorkerId (Integer workerId);

    @Query(value = "SELECT BRIGADE_ID FROM BRIGADE WHERE BRIGADE_ID = ?1", nativeQuery = true)
    Integer getBrigadeIdForWorker (Integer brigadeId);

    @Query(value = "SELECT BRIGADE_ID FROM BRIGADE WHERE BRIGADIER_ID = ?1", nativeQuery = true)
    Integer getBrigadeByBrigadierId(Integer brigadierId);

    @Query(value = "SELECT BRIGADE_ID FROM WORKER_STAFF WHERE WORKER_ID = ?1", nativeQuery = true)
    Integer getBrigadeIdByWorkerId (Integer workerId);

    @Query(value = "SELECT BRIGADIER_ID\n" +
                    "FROM BRIGADE\n" +
                    "WHERE BRIGADIER_ID = ?1 ", nativeQuery = true)
    Integer getBrigadeContains (Integer workerId);

    @Query(value = "SELECT BRIGADE_ID\n" +
            "FROM BRIGADE", nativeQuery = true)
    List<Integer> getAllBrigades ();

    @Query(value = "SELECT NAME FROM BRIGADE WHERE BRIGADE_ID = ?1", nativeQuery = true)
    String getBrigadeNameById (Integer brigadeId);

}
