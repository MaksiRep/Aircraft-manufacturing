package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer> {

    Plane getByPlaneId (Integer planeId);
}
