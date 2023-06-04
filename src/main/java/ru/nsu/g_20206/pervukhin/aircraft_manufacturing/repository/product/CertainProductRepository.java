package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.CertainProduct;

import java.util.Date;
import java.util.List;

@Repository
public interface CertainProductRepository extends JpaRepository<CertainProduct, Integer> {

    CertainProduct getCertainProductBySerialNumber (Integer serialNumber);

    @Query(value = "    SELECT * " +
                    "    FROM CERTAIN_PRODUCT", nativeQuery = true)
    List<CertainProduct> getAllCertainProducts();

    List<CertainProduct> getCertainProductByProductId (Integer productId);

    @Query(value = "SELECT ID FROM PRODUCT\n" +
            "WHERE PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Integer> getAllProductsByType( Integer productType);

    @Query(value = "    SELECT TESTING_CYCLE_ID " +
                    "    FROM CERTAIN_PRODUCT" +
                    "    JOIN PRODUCT_INFO PI on CERTAIN_PRODUCT.PRODUCT_ID = PI.PRODUCT_ID" +
                    "    WHERE SERIAL_NUMBER = ?1", nativeQuery = true)
    Integer getTestingCycle (Integer serialNumber);

    @Query(value = "SELECT DEV_CYCLE_ID\n" +
            "    FROM CERTAIN_PRODUCT\n" +
            "    JOIN PRODUCT_INFO PI on CERTAIN_PRODUCT.PRODUCT_ID = PI.PRODUCT_ID\n" +
            "    WHERE SERIAL_NUMBER = ?1", nativeQuery = true)
    Integer getDevelopmentCycle (Integer serialNumber);


    @Query(value = "SELECT END_TEST_DATE\n" +
            "FROM PRODUCT_TESTING\n" +
            "WHERE SERIAL_NUMBER = ?1\n" +
            "  AND TEST_STEP = (SELECT COUNT(ORD_NUM) STEPS_COUNT\n" +
            "                   FROM CERTAIN_PRODUCT\n" +
            "                            JOIN PRODUCT_INFO PI on CERTAIN_PRODUCT.PRODUCT_ID = PI.PRODUCT_ID\n" +
            "                            JOIN TEST_CYCLE TC on PI.TESTING_CYCLE_ID = TC.CYCLE_ID\n" +
            "                            JOIN TESTING_CYCLE T on TC.CYCLE_ID = T.CYCLE_ID\n" +
            "                   WHERE SERIAL_NUMBER = ?1)", nativeQuery = true)
    Date getEndTestDate (Integer serialNumber);

    @Query(value = "SELECT START_DATE\n" +
            "        FROM CERTAIN_PRODUCT\n" +
            "        WHERE SERIAL_NUMBER = ?1", nativeQuery = true)
    Date getStartDate (Integer serialNumber);

    @Query(value = "SELECT SERIAL_NUMBER\n" +
                    "FROM DEVELOPMENT\n" +
                    "WHERE SERIAL_NUMBER = ?1 \n" +
                    "GROUP BY SERIAL_NUMBER", nativeQuery = true)
    Integer getProductDevelopmentContains (Integer serialNumber);

    @Query(value = "SELECT SERIAL_NUMBER\n" +
                    "FROM PRODUCT_TESTING\n" +
                    "WHERE SERIAL_NUMBER = ?1 \n" +
                    "GROUP BY SERIAL_NUMBER", nativeQuery = true)
    Integer getProductTestingContains (Integer serialNumber);

    @Query(value = "SELECT CP.SERIAL_NUMBER\n" +
                    "FROM PRODUCT\n" +
                    "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
                    "         JOIN DEVELOPMENT D on CP.SERIAL_NUMBER = D.SERIAL_NUMBER\n" +
                    "WHERE END_DEV_DATE IS NULL\n" +
                    "  AND PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Integer> getCreatingProductsByType(Integer productType);

    @Query(value = "SELECT SERIAL_NUMBER\n" +
                    "FROM CERTAIN_PRODUCT\n" +
                    "WHERE END_DATE IS NOT NULL ", nativeQuery = true)
    List<Integer> getCreatingProducts ();

}
