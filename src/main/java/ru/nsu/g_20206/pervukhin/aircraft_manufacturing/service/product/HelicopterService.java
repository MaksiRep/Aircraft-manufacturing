package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Helicopter;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.HelicopterRepository;

import java.rmi.NotBoundException;

@Service
public class HelicopterService {

    @Autowired
    private HelicopterRepository helicopterRepository;

    @Autowired
    private ProductService productService;

    public void addHelicopter (Helicopter helicopter) throws AlreadyExistException, IncorrectInputException, NotFoundException {
        if (helicopter.getHelicopterId() == null ) {
            throw new IncorrectInputException("Helicopter id is null");
        }
        if (helicopterRepository.getByHelicopterId(helicopter.getHelicopterId()) != null) {
            throw new AlreadyExistException("Helicopter with this id already exist");
        }
        checkConstraint(helicopter);
        helicopterRepository.save(helicopter);
    }

    public Helicopter getHelicopter (Integer helicopterId) throws NotFoundException {
        Helicopter helicopter = helicopterRepository.getByHelicopterId(helicopterId);
        if ( helicopter == null) {
            throw new NotFoundException("Helicopter not found");
        }
        return helicopter;
    }

    public void updateHelicopter (Helicopter helicopter, Integer helicopterId) throws NotFoundException, IncorrectInputException, AlreadyExistException {

        if (helicopterRepository.getByHelicopterId(helicopterId) == null) {
            throw new IncorrectInputException("Helicopter with this id Not found");
        }

        helicopter.setHelicopterId(helicopterId);
        checkConstraint(helicopter);

        helicopterRepository.save(helicopter);
    }

    public void deleteHelicopter (Integer helicopterId) throws NotFoundException {
        if (helicopterRepository.getByHelicopterId(helicopterId) == null) {
            throw new NotFoundException("Helicopter with this id not found");
        }

        helicopterRepository.delete(helicopterRepository.getByHelicopterId(helicopterId));
    }

    private void checkConstraint (Helicopter helicopter) throws IncorrectInputException, NotFoundException {

        if ( helicopter.getPropellersSize() == null) {
            throw new IncorrectInputException("Propellers size is null");
        }
        if (!productService.existByProductId(helicopter.getHelicopterId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productService.getProductTypeById(helicopter.getHelicopterId()) != 2) {
            throw new IncorrectInputException("Incorrect product type");
        }

        if (helicopter.getPropellersSize() <= 0) {
            throw new IncorrectInputException("Propellers size should be more than \"0\"");
        }

        if (helicopter.getPropellersSize() > 10) {
            throw new IncorrectInputException("Propellers size can't be more than \"10\" meters");
        }
    }
}
