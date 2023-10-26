package itmo.MainService.controller;

import io.swagger.annotations.ApiOperation;
import itmo.MainService.DTO.FlatCreateDTO;
import itmo.MainService.entity.Flat;
import itmo.MainService.exception.FlatNotFoundException;
import itmo.MainService.exception.HouseNotFoundException;
import itmo.MainService.exception.IncorrectParametersException;
import itmo.MainService.service.FlatService;
import itmo.MainService.utility.FilterCriteria;
import itmo.MainService.utility.SortDirection;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/flats", produces = "application/xml")
public class FlatController {

    private final FlatService flatService;
    private final Logger logger = LogManager.getLogger(FlatController.class);

    @Autowired
    public FlatController(FlatService flatService){
        this.flatService = flatService;
    }

    @ApiOperation(value="Returns Flat by id" ,nickname = "Finding a flat by id")
    @GetMapping("/{id}")
    public Flat getFlatById(@PathVariable int id) throws FlatNotFoundException {
        logger.info("Received a request to get a flat by id =" +id);
        return flatService.getFlatById(id);
    }

    @ApiOperation(value="Returns all flats paged" ,nickname = "See all flats")
    @GetMapping("")
    public List<Flat> getAllFlats(int pageNumber){
        logger.info("Received a request to get all flats. Page =" + pageNumber);
        return flatService.getAllFlats(pageNumber);
    }

    @ApiOperation(value="Returns all flats filtered" ,nickname = "See certain flats by filter")
    @GetMapping("/")
    public List<Flat> getFlatsFiltered(
            @RequestBody List<FilterCriteria> filterCriteriaList,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam int pageNumber){
        logger.info("Received a request to get flats filtered!");
        if (sortField != null && sortDirection != null){
            logger.info("Response will be filtered and sorted");
            return flatService.getFlatsWithFilterSortAndPage(filterCriteriaList, pageNumber, sortField, SortDirection.valueOf(sortDirection));
        }
        logger.info("Response will be only filtered");
        return flatService.getFlatsWithFilterAndPage(filterCriteriaList, pageNumber);
    }

    @ApiOperation(value="Update flat by id" ,nickname = "Updating a flat")
    @PutMapping("/{id}")
    public Flat updateFlatById(
            @PathVariable int id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double coordinateX,
            @RequestParam(required = false) Integer coordinateY,
            @RequestParam(required = false) Integer area,
            @RequestParam(required = false) Long numberOfRooms,
            @RequestParam(required = false) String furnish,
            @RequestParam(required = false) String view,
            @RequestParam(required = false) String transport,
            @RequestParam(required = false) String newHouseName,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) Boolean hasBalcony
    ) throws HouseNotFoundException, FlatNotFoundException {
        logger.info("Received a request to update flat by id =" + id);
        return flatService.updateFlatById(id, name, coordinateX, coordinateY,
                area, numberOfRooms, furnish, view, transport,
                newHouseName, price, hasBalcony);
    }

    @ApiOperation(value="Delete flat by id" ,nickname = "Deleting a flat")
    @DeleteMapping("/{id}")
    public void deleteFlatById(@PathVariable int id) throws FlatNotFoundException {
        logger.info("Received a request to delete a flat by id " + id);
        flatService.deleteFlatById(id);
    }

    @ApiOperation(value="Create a new flat" ,nickname = "Creating a new flat")
    @PostMapping("")
    public Flat createFlat(
            @Valid @RequestBody FlatCreateDTO flatCreateDTO
    ) throws IncorrectParametersException, HouseNotFoundException {
        logger.info("Received a request to create a new Flat. " + flatCreateDTO);
        if (flatCreateDTO == null) {
            logger.error("Request body is null!");
            throw new IncorrectParametersException("Request can't be empty!");
        }
        if (!flatCreateDTO.isFull()) {
            logger.error("Request is not full!");
            throw new IncorrectParametersException("Request is not full!");
        }
        return flatService.createNewFlatToExistingHouse(flatCreateDTO.getName(), flatCreateDTO.getCoordinateX(),
                flatCreateDTO.getCoordinateY(), flatCreateDTO.getArea(), flatCreateDTO.getNumberOfRooms(),
                flatCreateDTO.getFurnish().toUpperCase(), flatCreateDTO.getView().toUpperCase(), flatCreateDTO.getTransport().toUpperCase(), flatCreateDTO.getHouseName(),
                flatCreateDTO.getPrice(), flatCreateDTO.getHasBalcony());
    }

    @ApiOperation(value="Get flats from one house" ,nickname = "Find flats from one house")
    @GetMapping("/same-house/count")
    public Integer findFlatsWithSameHouse(@RequestParam String houseName) throws HouseNotFoundException, IncorrectParametersException {
        logger.info("Received a request to get count of flats in the same house");
        return flatService.getFlatsWithSameHouse(houseName);
    }

    @ApiOperation(value="Get count of flats with rooms less than" ,nickname = "Get count of flats with rooms less than")
    @GetMapping("/number-of-rooms/less")
    public Long findFlatsWithNumberOfRoomsLessThan(@RequestParam Integer minRooms){
        logger.info("Received a request to get count of flats with less than " + minRooms + " rooms");
        return flatService.getFlatsWithLessNumberOfRooms(minRooms);
    }

    @ApiOperation(value="Delete flats from one house" ,nickname = "Delete flats from one house")
    @DeleteMapping("/same-house/remove")
    public void deleteFlatsWithSameHouse(@RequestParam String houseName) throws IncorrectParametersException, HouseNotFoundException {
        logger.info("Received request to delete all flats with house = " + houseName);
        flatService.deleteFlatsByHouseId(houseName);
    }
}
