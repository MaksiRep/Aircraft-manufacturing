package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Product;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product getByName (String name);

    @Query(value = "SELECT PRODUCT_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextProductId();

    @Query(value = "SELECT PRODUCT_TYPE FROM PRODUCT WHERE ID = ?1", nativeQuery = true)
    Integer getProductTypeById (Integer productId);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN PRODUCT_INFO PI on PRODUCT.ID = PI.PRODUCT_ID\n" +
            "WHERE WORKSHOP_ID = ?1 \n" +
            "  AND PRODUCT_TYPE = ?2 ", nativeQuery = true)
    List<Product> getProductsByWorkshopAndProdType (Integer workshopId, Integer productTypeId);

    Product getProductById (Integer id);

    @Query(value = "SELECT * FROM PRODUCT", nativeQuery = true)
    List<Product> getAllProducts ();

    @Query(value = "SELECT * FROM PRODUCT WHERE PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Product> getAllProductsByProdId (Integer productTypeId);

    @Query(value = "SELECT PRODUCT_ID FROM PRODUCT_INFO WHERE WORKSHOP_ID = ?1", nativeQuery = true)
    List<Integer> getWorkshopIds (Integer workshopId);

    @Query(value = "SELECT PLANE_ID\n" +
                    "FROM PLANE\n" +
                    "WHERE PLANE_ID = ?1 \n" +
                    "GROUP BY PLANE_ID", nativeQuery = true)
    Integer getProductPlaneContains (Integer productId);

    @Query(value = "SELECT HELICOPTER_ID\n" +
                    "FROM HELICOPTER\n" +
                    "WHERE HELICOPTER_ID = ?1 \n" +
                    "GROUP BY HELICOPTER_ID", nativeQuery = true)
    Integer getProductHelicopterContains (Integer productId);

    @Query(value = "SELECT ROCKETS_ID\n" +
                    "FROM ROCKETS\n" +
                    "WHERE ROCKETS_ID = ?1 \n" +
                    "GROUP BY ROCKETS_ID", nativeQuery = true)
    Integer getProductRocketContains (Integer productId);

    @Query(value = "SELECT GLIDER_ID\n" +
                    "FROM GLIDER\n" +
                    "WHERE GLIDER_ID = ?1 \n" +
                    "GROUP BY GLIDER_ID", nativeQuery = true)
    Integer getProductGliderContains (Integer productId);

    @Query(value = "SELECT HANG_GLIDER_ID\n" +
                    "FROM HANG_GLIDER\n" +
                    "WHERE HANG_GLIDER_ID = ?1 \n" +
                    "GROUP BY HANG_GLIDER_ID", nativeQuery = true)
    Integer getProductHangGliderContains (Integer productId);

    @Query(value = "SELECT PRODUCT_ID\n" +
                    "FROM PRODUCT_INFO\n" +
                    "WHERE PRODUCT_ID = ?1 \n" +
                    "GROUP BY PRODUCT_ID", nativeQuery = true)
    Integer getProductInfoContains (Integer productId);

    @Query(value = "SELECT PRODUCT_ID\n" +
                    "FROM CERTAIN_PRODUCT\n" +
                    "WHERE PRODUCT_ID = ?1 \n" +
                    "GROUP BY PRODUCT_ID", nativeQuery = true)
    Integer getCertainProductContains (Integer productId);


    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "WHERE END_DATE >= TO_DATE(?1, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?2, 'dd.mm.yyyy')\n", nativeQuery = true)
    List<Product> getAllProductsByDates (String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "WHERE END_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND PRODUCT_TYPE = ?1 ", nativeQuery = true)
    List<Product> getAllProductsByDatesAndType (Integer productType, String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_INFO PI on PRODUCT.ID = PI.PRODUCT_ID\n" +
            "WHERE END_DATE >= TO_DATE( ?1, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE( ?2, 'dd.mm.yyyy')\n" +
            "  AND WORKSHOP_ID = ?3 ", nativeQuery = true)
    List<Product> getProductsByWorkshopByDates (String firstDate, String secondDate, Integer workshopId);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN DEVELOPMENT D on CP.SERIAL_NUMBER = D.SERIAL_NUMBER\n" +
            "         JOIN DEVELOPMENT_CYCLE DC on D.CYCLE_ID = DC.CYCLE_ID and D.DEV_STEP = DC.ORD_NUM\n" +
            "WHERE END_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND SECTION_ID = ?1", nativeQuery = true)
    List<Product> getProductsBySectionByDates (Integer sectionId, String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "WHERE END_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Product> getProductsByTypeAndDate (Integer productType, String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_INFO PI on PRODUCT.ID = PI.PRODUCT_ID\n" +
            "WHERE END_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND WORKSHOP_ID = ?2\n" +
            "  AND PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Product> getProductsByTypeAndWorkshopAndDate (Integer productType, Integer workshopId, String firstDate, String secondDate);

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN DEVELOPMENT D on CP.SERIAL_NUMBER = D.SERIAL_NUMBER\n" +
            "         JOIN DEVELOPMENT_CYCLE DC on D.CYCLE_ID = DC.CYCLE_ID and D.DEV_STEP = DC.ORD_NUM\n" +
            "WHERE END_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND END_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND SECTION_ID = ?2 \n" +
            "  AND PRODUCT_TYPE = ?1", nativeQuery = true)
    List<Product> getProductsByTypeAndSectionAndDate (Integer productType, Integer sectionId, String firstDate, String secondDate);

    @Query(value = "SELECT CP.PRODUCT_ID \n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?2, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND LABORATORY_ID = ?1 \n", nativeQuery = true)
    Set<Integer> getProductsByLabIdAndDate(Integer laboratoryId, String firstDate, String secondDate);

    @Query(value = "SELECT PRODUCT.ID \n" +
            "FROM PRODUCT\n" +
            "         JOIN CERTAIN_PRODUCT CP on PRODUCT.ID = CP.PRODUCT_ID\n" +
            "         JOIN PRODUCT_TESTING PT on CP.SERIAL_NUMBER = PT.SERIAL_NUMBER\n" +
            "         JOIN TESTING_CYCLE TC on TC.CYCLE_ID = PT.CYCLE_ID and TC.ORD_NUM = PT.TEST_STEP\n" +
            "         JOIN EQUIPMENT E on E.ID = TC.EQUIPMENT_ID\n" +
            "WHERE START_TEST_DATE >= TO_DATE(?3, 'dd.mm.yyyy')\n" +
            "  AND START_TEST_DATE <= TO_DATE(?4, 'dd.mm.yyyy')\n" +
            "  AND LABORATORY_ID = ?1 AND PRODUCT_TYPE = ?2 \n", nativeQuery = true)
    Set<Integer> getProductsByLabIdAndDateAndProductType(Integer laboratoryId, Integer productType, String firstDate, String secondDate);

    @Query(value = "SELECT PT.NAME\n" +
            "FROM PRODUCT\n" +
            "         JOIN PRODUCT_TYPE PT on PRODUCT.PRODUCT_TYPE = PT.TYPE_ID\n" +
            "WHERE PRODUCT.ID = ?1", nativeQuery = true)
    String getProductTypeName (Integer productId);
}