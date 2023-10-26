package itmo.MainService.controller;

import itmo.MainService.DTO.HouseCreateDTO;
import itmo.MainService.entity.House;
import itmo.MainService.exception.HouseExistsException;
import itmo.MainService.exception.HouseNotFoundException;
import itmo.MainService.exception.IncorrectParametersException;
import itmo.MainService.service.HouseService;
import itmo.MainService.utility.FilterCriteria;
import itmo.MainService.utility.SortDirection;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping(value = "/houses", produces = "application/xml")
public class HouseController {

    private final HouseService houseService;
    private final Logger logger = LogManager.getLogger(HouseController.class);

    @Autowired
    public HouseController(HouseService houseService){
        this.houseService = houseService;
    }

    @GetMapping(value = "/{name}", produces = "application/xml")
    public House findHouseById(@PathVariable String name) throws HouseNotFoundException {
        logger.info("Received request to find house by id = " + name);
        return houseService.getHouseByName(name);
    }

    @PostMapping(value ="" , produces = "application/xml")
    public House createNewHouse(@Valid @RequestBody HouseCreateDTO houseCreateDTO) throws IncorrectParametersException, HouseExistsException {
        logger.info("Received request to create new house! " + houseCreateDTO);
        if (houseCreateDTO == null) {
            logger.error("Request is null!");
            throw new IncorrectParametersException("Request can't be empty");
        }
        if (!houseCreateDTO.isFull()) {
            logger.error("Request is not full");
            throw new IncorrectParametersException("Request is not full");
        }
        return houseService.createNewHouse(houseCreateDTO.getName(), houseCreateDTO.getYear(), houseCreateDTO.getNumberOfFloors());

    }

    @GetMapping(value = "", produces = "application/xml")
    public List<House> getAllHouses(@RequestParam Integer pageNumber){
        logger.info("Received a request to get all houses. Page = " + pageNumber);
        return houseService.getAllHouses(pageNumber);
    }

    @GetMapping(value ="/", produces = "application/xml")
    public List<House> getHousesByFilteringAndSorting(
            @RequestBody List<FilterCriteria> filterCriteriaList,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam Integer pageNumber){
        logger.info("Received a request to get houses by filtering");
        if (sortDirection == null || sortField == null) {
            logger.info("Request will be only filtered");
            return houseService.getHousesWithFilterAndPage(filterCriteriaList, pageNumber);
        }
        logger.info("Request will be filtered and sorted");
        return houseService.getHousesWithFilterSortAndPage(filterCriteriaList, pageNumber, sortField, SortDirection.valueOf(sortDirection));
    }

    @PutMapping(value ="/{name}", produces = "application/xml")
    public House updateHouseById(@PathVariable String name,
                                 @RequestParam(required = false) Integer year,
                                 @RequestParam(required = false) Long numberOfFloors) throws HouseNotFoundException, HouseExistsException {
        logger.info("Received a request to update house by id =" + name);
        return houseService.updateHouseByName(name, year, numberOfFloors);
    }

    @DeleteMapping(value ="/{name}", produces = "application/xml")
    public void deleteHouseById(@PathVariable String name) throws HouseNotFoundException {
        logger.info("Received a request to delete a house by id " + name);
        houseService.deleteHouse(name);
    }
}
