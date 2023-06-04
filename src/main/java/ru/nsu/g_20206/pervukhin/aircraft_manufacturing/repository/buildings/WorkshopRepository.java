package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Workshop;

import java.util.List;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    Workshop getByName (String name);

    Workshop getByManagerId (Integer managerId);

    Workshop getWorkshopById (Integer id);

    @Query(value = "SELECT * FROM WORKSHOP", nativeQuery = true)
    List<Workshop> getWorkshops ();

    @Query(value = "SELECT WORKSHOP_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextWorkshopId();

    boolean existsById (Integer id);

    @Query(value = "SELECT MANAGER_ID FROM WORKSHOP", nativeQuery = true)
    List<Integer> getManagers ();


    @Query(value = "SELECT ENGINEER_ID FROM WORKSHOP_MASTER WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getMasterById (Integer masterID);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM WORKSHOP_LABORATORY\n" +
                    "WHERE WORKSHOP_ID = ?1 \n" +
                    "GROUP BY WORKSHOP_ID", nativeQuery = true)
    Integer getWorkshopLaboratoryContains (Integer workshopId);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM WORKSHOP_MASTER\n" +
                    "WHERE WORKSHOP_ID = ?1 \n" +
                    "GROUP BY WORKSHOP_ID", nativeQuery = true)
    Integer getWorkshopMasterContains (Integer workshopId);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM SECTION\n" +
                    "WHERE WORKSHOP_ID = ?1 \n" +
                    "GROUP BY WORKSHOP_ID", nativeQuery = true)
    Integer getWorkshopSectionContains (Integer workshopId);

    @Query(value = "SELECT WORKSHOP_ID\n" +
                    "FROM PRODUCT_INFO\n" +
                    "WHERE WORKSHOP_ID = ?1 \n" +
                    "GROUP BY WORKSHOP_ID", nativeQuery = true)
    Integer getWorkshopProductInfoContains (Integer workshopId);

    @Query(value = "SELECT S.NAME, S.SURNAME\n" +
            "FROM WORKSHOP\n" +
            "         JOIN STAFF S on WORKSHOP.MANAGER_ID = S.ID\n" +
            "WHERE WORKSHOP.ID = ?1", nativeQuery = true)
    String getWorkshopManagerName (Integer workshopId);


    @Query(value = "WITH WORKSHOPS_MANAGERS AS (SELECT ENGINEERING_ID AS MANAGER_ID\n" +
            "                            FROM ENGINEERING_STAFF\n" +
            "                                     JOIN WORKSHOP W on ENGINEERING_ID = W.MANAGER_ID),\n" +
            "     SECTIONS_MANAGERS AS (SELECT ENGINEERING_ID AS MANAGER_ID\n" +
            "                           FROM ENGINEERING_STAFF\n" +
            "                                    JOIN SECTION S on ENGINEERING_STAFF.ENGINEERING_ID = S.MANAGER_ID),\n" +
            "     SECTIONS_MASTERS AS (SELECT ENGINEER_ID AS MASTER_ID\n" +
            "                          FROM ENGINEERING_STAFF\n" +
            "                                   JOIN SECTION_MASTER SM on ENGINEERING_STAFF.ENGINEERING_ID = SM.ENGINEER_ID\n" +
            "                                   JOIN SECTION S3 on SM.SECTION_ID = S3.ID),\n" +
            "     WORKSHOPS_MASTERS AS (SELECT ENGINEER_ID AS MASTER_ID\n" +
            "                           FROM ENGINEERING_STAFF\n" +
            "                                    JOIN WORKSHOP_MASTER WM on ENGINEERING_STAFF.ENGINEERING_ID = WM.ENGINEER_ID)\n" +
            "\n" +
            "SELECT STAFF.ID\n" +
            "FROM STAFF\n" +
            "         JOIN ENGINEERING_STAFF ES ON ES.ENGINEERING_ID = STAFF.ID\n" +
            "         LEFT JOIN WORKSHOPS_MANAGERS ON WORKSHOPS_MANAGERS.MANAGER_ID = ES.ENGINEERING_ID\n" +
            "         LEFT JOIN WORKSHOPS_MASTERS ON WORKSHOPS_MASTERS.MASTER_ID = ES.ENGINEERING_ID\n" +
            "         LEFT JOIN SECTIONS_MANAGERS ON SECTIONS_MANAGERS.MANAGER_ID = ES.ENGINEERING_ID\n" +
            "         LEFT JOIN SECTIONS_MASTERS ON SECTIONS_MASTERS.MASTER_ID = ES.ENGINEERING_ID\n" +
            "\n" +
            "WHERE WORKSHOPS_MANAGERS.MANAGER_ID IS NULL\n" +
            "  AND WORKSHOPS_MASTERS.MASTER_ID IS NULL\n" +
            "  AND SECTIONS_MANAGERS.MANAGER_ID IS NULL\n" +
            "  AND SECTIONS_MASTERS.MASTER_ID IS NULL\n" +
            "  AND STAFF.DISMISSAL_DATE IS NULL", nativeQuery = true)
    List<Integer> getFreeEngineers ();
}
