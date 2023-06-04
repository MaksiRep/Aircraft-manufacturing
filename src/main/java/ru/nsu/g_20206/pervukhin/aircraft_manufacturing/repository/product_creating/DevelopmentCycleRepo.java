package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevelopmentCycle;

import java.util.Date;
import java.util.List;

@Repository
public interface DevelopmentCycleRepo extends JpaRepository<DevelopmentCycle, Integer> {

    DevelopmentCycle getByCycleIdAndOrdNum (Integer cycleId, Integer ordNum);

    @Query(value = "    SELECT CYCLE_ID\n" +
            "    FROM DEVELOPMENT_CYCLE\n" +
            "    JOIN SECTION S on DEVELOPMENT_CYCLE.SECTION_ID = S.ID\n" +
            "    WHERE WORKSHOP_ID = ?1 \n" +
            "    GROUP BY CYCLE_ID", nativeQuery = true)
    List<Integer> getCycleIdsByWorkshop (Integer workshopId);

    @Query(value = "SELECT CYCLE_ID\n" +
            "FROM DEVELOPMENT_CYCLE\n" +
            "WHERE CYCLE_ID = ?1 \n" +
            "GROUP BY CYCLE_ID", nativeQuery = true)
    Integer getDevelopmentCycle (Integer cycleId);

    @Query(value = "SELECT DEVELOPMENT_CYCLE.CYCLE_ID\n" +
                    "FROM DEVELOPMENT_CYCLE\n" +
                    "JOIN DEVELOPMENT D on DEVELOPMENT_CYCLE.CYCLE_ID = D.CYCLE_ID AND DEVELOPMENT_CYCLE.ORD_NUM = D.DEV_STEP\n" +
                    "WHERE DEVELOPMENT_CYCLE.CYCLE_ID = 1? AND DEVELOPMENT_CYCLE.ORD_NUM = 2? \n" +
                    "GROUP BY DEVELOPMENT_CYCLE.CYCLE_ID", nativeQuery = true)
    Integer getDevelopmentContains (Integer cycleId, Integer ordNum);

    @Query(value = "SELECT *\n" +
            "FROM DEVELOPMENT_CYCLE\n" +
            "WHERE CYCLE_ID = ?1", nativeQuery = true)
    List<DevelopmentCycle> getDevelopmentCycleByCycleId (Integer cycleId);

    @Query(value = "SELECT NAME\n" +
            "FROM SECTION\n" +
            "         JOIN DEVELOPMENT_CYCLE DC on SECTION.ID = DC.SECTION_ID\n" +
            "WHERE DC.CYCLE_ID = ?1", nativeQuery = true)
    List<String> getSectionName (Integer devCycleId);
}
