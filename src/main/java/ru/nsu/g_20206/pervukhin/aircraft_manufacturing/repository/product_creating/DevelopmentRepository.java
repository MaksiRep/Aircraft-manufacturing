package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.Development;

import java.util.Date;
import java.util.List;

@Repository
public interface DevelopmentRepository extends JpaRepository<Development, Integer> {

    Development getBySerialNumberAndCycleIdAndDevStep(Integer serialNumber, Integer cycleId, Integer devStep);

    @Query(value = "SELECT DEVELOPMENT_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextDevelopmentId();

    @Query(value = "SELECT END_DEV_DATE " +
                    "FROM DEVELOPMENT WHERE SERIAL_NUMBER = ?1 AND " +
                    "DEV_STEP = " +
                    "(SELECT COUNT(D.ORD_NUM) AS LAST_STEP " +
                    "FROM PRODUCT_INFO " +
                    "JOIN CERTAIN_PRODUCT CP on PRODUCT_INFO.PRODUCT_ID = CP.PRODUCT_ID " +
                    "JOIN DEV_CYCLE DC on PRODUCT_INFO.DEV_CYCLE_ID = DC.CYCLE_ID " +
                    "JOIN DEVELOPMENT_CYCLE D on DC.CYCLE_ID = D.CYCLE_ID " +
                    "WHERE CP.SERIAL_NUMBER = ?1)", nativeQuery = true)
    Date getEndDevelopmentDate (Integer serialNumber);


    @Query(value = "SELECT END_DEV_DATE\n" +
            "    FROM DEVELOPMENT\n" +
            "    WHERE SERIAL_NUMBER = ?1 \n" +
            "    AND DEV_STEP = (?2 - 1) \n" +
            "    AND CYCLE_ID = ?3", nativeQuery = true)
    Date getPreviousEndDate (Integer SerialNumber, Integer devStep, Integer cycleId);

    Development getByDevelopmentId (Integer developmentId);

    @Query(value = "SELECT * FROM DEVELOPMENT\n", nativeQuery = true)
    List<Development> getAllDevelopments ();

    @Query(value = "SELECT CYCLE_ID FROM DEVELOPMENT_CYCLE\n" +
            "GROUP BY CYCLE_ID", nativeQuery = true)
    List<Integer> getAllDevCycles ();

    @Query(value = "SELECT BRIGADE_ID FROM BRIGADE", nativeQuery = true)
    List<Integer> getAllBrigades();
}
