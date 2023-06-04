package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Glider;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.GliderRepository;

import java.rmi.NotBoundException;

@Service
public class GliderService {

    @Autowired
    private GliderRepository gliderRepository;

    @Autowired
    private ProductService productService;

    public void addGlider (Glider glider) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (glider.getGliderId() == null) {
            throw new IncorrectInputException("Glider id is null");
        }
        if (gliderRepository.getByGliderId(glider.getGliderId()) != null) {
            throw new AlreadyExistException("Glider with this id already exist");
        }
        checkConstraint(glider);
        gliderRepository.save(glider);
    }

    public Glider getGlider (Integer gliderId) throws NotFoundException {
        Glider glider = gliderRepository.getByGliderId(gliderId);
        if (glider == null) {
            throw new NotFoundException("Glider not found");
        }
        return glider;
    }

    public void updateGlider (Glider glider, Integer gliderId) throws NotFoundException, IncorrectInputException {
        if (gliderRepository.getByGliderId(gliderId) == null) {
            throw new NotFoundException("Glider not found");
        }
        glider.setGliderId(gliderId);
        checkConstraint(glider);
        gliderRepository.save(glider);
    }

    public void deleteGlider (Integer gliderId) throws NotFoundException {
        if (gliderRepository.getByGliderId(gliderId) == null) {
            throw new NotFoundException("Glider with this id not found");
        }

        gliderRepository.delete(gliderRepository.getByGliderId(gliderId));
    }

    private void checkConstraint (Glider glider) throws IncorrectInputException, NotFoundException {

        if (glider.getWingspan() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (!productService.existByProductId(glider.getGliderId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productService.getProductTypeById(glider.getGliderId()) != 4) {
            throw new IncorrectInputException("Incorrect product type");
        }

        if (glider.getWingspan() <= 0) {
            throw new IncorrectInputException("Wingspan should be more than \"0\"");
        }

        if (glider.getWingspan() > 50) {
            throw new IncorrectInputException("Wingspan can't be more than \"50\" meters");
        }
    }
}
