package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.EngineeringStaff;

import java.util.List;

@Repository
public interface EngineeringStaffRepository extends JpaRepository<EngineeringStaff, Integer> {

    EngineeringStaff getByEngineeringId (Integer engineeringId);

    boolean existsByEngineeringId (Integer engineeringId);


    @Query(value = "SELECT MANAGER_ID\n" +
                    "FROM WORKSHOP\n" +
                    "WHERE MANAGER_ID = ?1", nativeQuery = true)
    Integer getWorkshopManagerContains (Integer engineerId);

    @Query(value = "SELECT MANAGER_ID\n" +
                    "FROM SECTION\n" +
                    "WHERE MANAGER_ID = ?1", nativeQuery = true)
    Integer getSectionManagerContains (Integer engineerId);

    @Query(value = "SELECT ENGINEER_ID\n" +
                    "FROM WORKSHOP_MASTER\n" +
                    "WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getWorkshopMasterContains (Integer engineerId);

    @Query(value = "SELECT ENGINEER_ID\n" +
                    "FROM SECTION_MASTER\n" +
                    "WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getSectionMasterContains (Integer engineerId);

    @Query(value = "SELECT ID FROM LABORATORY", nativeQuery = true)
    List<Integer> getAllLaboratories ();
}
