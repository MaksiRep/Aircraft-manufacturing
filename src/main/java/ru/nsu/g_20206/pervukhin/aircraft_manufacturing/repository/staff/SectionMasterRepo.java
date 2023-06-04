package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff.SectionMaster;

import java.util.List;

@Repository
public interface SectionMasterRepo extends JpaRepository <SectionMaster, Integer> {

    SectionMaster getByEngineerId (Integer engineerId);

    @Query(value = "SELECT ENGINEER_ID\n" +
                    "FROM SECTION_MASTER\n" +
                    "WHERE SECTION_ID = ?1", nativeQuery = true)
    List<Integer> getSectionMasters (Integer sectionId);

    @Query(value = "SELECT SECTION_ID\n" +
                    "FROM SECTION_MASTER\n" +
                    "WHERE ENGINEER_ID = ?1", nativeQuery = true)
    Integer getMastersSection (Integer masterId);

    boolean existsByEngineerId (Integer engineerId);

    boolean existsBySectionId (Integer sectionId);

    SectionMaster getBySectionIdAndEngineerId (Integer sectionId, Integer engineerId);
}
