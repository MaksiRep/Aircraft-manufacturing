package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository <Section, Integer> {

    Section getByName (String name);

    Section getByManagerId (Integer managerId);

    Section getSectionById (Integer id);

    @Query(value = "SELECT * FROM SECTION", nativeQuery = true)
    List<Section> getSections ();

    @Query(value = "SELECT SECTION_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextSectionId();

    boolean existsById (Integer id);

    @Query(value = "SELECT ENGINEER_ID FROM SECTION_MASTER WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getMasterById (Integer masterID);

    @Query(value = "SELECT MANAGER_ID FROM SECTION", nativeQuery = true)
    List<Integer> getManagers ();

    @Query(value = "SELECT SECTION_ID\n" +
                    "FROM DEVELOPMENT_CYCLE\n" +
                    "WHERE SECTION_ID = ?1 \n" +
                    "GROUP BY SECTION_ID", nativeQuery = true)
    Integer getDevCycleSectionContains (Integer sectionId);

    @Query(value = "SELECT SECTION_ID\n" +
                    "FROM SECTION_MASTER\n" +
                    "WHERE SECTION_ID = ?1 \n" +
                    "GROUP BY SECTION_ID", nativeQuery = true)
    Integer getSectionMasterContains (Integer sectionId);

    @Query(value = "SELECT SECTION_ID\n" +
                    "FROM BRIGADE\n" +
                    "WHERE SECTION_ID = ?1 \n" +
                    "GROUP BY SECTION_ID", nativeQuery = true)
    Integer getBrigadeSectionContains (Integer sectionId);

    @Query(value = "SELECT S.ID\n" +
            "FROM DEVELOPMENT_CYCLE\n" +
            "JOIN SECTION S on DEVELOPMENT_CYCLE.SECTION_ID = S.ID\n" +
            "WHERE CYCLE_ID = (SELECT CYCLE_ID\n" +
            "                  FROM PRODUCT_INFO\n" +
            "                           JOIN DEV_CYCLE DC on PRODUCT_INFO.DEV_CYCLE_ID = DC.CYCLE_ID\n" +
            "                  WHERE PRODUCT_ID = ?1)", nativeQuery = true)
    List<Integer> getProductSections (Integer productId);

    @Query(value = "SELECT ID FROM WORKSHOP", nativeQuery = true)
    List<Integer> getWorkshopsIds ();

    @Query(value = "SELECT * FROM SECTION", nativeQuery = true)
    List<Section> getAllSections ();

    @Query(value = "SELECT * FROM SECTION WHERE WORKSHOP_ID = ?1", nativeQuery = true)
    List<Section> getSectionByWorkshopId (Integer workshopId);

    @Query(value = "SELECT ID FROM PRODUCT WHERE ID = ?1", nativeQuery = true)
    Integer getProductById (Integer productId);

    @Query(value = "SELECT * \n" +
            "FROM DEVELOPMENT_CYCLE\n" +
            "         JOIN SECTION S on DEVELOPMENT_CYCLE.SECTION_ID = S.ID\n" +
            "WHERE CYCLE_ID = (SELECT DEV_CYCLE_ID\n" +
            "                  FROM PRODUCT_INFO\n" +
            "                  WHERE PRODUCT_ID = ?1)\n", nativeQuery = true)
    List<Section> getProductCycle (Integer productId);

    @Query(value = "SELECT S.NAME, S.SURNAME\n" +
            "FROM SECTION\n" +
            "         JOIN STAFF S on SECTION.MANAGER_ID = S.ID\n" +
            "WHERE SECTION.ID = ?1", nativeQuery = true)
    String getSectionManagerName (Integer sectionId);

    @Query(value = "SELECT W.NAME\n" +
            "FROM SECTION\n" +
            "         JOIN WORKSHOP W on SECTION.WORKSHOP_ID = W.ID\n" +
            "WHERE SECTION.ID = ?1", nativeQuery = true)
    String getSectionWorkshopName (Integer sectionId);

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
