package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.ProductTesting;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductTestingRepo extends JpaRepository<ProductTesting, Integer> {

    ProductTesting getBySerialNumberAndCycleIdAndTestStep (Integer serialNumber, Integer cycleId, Integer testStep);

    @Query(value = "SELECT PRODUCT_TESTING_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextProductTestingId();

    @Query(value = "SELECT END_TEST_DATE FROM PRODUCT_TESTING WHERE SERIAL_NUMBER = ?1 AND TEST_STEP = (?2 - 1) AND CYCLE_ID = ?3", nativeQuery = true)
    Date getPreviousEndDate (Integer serialNumber, Integer testStep, Integer cycleId);

    ProductTesting getProductTestingById (Integer id);

    @Query(value = "SELECT * FROM PRODUCT_TESTING", nativeQuery = true)
    List<ProductTesting> getAllProductTestings ();

    @Query(value = "SELECT NAME, SURNAME\n" +
            "FROM PRODUCT_TESTING\n" +
            "         JOIN STAFF S on PRODUCT_TESTING.TESTER_ID = S.ID\n" +
            "WHERE PRODUCT_TESTING.ID = ?1", nativeQuery = true)
    String getTesterName (Integer productTestingId);

    @Query(value = "SELECT CYCLE_ID FROM TESTING_CYCLE GROUP BY CYCLE_ID", nativeQuery = true)
    List<Integer> getAllTestCycles ();

    @Query(value = "SELECT TESTER_ID FROM TESTER", nativeQuery = true)
    List<Integer> getAllTesters ();

}
