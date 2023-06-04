package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.ProductType;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository <ProductType, Integer> {

    ProductType getByName (String name);

    @Query(value = "SELECT PRODUCT_TYPE_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextProductTypeId();

    @Query(value = "SELECT * FROM PRODUCT_TYPE", nativeQuery = true)
    List<ProductType> getAllTypes();

    boolean existsByTypeId (Integer typeId);

    ProductType getProductTypeByTypeId (Integer typeId);

    @Query(value = "SELECT PRODUCT_TYPE\n" +
                    "FROM PRODUCT\n" +
                    "WHERE PRODUCT_TYPE = ?1 \n" +
                    "GROUP BY PRODUCT_TYPE", nativeQuery = true)
    Integer getProductTypeContains (Integer productTypeId);
}
