package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.Staff;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff getByNameAndSurnameAndBirthDate (String name, String surname, Date birthDate);

    @Query(value = "SELECT * FROM STAFF", nativeQuery = true)
    List<Staff> getAllStaff ();

    @Query(value = "SELECT STAFF_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextStaffId();

    boolean existsById (Integer id);

    @Query(value = "SELECT STAFF_TYPE FROM STAFF WHERE ID = ?1", nativeQuery = true)
    Integer getStaffTypeById (Integer staffId);

    @Query(value = "SELECT ENGINEERING_ID\n" +
                    "FROM ENGINEERING_STAFF", nativeQuery = true)
    List<Integer> getEngineers ();

    @Query(value = "SELECT WORKER_ID\n" +
            "FROM WORKER_STAFF", nativeQuery = true)
    List<Integer> getWorkers ();

    @Query(value = "SELECT TESTER_ID\n" +
            "FROM TESTER", nativeQuery = true)
    List<Integer> getTesters ();

    Staff getStaffById (Integer id);

    @Query(value = "SELECT ENGINEERING_ID\n" +
                    "FROM ENGINEERING_STAFF\n" +
                    "WHERE ENGINEERING_ID = ?1", nativeQuery = true)
    Integer getEngineer (Integer engineerId);

    @Query(value = "SELECT TESTER_ID\n" +
                    "FROM TESTER\n" +
                    "WHERE TESTER_ID = ?1", nativeQuery = true)
    Integer getTester (Integer testerId);

    @Query(value = "SELECT WORKER_ID\n" +
                    "FROM WORKER_STAFF\n" +
            "       WHERE WORKER_ID = ?1", nativeQuery = true)
    Integer getWorker (Integer workerId);

    @Query(value = "WITH WORKSHOPS_MANAGERS AS (SELECT ENGINEERING_ID AS MANAGER_ID, W.ID AS WORKSHOP_ID\n" +
            "                            FROM ENGINEERING_STAFF\n" +
            "                                     JOIN WORKSHOP W on ENGINEERING_ID = W.MANAGER_ID),\n" +
            "     SECTIONS_MANAGERS AS (SELECT ENGINEERING_ID AS MANAGER_ID, S.WORKSHOP_ID AS WORKSHOP_ID\n" +
            "                           FROM ENGINEERING_STAFF\n" +
            "                                    JOIN SECTION S on ENGINEERING_STAFF.ENGINEERING_ID = S.MANAGER_ID),\n" +
            "     SECTIONS_WORKERS AS (SELECT WORKER_ID AS WORKER_ID, S2.WORKSHOP_ID AS WORKSHOP_ID\n" +
            "                          FROM WORKER_STAFF\n" +
            "                                   JOIN BRIGADE B on WORKER_STAFF.BRIGADE_ID = B.BRIGADE_ID\n" +
            "                                   JOIN SECTION S2 on B.SECTION_ID = S2.ID),\n" +
            "     SECTIONS_MASTERS AS (SELECT ENGINEER_ID AS MASTER_ID, S3.WORKSHOP_ID AS WORKSHOP_ID\n" +
            "                          FROM ENGINEERING_STAFF\n" +
            "                                   JOIN SECTION_MASTER SM on ENGINEERING_STAFF.ENGINEERING_ID = SM.ENGINEER_ID\n" +
            "                                   JOIN SECTION S3 on SM.SECTION_ID = S3.ID),\n" +
            "     WORKSHOPS_MASTERS AS (SELECT ENGINEER_ID AS MASTER_ID, WM.WORKSHOP_ID AS WORKSHOP_ID\n" +
            "                           FROM ENGINEERING_STAFF\n" +
            "                                    JOIN WORKSHOP_MASTER WM on ENGINEERING_STAFF.ENGINEERING_ID = WM.ENGINEER_ID)\n" +
            "\n" +
            "SELECT STAFF.ID\n" +
            "FROM STAFF\n" +
            "         LEFT JOIN WORKSHOPS_MANAGERS ON WORKSHOPS_MANAGERS.MANAGER_ID = STAFF.ID\n" +
            "         LEFT JOIN WORKSHOPS_MASTERS ON WORKSHOPS_MASTERS.MASTER_ID = STAFF.ID\n" +
            "         LEFT JOIN SECTIONS_MANAGERS ON SECTIONS_MANAGERS.MANAGER_ID = STAFF.ID\n" +
            "         LEFT JOIN SECTIONS_MASTERS ON SECTIONS_MASTERS.MASTER_ID = STAFF.ID\n" +
            "         LEFT JOIN SECTIONS_WORKERS ON SECTIONS_WORKERS.WORKER_ID = STAFF.ID\n" +
            "WHERE ?1 IN (WORKSHOPS_MANAGERS.WORKSHOP_ID,\n" +
            "            SECTIONS_MANAGERS.WORKSHOP_ID,\n" +
            "            SECTIONS_WORKERS.WORKSHOP_ID,\n" +
            "            SECTIONS_MASTERS.WORKSHOP_ID,\n" +
            "            WORKSHOPS_MASTERS.WORKSHOP_ID)", nativeQuery = true)
    List<Integer> getWorkshopStaffByType (Integer workshopId);


    @Query(value = "SELECT S2.ID, S2.NAME, S2.SURNAME, S2.BIRTH_DATE, S2.EDUCATION, S2.EMPLOYMENT_DATE, S2.DISMISSAL_DATE, S2.SALARY, S2.STAFF_TYPE\n" +
            "FROM DEVELOPMENT_CYCLE\n" +
            "         JOIN SECTION S on DEVELOPMENT_CYCLE.SECTION_ID = S.ID\n" +
            "         JOIN BRIGADE B on S.ID = B.SECTION_ID\n" +
            "         JOIN WORKER_STAFF WS on B.BRIGADE_ID = WS.BRIGADE_ID\n" +
            "         JOIN STAFF S2 on WS.WORKER_ID = S2.ID\n" +
            "WHERE CYCLE_ID = (SELECT DEV_CYCLE_ID\n" +
            "                  FROM PRODUCT_INFO\n" +
            "                  WHERE PRODUCT_ID = ?1)", nativeQuery = true)
    List<Staff> getBrigadeWorkersByProductId (Integer productId);

    @Query(value = "SELECT S.ID\n" +
                    "FROM SECTION\n" +
                    "         JOIN BRIGADE B on SECTION.ID = B.SECTION_ID\n" +
                    "         JOIN WORKER_STAFF WS on B.BRIGADE_ID = WS.BRIGADE_ID\n" +
                    "         JOIN STAFF S on WS.WORKER_ID = S.ID\n" +
                    "WHERE WORKSHOP_ID = ?1", nativeQuery = true)
    List<Integer> getWorkshopBrigadeWorkers (Integer workshopId);

    @Query(value = "SELECT S.ID\n" +
            "FROM SECTION\n" +
            "         JOIN BRIGADE B on SECTION.ID = B.SECTION_ID\n" +
            "         JOIN WORKER_STAFF WS on B.BRIGADE_ID = WS.BRIGADE_ID\n" +
            "         JOIN STAFF S on WS.WORKER_ID = S.ID\n" +
            "WHERE SECTION_ID = ?1", nativeQuery = true)
    List<Integer> getSectionBrigadeWorkers (Integer section);

    @Query(value = "SELECT ID FROM SECTION", nativeQuery = true)
    List<Integer> getSections ();

    @Query(value = "SELECT S.ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "         JOIN TESTER T on T.TESTER_ID = PT.TESTER_ID\n" +
            "         JOIN STAFF S on T.TESTER_ID = S.ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND L.ID = ?1 \n" +
            "  AND PRODUCT_ID = ?2", nativeQuery = true)
    List<Integer> getStaffByLaboratoryAndProductAndDateAnd (Integer laboratoryId, Integer productId, String firstDate, String secondDate);

    @Query(value = "SELECT S.ID" +
            " FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "         JOIN TESTER T on T.TESTER_ID = PT.TESTER_ID\n" +
            "         JOIN STAFF S on T.TESTER_ID = S.ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND L.ID = ?1 \n" +
            "  AND PRODUCT_TYPE = ?2", nativeQuery = true)
    List<Integer> getStaffByLaboratoryAndProductAndDateAndProdType (Integer laboratoryId, Integer productType, String firstDate, String secondDate);

    @Query(value = "SELECT S.ID" +
            " FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "         JOIN TESTER T on T.TESTER_ID = PT.TESTER_ID\n" +
            "         JOIN STAFF S on T.TESTER_ID = S.ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND L.ID = ?1 \n", nativeQuery = true)
    List<Integer> getStaffByLaboratoryInAll (Integer laboratoryId, String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM STAFF\n" +
            "WHERE NAME = ?1 AND SURNAME = ?2 AND BIRTH_DATE = TO_DATE(?3 , 'dd.mm.yyyy')", nativeQuery = true)
    Staff getStaffByNameAndSurnameAndBirthDate (String name, String surname, String birthDate);

    @Query(value = "DELETE FROM ENGINEERING_STAFF WHERE ENGINEERING_ID = ?1", nativeQuery = true)
    void deleteEngineer (Integer id);

    @Query(value = "DELETE FROM TESTER WHERE TESTER_ID = ?1", nativeQuery = true)
    void deleteTester (Integer id);

    @Query(value = "DELETE FROM WORKER_STAFF WHERE WORKER_ID = ?1", nativeQuery = true)
    void deleteWorker (Integer id);

    @Query(value = "SELECT T.TESTER_ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "         JOIN TESTER T on T.TESTER_ID = PT.TESTER_ID\n" +
            "         JOIN STAFF S on T.TESTER_ID = S.ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND L.ID = ?1", nativeQuery = true)
    Set<Integer> getTestersByLabIdAndDate(Integer laboratoryId, String firstDate, String secondDate);

    @Query(value = "SELECT T.TESTER_ID\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "         JOIN LABORATORY L on E.LABORATORY_ID = L.ID\n" +
            "         JOIN TESTER T on T.TESTER_ID = PT.TESTER_ID\n" +
            "         JOIN STAFF S on T.TESTER_ID = S.ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND PRODUCT_TYPE = ?1\n" +
            "  AND L.ID = ?2", nativeQuery = true)
    Set<Integer> getTesterByLaboratoryIdAndProdTypeAndDate (Integer laboratoryId, Integer productTypeId, String firstDate, String secondDate);
}
