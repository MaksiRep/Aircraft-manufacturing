package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.TestCycle;

import java.util.List;

@Repository
public interface TestCycleRepository extends JpaRepository<TestCycle, Integer> {

    @Query(value = "SELECT TEST_CYCLE_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextTestCycleId();

    @Query(value = "SELECT CYCLE_ID FROM TEST_CYCLE WHERE CYCLE_ID > 0", nativeQuery = true)
    List<Integer> getAllTestCycles ();

    TestCycle getTestCycleByCycleId (Integer cycleId);

    @Query(value = "SELECT TESTING_CYCLE_ID\n" +
                    "FROM PRODUCT_INFO\n" +
                    "WHERE TESTING_CYCLE_ID = ?1 \n" +
                    "GROUP BY TESTING_CYCLE_ID", nativeQuery = true)
    Integer getProdInfoCycleContains (Integer testCycleId);

    @Query(value = "SELECT CYCLE_ID\n" +
                    "FROM TESTING_CYCLE\n" +
                    "WHERE CYCLE_ID = ?1 \n" +
                    "GROUP BY CYCLE_ID", nativeQuery = true)
    Integer getTestingCycleContains (Integer testCycleId);
}
