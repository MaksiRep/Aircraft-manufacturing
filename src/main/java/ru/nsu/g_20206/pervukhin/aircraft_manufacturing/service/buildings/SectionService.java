package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.buildings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request.SectionRequest;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings.Section;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.OverflowException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.buildings.SectionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public void addSection(SectionRequest sectionRequest) throws IncorrectInputException, OverflowException, NotFoundException {


        Integer nextVal = sectionRepository.getNextSectionId();
        if (nextVal > 300) {
            throw new OverflowException("Section overflow, please delete something");
        }

        Section section = new Section(nextVal, sectionRequest.getName(), sectionRequest.getWorkshopId(), sectionRequest.getManagerId());

        checkConstraint(section);

        if (section.getManagerId() != null && section.getManagerId() != 0) {
            if (sectionRepository.getByManagerId(section.getManagerId()) != null) {
                throw new IncorrectInputException("This manager already exist in other section");
            }
            if (sectionRepository.getMasterById(section.getManagerId()) != null) {
                throw new IncorrectInputException("Engineer already master in section");
            }
        }

        sectionRepository.save(section);
    }

    public Section getSection(Integer sectionId) throws NotFoundException {

        Section section = sectionRepository.getSectionById(sectionId);
        if (section == null) {
            throw new NotFoundException("Laboratory not found");
        }
        return section;
    }

    public void updateSection(Section section, Integer sectionId) throws NotFoundException, IncorrectInputException {

        if (sectionRepository.getSectionById(sectionId) == null) {
            throw new NotFoundException("Section not found");
        }

        checkConstraint(section);

        if (section.getManagerId() != null && section.getManagerId() != 0) {
            if (sectionRepository.getByManagerId(section.getManagerId()) != null &&
                    sectionRepository.getByManagerId(section.getManagerId()).getId() != sectionId) {
                throw new IncorrectInputException("This manager already exist in other section");
            }
            if (sectionRepository.getMasterById(section.getManagerId()) != null) {
                throw new IncorrectInputException("Engineer already master in section");
            }
        }

        section.setId(sectionId);
        sectionRepository.save(section);
    }

    public void deleteSection(Integer sectionId) throws NotFoundException, IncorrectInputException {

        if (sectionRepository.getSectionById(sectionId) == null) {
            throw new NotFoundException("Section with this id not found");
        }

        if (sectionRepository.getSectionMasterContains(sectionId) != null) {
            throw new IncorrectInputException("Delete or update section from section - master connection");
        }

        if (sectionRepository.getBrigadeSectionContains(sectionId) != null) {
            throw new IncorrectInputException("Delete or update section from brigade");
        }

        if (sectionRepository.getDevCycleSectionContains(sectionId) != null) {
            throw new IncorrectInputException("Delete or update section from development cycle");
        }

        sectionRepository.delete(sectionRepository.getSectionById(sectionId));
    }

    public List<Section> getProductSections(Integer productId) throws NotFoundException {
        if (sectionRepository.getProductById(productId) == null) {
            throw new NotFoundException("Product with this id not found");
        }
        List<Integer> sectionIds = sectionRepository.getProductSections(productId);
        ArrayList<Section> staffList = new ArrayList<>();
        for (Integer sectionId : sectionIds) {
            staffList.add(sectionRepository.getSectionById(sectionId));
        }
        return staffList;

    }

    public List<Section> getSections() {
        return sectionRepository.getSections();
    }


    private void checkConstraint(Section section) throws IncorrectInputException, NotFoundException {

        if (section.getWorkshopId() == null || section.getWorkshopId() == 0 || section.getName() == null || section.getName().length() == 0) {
            throw new IncorrectInputException("Null values");
        }

        if (!sectionRepository.getWorkshopsIds().contains(section.getWorkshopId())) {
            throw new NotFoundException("Workshop with is id not found");
        }

        if (section.getName().length() > 30) {
            throw new IncorrectInputException("To long name, should be less than \"31\" symbols");
        }
    }

    public boolean existBySectionId(Integer sectionId) {
        return sectionRepository.getSectionById(sectionId) != null;
    }

    public List<Integer> getManagers() {
        return sectionRepository.getManagers();
    }

    public List<Section> getAllSections () {
        return sectionRepository.getAllSections();
    }

    public List<Section> getSectionsByWorkshopId (Integer workshopId) {
        return sectionRepository.getSectionByWorkshopId(workshopId);
    }

    public List<Section> getProductCycle (Integer productId) {
        return sectionRepository.getProductCycle(productId);
    }

    public String getSectionManagerName (Integer sectionId) {
        return sectionRepository.getSectionManagerName(sectionId).replace(',', ' ');
    }

    public String getWorkshopName (Integer sectionId) {
        return sectionRepository.getSectionWorkshopName(sectionId).replace(',', ' ');
    }

    public List<Integer> getFreeEngineers () {
        return sectionRepository.getFreeEngineers();
    }
}
