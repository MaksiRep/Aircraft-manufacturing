package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.StaffType;

import java.util.List;

@Repository
public interface StaffTypeRepository extends JpaRepository<StaffType, Integer> {

    StaffType getByName (String typeName);

    @Query(value = "SELECT STAFF_TYPE_SEQUENCE.nextval FROM dual", nativeQuery = true)
    Integer getNextStaffTypeId();

    @Query(value = "SELECT * FROM STAFF_TYPE", nativeQuery = true)
    List<StaffType> getAllStaffType ();

    @Query(value = "SELECT TYPE_ID FROM STAFF_TYPE WHERE TYPE_ID = ?1", nativeQuery = true)
    Integer existsStaffTypeByTypeId (Integer typeId);

    StaffType getStaffTypeByTypeId (Integer typeId);

    @Query(value = "SELECT STAFF_TYPE\n" +
                    "FROM STAFF\n" +
                    "WHERE STAFF_TYPE = ?1 \n" +
                    "GROUP BY STAFF_TYPE", nativeQuery = true)
    Integer getStaffContains(Integer typeId);
}
