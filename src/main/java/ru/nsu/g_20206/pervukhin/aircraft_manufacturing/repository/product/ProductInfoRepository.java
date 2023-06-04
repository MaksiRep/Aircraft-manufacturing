package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductInfo;

import java.util.List;

@Repository
public interface ProductInfoRepository extends JpaRepository <ProductInfo, Integer> {

    ProductInfo getByProductId (Integer productId);

    @Query(value = "SELECT * FROM PRODUCT_INFO", nativeQuery = true)
    List<ProductInfo> getAllProductInfo ();

    @Query(value = "SELECT NAME\n" +
            "FROM PRODUCT_INFO\n" +
            "         JOIN WORKSHOP W on PRODUCT_INFO.WORKSHOP_ID = W.ID\n" +
            "WHERE PRODUCT_ID = ?1", nativeQuery = true)
    String getWorkshopName (Integer productInfoId);

    @Query(value = "SELECT ID FROM WORKSHOP", nativeQuery = true)
    List<Integer> getAllWorkshops();

    @Query(value = "SELECT CYCLE_ID FROM TESTING_CYCLE GROUP BY CYCLE_ID", nativeQuery = true)
    List<Integer> getAllTestCycles();

    @Query(value = "SELECT CYCLE_ID FROM DEVELOPMENT_CYCLE GROUP BY CYCLE_ID", nativeQuery = true)
    List<Integer> getAllDevCycles();
}
