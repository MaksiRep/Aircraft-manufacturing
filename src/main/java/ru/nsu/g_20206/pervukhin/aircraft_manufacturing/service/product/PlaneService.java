package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.Plane;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.enums.PlaneEnum;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.PlaneRepository;

import java.util.Locale;

@Service
public class PlaneService {

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private ProductService productService;

    public void addPlane (Plane plane) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (plane.getPlaneId() == null) {
            throw new IncorrectInputException("Plane id is null");
        }

        if (planeRepository.getByPlaneId(plane.getPlaneId()) != null) {
            throw new AlreadyExistException("Plane with this id already exist");
        }

        checkConstraint(plane);

        String planeType = checkPlaneType(plane.getType().toLowerCase(Locale.ROOT));

        plane.setType(planeType);
        planeRepository.save(plane);
    }

    public Plane getPlane (Integer planeId) throws NotFoundException {
        Plane plane = planeRepository.getByPlaneId(planeId);
        if (plane == null) {
            throw new NotFoundException("Plane not found");
        }
        return plane;
    }

    public void updatePlane (Plane plane, Integer planeId) throws NotFoundException, IncorrectInputException {
        if (planeRepository.getByPlaneId(planeId) == null) {
            throw new NotFoundException("plane with this id not found");
        }
        plane.setPlaneId(planeId);
        checkConstraint(plane);

        String planeType = checkPlaneType(plane.getType().toLowerCase(Locale.ROOT));
        plane.setType(planeType);

        planeRepository.save(plane);
    }

    public void deletePlane (Integer planeId) throws NotFoundException {
        if (planeRepository.getByPlaneId(planeId) == null) {
            throw new NotFoundException("Plane with this id not found");
        }

        planeRepository.delete(planeRepository.getByPlaneId(planeId));
    }

    private void checkConstraint (Plane plane) throws IncorrectInputException, NotFoundException {

        if (plane.getEngineCount() == null || plane.getWingspan() == null || plane.getType() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (!productService.existByProductId(plane.getPlaneId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productService.getProductTypeById(plane.getPlaneId()) != 1) {
            throw new IncorrectInputException("Incorrect product type");
        }

        if (plane.getWingspan() <= 0) {
            throw new IncorrectInputException("Wingspan should be more than \"0\"");
        }

        if (plane.getEngineCount() <= 0) {
            throw new IncorrectInputException("Engine count should be more than \"0\"");
        }

        if (plane.getEngineCount() > 8) {
            throw new IncorrectInputException("Engine count can't be more than \"8\"");
        }

        if (plane.getWingspan() > 100) {
            throw new IncorrectInputException("Wingspan can't be more than \"100\" meters");
        }
    }

    private String checkPlaneType (String type) throws IncorrectInputException {
        for (PlaneEnum planeEnum : PlaneEnum.values()) {
            if (type.equals(planeEnum.getValue().toLowerCase(Locale.ROOT))) {
                return planeEnum.getValue();
            }
        }
        throw new IncorrectInputException("Incorrect plane type, should be : \"Гражданский\", \"Транспортный\", \"Военный\"");
    }
}
