package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.WorkshopMaster;

import java.util.List;

@Repository
public interface WorkshopMasterRepo extends JpaRepository<WorkshopMaster, Integer> {

    WorkshopMaster getByEngineerId(Integer engineerId);

    @Query(value = "SELECT ENGINEER_ID\n" +
                    "FROM WORKSHOP_MASTER\n" +
                    "WHERE WORKSHOP_ID = ?1", nativeQuery = true)
    List<Integer> getWorkshopMasters(Integer workshopId);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM WORKSHOP_MASTER\n" +
                    "WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getMastersWorkshop(Integer masterId);

    boolean existsByEngineerId (Integer engineerId);

    WorkshopMaster getByWorkshopIdAndEngineerId (Integer workshopId, Integer engineerId);
}
