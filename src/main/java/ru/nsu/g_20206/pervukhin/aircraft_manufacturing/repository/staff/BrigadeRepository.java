package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Brigade;

import java.util.List;

@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Integer> {

    Brigade getByName (String name);

    Brigade getByBrigadierId (Integer brigadierId);

    @Query(value = "SELECT BRIGADE_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextBrigadeId();


    @Query(value = "SELECT B.BRIGADE_ID\n" +
            "    FROM DEVELOPMENT_CYCLE\n" +
            "    JOIN SECTION S on DEVELOPMENT_CYCLE.SECTION_ID = S.ID\n" +
            "    JOIN BRIGADE B on S.ID = B.SECTION_ID\n" +
            "    WHERE ORD_NUM = ?1 \n" +
            "    AND CYCLE_ID = ?2", nativeQuery = true)
    List<Integer> getBrigadesByDevCycle (Integer devStep, Integer devCycle);

    Brigade getBrigadeByBrigadeId (Integer brigadeId);

    @Query(value = "SELECT BRIGADE_ID\n" +
                    "FROM WORKER_STAFF\n" +
                    "WHERE BRIGADE_ID = ?1 \n" +
                    "GROUP BY BRIGADE_ID", nativeQuery = true)
    Integer getWorkersContains (Integer brigadeId);

    @Query(value = "SELECT BRIGADE_ID\n" +
                    "FROM DEVELOPMENT\n" +
                    "WHERE BRIGADE_ID = ?1 \n" +
                    "GROUP BY BRIGADE_ID", nativeQuery = true)
    Integer getDevelopmentContains (Integer brigadeId);

    @Query(value = "SELECT BRIGADE_ID FROM BRIGADE", nativeQuery = true)
    List<Integer> getAllBrigadesIds ();
}
