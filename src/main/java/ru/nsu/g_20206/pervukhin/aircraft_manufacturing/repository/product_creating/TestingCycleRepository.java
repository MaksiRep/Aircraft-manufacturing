package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.TestingCycle;

import java.util.Date;
import java.util.List;

@Repository
public interface TestingCycleRepository extends JpaRepository <TestingCycle, Integer> {

    TestingCycle getByCycleIdAndOrdNum (Integer cycleId, Integer ordNum);

    @Query(value = "SELECT T.TESTER_ID" +
            "        FROM TESTING_CYCLE" +
            "                 JOIN EQUIPMENT E on TESTING_CYCLE.EQUIPMENT_ID = E.ID" +
            "                 JOIN LABORATORY L on E.LABORATORY_ID = L.ID" +
            "                 JOIN TESTER T on L.ID = T.LABORATORY_ID" +
            "        WHERE CYCLE_ID = ?1" +
            "          AND ORD_NUM = ?2", nativeQuery = true)
    List<Integer> getTesters (Integer cycleId, Integer testStep);

    @Query(value = "SELECT CYCLE_ID\n" +
            "FROM TESTING_CYCLE\n" +
            "WHERE CYCLE_ID = ?1 \n" +
            "GROUP BY CYCLE_ID", nativeQuery = true)
    Integer getTestingCycle (Integer cycleId);

    @Query(value = "SELECT TESTING_CYCLE.CYCLE_ID\n" +
            "FROM TESTING_CYCLE\n" +
            "         JOIN PRODUCT_TESTING PT on TESTING_CYCLE.CYCLE_ID = PT.CYCLE_ID AND TESTING_CYCLE.ORD_NUM = PT.TEST_STEP\n" +
            "WHERE TESTING_CYCLE.CYCLE_ID = ?1 AND TESTING_CYCLE.ORD_NUM = ?2 \n" +
            "GROUP BY TESTING_CYCLE.CYCLE_ID", nativeQuery = true)
    Integer getProductTestingContains (Integer cycleId, Integer ordNum);

    @Query(value = "SELECT *\n" +
            "FROM TESTING_CYCLE\n" +
            "WHERE CYCLE_ID = ?1", nativeQuery = true)
    List<TestingCycle> getProductTestingCycleByCycleId (Integer cycleId);

    @Query(value = "SELECT NAME\n" +
            "FROM EQUIPMENT\n" +
            "         JOIN TESTING_CYCLE TC on EQUIPMENT.ID = TC.EQUIPMENT_ID\n" +
            "WHERE TC.CYCLE_ID = ?1", nativeQuery = true)
    List<String> getEquipmentsNames (Integer cycleId);
}
