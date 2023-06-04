package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Laboratory;

import java.util.List;
import java.util.Set;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Integer> {

    Laboratory getByName (String name);

    Laboratory getLaboratoryById (Integer id);

    @Query(value = "SELECT LABORATORY_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextLaboratoryId();

    boolean existsById (Integer id);

    @Query(value = "SELECT * FROM LABORATORY", nativeQuery = true)
    List<Laboratory> getLaboratories ();

    @Query(value = "SELECT LABORATORY_ID\n" +
                    "FROM WORKSHOP_LABORATORY\n" +
                    "WHERE LABORATORY_ID = ?1\n" +
                    "GROUP BY LABORATORY_ID", nativeQuery = true)
    Integer getWorkshopLaboratoryContains (Integer laboratoryId);

    @Query(value = "SELECT LABORATORY_ID\n" +
                    "FROM TESTER\n" +
                    "WHERE LABORATORY_ID = ?1 \n" +
                    "GROUP BY LABORATORY_ID", nativeQuery = true)
    Integer getTesterLaboratoryContains (Integer laboratoryId);

    @Query(value = "SELECT LABORATORY_ID\n" +
                    "FROM EQUIPMENT\n" +
                    "WHERE LABORATORY_ID = ?1 \n" +
                    "GROUP BY LABORATORY_ID", nativeQuery = true)
    Integer getEquipmentLaboratoryContains (Integer laboratoryId);

    @Query(value = "SELECT L.ID\n" +
            "FROM TESTING_CYCLE\n" +
            "         JOIN EQUIPMENT E on E.ID = TESTING_CYCLE.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "WHERE CYCLE_ID = (SELECT TESTING_CYCLE_ID\n" +
            "                  FROM PRODUCT\n" +
            "                           JOIN PRODUCT_INFO PI on PRODUCT.ID = PI.PRODUCT_ID\n" +
            "                           JOIN TEST_CYCLE TC on PI.TESTING_CYCLE_ID = TC.CYCLE_ID\n" +
            "                  WHERE PRODUCT_ID = ?1)\n" +
            "GROUP BY L.ID", nativeQuery = true)
    List<Integer> getLaboratoryByProductId (Integer productId);

    @Query(value = "SELECT L.ID\n" +
            "FROM TESTING_CYCLE\n" +
            "         JOIN EQUIPMENT E on E.ID = TESTING_CYCLE.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "WHERE CYCLE_ID = (SELECT TESTING_CYCLE_ID\n" +
            "                  FROM PRODUCT_INFO\n" +
            "                  WHERE PRODUCT_ID = ?1))", nativeQuery = true)
    Set<Integer> getLaboratoryByProduct (Integer productId);
}
