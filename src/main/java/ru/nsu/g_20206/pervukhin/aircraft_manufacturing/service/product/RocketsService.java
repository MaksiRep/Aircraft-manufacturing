package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Rockets;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.enums.PlaneEnum;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.enums.RocketEnum;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.RocketsRepository;

import java.rmi.NotBoundException;
import java.util.Locale;

@Service
public class RocketsService {

    @Autowired
    private RocketsRepository rocketsRepository;

    @Autowired
    private ProductService productService;

    public void addRockets (Rockets rockets) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (rockets.getRocketsId() == null) {
            throw new IncorrectInputException("Rocket id is null");
        }
        if (rocketsRepository.getByRocketsId(rockets.getRocketsId()) != null) {
            throw  new AlreadyExistException("Rocket withs this id already exist");
        }

        checkConstraint(rockets);

        String rocketType = checkRocketType(rockets.getType());

        rockets.setType(rocketType);
        rocketsRepository.save(rockets);
    }

    public Rockets getRockets (Integer rocketId) throws NotFoundException {
        Rockets rockets = rocketsRepository.getByRocketsId(rocketId);
        if ( rockets == null) {
            throw new NotFoundException("Rocket not found");
        }
        return rockets;
    }

    public void updateRockets (Rockets rockets, Integer rocketId) throws NotFoundException, IncorrectInputException {
        if (rocketsRepository.getByRocketsId(rocketId) == null) {
            throw new NotFoundException("Rocket with this id not found");
        }
        rockets.setRocketsId(rocketId);
        checkConstraint(rockets);

        rocketsRepository.save(rockets);
    }

    public void deleteRocket (Integer rocketId) throws NotFoundException {
        if (rocketsRepository.getByRocketsId(rocketId) == null) {
            throw new NotFoundException("Plane with this id not found");
        }

        rocketsRepository.delete(rocketsRepository.getByRocketsId(rocketId));
    }

    private void checkConstraint (Rockets rockets) throws IncorrectInputException, NotFoundException {
        if (rockets.getType() == null || rockets.getChargePower() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (!productService.existByProductId(rockets.getRocketsId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productService.getProductTypeById(rockets.getRocketsId()) != 3) {
            throw new IncorrectInputException("Incorrect product type");
        }

        if (rockets.getChargePower() <= 0) {
            throw new IncorrectInputException("Charge power should be more than \"0\"");
        }

        if (rockets.getChargePower() > 980000) {
            throw new IncorrectInputException("Charge power can't be more than \"980000\" tonn");
        }
    }

    private String checkRocketType (String type) throws IncorrectInputException {
        for (RocketEnum rocketEnum : RocketEnum.values()) {
            if (type.equals(rocketEnum.getValue().toLowerCase(Locale.ROOT))) {
                return rocketEnum.getValue();
            }
        }
        throw new IncorrectInputException("Incorrect rocket type, should be : \"Артиллерийская\", \"Авиационная\", \"Военно-морская\"");
    }
}
