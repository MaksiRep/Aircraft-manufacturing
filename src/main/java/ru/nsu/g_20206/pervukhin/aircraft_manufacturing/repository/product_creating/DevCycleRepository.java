package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product_creating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating.DevCycle;

import java.util.List;

@Repository
public interface DevCycleRepository extends JpaRepository<DevCycle, Integer> {

    @Query(value = "SELECT DEV_CYCLE_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextDevCycleId();

    @Query(value = "SELECT CYCLE_ID FROM DEV_CYCLE WHERE CYCLE_ID > 0", nativeQuery = true)
    List<Integer> getAllDevCycles ();

    DevCycle getDevCycleByCycleId (Integer cycleId);

    @Query(value = "SELECT DEV_CYCLE_ID\n" +
                    "FROM PRODUCT_INFO\n" +
                    "WHERE DEV_CYCLE_ID = ?1 \n" +
                    "GROUP BY DEV_CYCLE_ID", nativeQuery = true)
    Integer getProdInfoCycleContains (Integer devCycleId);

    @Query(value = "SELECT CYCLE_ID\n" +
                    "FROM DEVELOPMENT_CYCLE\n" +
                    "WHERE CYCLE_ID = ?1 \n" +
                    "GROUP BY CYCLE_ID", nativeQuery = true)
    Integer getDevelopCycleContains (Integer devCycleId);
}
