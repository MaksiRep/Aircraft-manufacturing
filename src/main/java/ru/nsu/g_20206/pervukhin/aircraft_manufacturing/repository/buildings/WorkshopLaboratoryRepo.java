package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.WorkshopLaboratory;

import java.util.List;

@Repository
public interface WorkshopLaboratoryRepo extends JpaRepository<WorkshopLaboratory, Integer> {

    WorkshopLaboratory getByWorkshopIdAndLaboratoryId (Integer workshopId, Integer laboratoryId);

    @Query(value = "SELECT LABORATORY_ID\n" +
                    "FROM WORKSHOP_LABORATORY\n" +
                    "WHERE WORKSHOP_ID = ?1", nativeQuery = true)
    List<Integer> getWorkshopLaboratories (Integer workshopId);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM WORKSHOP_LABORATORY\n" +
                    "WHERE LABORATORY_ID = ?1", nativeQuery = true)
    List<Integer> getLaboratoryWorkshops (Integer laboratoryId);

    boolean existsByWorkshopId (Integer workshopId);

    boolean existsByLaboratoryId (Integer laboratoryId);
}
