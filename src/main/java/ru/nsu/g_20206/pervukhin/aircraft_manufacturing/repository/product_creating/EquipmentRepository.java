package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Equipment;

import java.util.List;
import java.util.Set;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    Equipment getByName (String name);

    @Query(value = "SELECT EQUIPMENT_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextEquipmentId();

    boolean existsById (Integer id);

    Equipment getEquipmentById (Integer id);

    @Query(value = "SELECT EQUIPMENT_ID\n" +
                    "FROM TESTING_CYCLE\n" +
                    "WHERE EQUIPMENT_ID = ?1 \n" +
                    "GROUP BY EQUIPMENT_ID", nativeQuery = true)
    Integer getTestingCycleContains (Integer equipmentId);

    @Query(value = "SELECT * FROM EQUIPMENT\n", nativeQuery = true)
    List<Equipment> getAllEquipment ();

    @Query(value = "SELECT PRODUCT_ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND E.LABORATORY_ID = ?1", nativeQuery = true)
    Set<Integer> getProductsByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate);

    @Query(value = "SELECT E.ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND E.LABORATORY_ID = ?1\n" +
            "  AND PRODUCT_TYPE = ?2", nativeQuery = true)
    List<Integer> getProductsByLabIdAndDateAndProductType (Integer laboratoryId, Integer productTypeId, String firstDate, String secondDate);

    @Query(value = "SELECT E.ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND E.LABORATORY_ID = ?1\n" +
            "  AND PRODUCT_ID = ?2", nativeQuery = true)
    List<Integer> getEquipmentByLaboratoryIdAnDateAndProductId (Integer laboratoryId, Integer productId, String firstDate, String secondDate);

    @Query(value = "SELECT E.ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND E.LABORATORY_ID = ?1\n", nativeQuery = true)
    List<Integer> getAllProductsByLabIdAndDate (Integer laboratoryId, String firstDate, String secondDate);

    @Query(value = "SELECT L.NAME\n" +
            "FROM EQUIPMENT\n" +
            "         JOIN LABORATORY L on EQUIPMENT.LABORATORY_ID = L.ID\n" +
            "WHERE EQUIPMENT.ID = ?1", nativeQuery = true)
    String getLaboratoryName (Integer equipmentId);
}
