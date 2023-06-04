package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.HangGlider;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.AlreadyExistException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.IncorrectInputException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.exceptions.NotFoundException;
import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.repository.product.HangGliderRepository;

import java.rmi.NotBoundException;

@Service
public class HangGliderService {

    @Autowired
    private HangGliderRepository hangGliderRepository;

    @Autowired
    private ProductService productService;

    public void addHangGlider(HangGlider hangGlider) throws AlreadyExistException, IncorrectInputException, NotFoundException {

        if (hangGlider.getHangGliderId() == null) {
            throw new IncorrectInputException("Hang glider id is null");
        }
        if (hangGliderRepository.getByHangGliderId(hangGlider.getHangGliderId()) != null) {
            throw new AlreadyExistException("Hang glider with this id already exist");
        }

        checkConstrain(hangGlider);

        hangGliderRepository.save(hangGlider);
    }

    public HangGlider getHangGlider (Integer hangGliderId) throws NotFoundException {
        HangGlider hangGlider = hangGliderRepository.getByHangGliderId(hangGliderId);
        if ( hangGlider == null) {
            throw new NotFoundException("Hang glider not found");
        }
        return hangGlider;
    }

    public void updateHangGlider (HangGlider hangGlider, Integer hangGliderId) throws IncorrectInputException, NotFoundException {
        if (hangGliderRepository.getByHangGliderId(hangGliderId) == null) {
            throw new NotFoundException("Hang glider with this id not found");
        }

        hangGlider.setHangGliderId(hangGliderId);
        checkConstrain(hangGlider);

        hangGliderRepository.save(hangGlider);
    }

    public void deleteHangGlider (Integer hangGliderId) throws NotFoundException {
        if (hangGliderRepository.getByHangGliderId(hangGliderId) == null) {
            throw new NotFoundException("Hang glider with this id not found");
        }

        hangGliderRepository.delete(hangGliderRepository.getByHangGliderId(hangGliderId));
    }

    private void checkConstrain (HangGlider hangGlider) throws IncorrectInputException, NotFoundException {

        if (hangGlider.getCapacity() == null || hangGlider.getWingspan() == null) {
            throw new IncorrectInputException("Null values");
        }

        if (!productService.existByProductId(hangGlider.getHangGliderId())) {
            throw new NotFoundException("Product with this id not found");
        }

        if (productService.getProductTypeById(hangGlider.getHangGliderId()) != 5) {
            throw new IncorrectInputException("Incorrect product type");
        }

        if (hangGlider.getWingspan() <= 0) {
            throw new IncorrectInputException("Wingspan should be more then \"0\"");
        }

        if (hangGlider.getCapacity() > 3) {
            throw new IncorrectInputException("Capacity can't be more than \"3\"");
        }

        if (hangGlider.getWingspan() > 10) {
            throw new IncorrectInputException("Wingspan can't be more than \"10\" meters");
        }

        if (hangGlider.getCapacity() <= 0) {
            throw new IncorrectInputException("Capacity should be more then \"0\"");
        }
    }
}
