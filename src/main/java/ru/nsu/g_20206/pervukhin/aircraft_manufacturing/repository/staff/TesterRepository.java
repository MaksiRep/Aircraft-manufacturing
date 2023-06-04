package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Tester;

import java.util.List;

@Repository
public interface TesterRepository extends JpaRepository <Tester, Integer> {

    Tester getByTesterId (Integer testerId);

    @Query(value = "SELECT TESTER_ID\n" +
                    "FROM PRODUCT_TESTING\n" +
                    "WHERE TESTER_ID = 132\n" +
                    "GROUP BY TESTER_ID", nativeQuery = true)
    Integer getProdTestingContains (Integer testerId);

    @Query(value = "SELECT ID FROM LABORATORY", nativeQuery = true)
    List<Integer> getAllLaboratories ();
}
