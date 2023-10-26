package itmo.MainService.service;

import itmo.MainService.entity.*;
import itmo.MainService.exception.FlatNotFoundException;
import itmo.MainService.exception.HouseNotFoundException;
import itmo.MainService.exception.IncorrectParametersException;
import itmo.MainService.repository.FlatRepository;
import itmo.MainService.repository.HouseRepository;
import itmo.MainService.utility.FilterCriteria;
import itmo.MainService.utility.FlatSpecification;
import itmo.MainService.utility.SortDirection;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlatService {

    private final FlatRepository flatRepository;
    private final HouseRepository houseRepository;

    private final Logger logger = LogManager.getLogger(FlatService.class);

    private final int PAGE_SIZE = 10;

    @Autowired
    public FlatService(FlatRepository flatRepository, HouseRepository houseRepository){
        this.flatRepository = flatRepository;
        this.houseRepository = houseRepository;
    }

    public Flat getFlatById(Integer id) throws FlatNotFoundException {
        logger.info("Finding a flat by id = " + id);
        Optional<Flat> flat = flatRepository.findById(id);
        if (flat.isEmpty()) {
            logger.error("Flat has not been found. Does not Exists");
            throw new FlatNotFoundException("Flat with id = " + id + " does not exist");
        }
        logger.info("Flat has been found. Returning");
        return flat.get();
    }



    public Flat updateFlatById(Integer id,
                            String name,
                            Double coordinateX,
                            Integer coordinateY,
                            Integer area,
                            Long numberOfRooms,
                            String furnish,
                            String view,
                            String transport,
                            String newHouseName,
                            Long price,
                            Boolean hasBalcony) throws FlatNotFoundException, HouseNotFoundException {
        logger.info("Updating a flat by id = " + id);
        Flat flat = flatRepository.findById(id).orElseThrow(() -> new FlatNotFoundException("Flat with id " + id + " does not exists!"));
        if (name != null && !name.isEmpty()) {
            logger.info("Name is correct and set!");
            flat.setName(name);
        }
        if (coordinateX != null) {
            logger.info("Coordinate X is correct and set!");
            flat.getCoordinates().setX(coordinateX);
        }
        if (coordinateY != null) {
            logger.info("Coordinate Y is correct and set!");
            flat.getCoordinates().setY(coordinateY);
        }
        if (area != null && area > 0 && area <= 527){
            logger.info("Area is correct and set!");
            flat.setArea(area);
        }
        if (numberOfRooms != null && numberOfRooms > 0) {
            logger.info("Number of rooms is correct and set!");
            flat.setNumberOfRooms(numberOfRooms);
        }
        if (furnish != null) {
            logger.info("Furnish is correct and set!");
            flat.setFurnish(Furnish.valueOf(furnish.strip().toUpperCase()));
        }
        if (view != null) {
            logger.info("View is correct and set!");
            flat.setView(View.valueOf(view.strip().toUpperCase()));
        }
        if (transport != null) {
            logger.info("Transport is correct and set");
            flat.setTransport(Transport.valueOf(transport.strip().toUpperCase()));
        }
        if (newHouseName != null && !flat.getHouse().getName().equals(newHouseName)){
            logger.info("House was correct, but checking if it exists!");
            House house = houseRepository.findById(newHouseName.strip()).orElseThrow(() -> new HouseNotFoundException("House with id =" + newHouseName + " does not exists"));
            logger.info("House was found setting the house");
            flat.setHouse(house);
        }
        if (price != null && price > 0) {
            logger.info("Price is correct and set!");
            flat.setPrice(price);
        }
        if (hasBalcony != null) {
            logger.info("HasBalcony was correct and set!");
            flat.setHasBalcony(hasBalcony);
        }
        return flatRepository.save(flat);
    }

    public Flat createNewFlatToExistingHouse(String name,
                               Double coordinateX,
                               Integer coordinateY,
                               Integer area,
                               Long numberOfRooms,
                               String furnish,
                               String view,
                               String transport,
                               String houseName,
                               Long price,
                               Boolean hasBalcony) throws HouseNotFoundException {
        logger.info("Creating a new flat to House with id =" + houseName);
        House house = houseRepository.findById(houseName).orElseThrow(() -> new HouseNotFoundException("House with id " + houseName + " does not exists"));
        if (name.isEmpty())  throw new ValidationException("House name cannot be empty!");
        if (coordinateX == null || coordinateY == null  || coordinateX > 548) throw new ValidationException("Coordinates cannot be null | Coordinate X must be smaller than 548");
        if (area == null || area <= 0 || area > 527) throw new ValidationException("Area invalid! Must be greater than 0 and less than 527!");
        if (numberOfRooms == null || numberOfRooms <= 0) throw new ValidationException("Number of rooms invalid! Must be greater than 0.");
        if (price == null || price <= 0) throw new ValidationException("Price invalid: must be greater than 0");
        if (hasBalcony == null) throw new ValidationException("boolean hasBalcony can not be null");

        Flat flat = new Flat(name, new Coordinates(coordinateX, coordinateY),
                area, numberOfRooms, Furnish.valueOf(furnish), View.valueOf(view),
                Transport.valueOf(transport), house, price, hasBalcony);
        logger.info("Flat was successfully created");
        return flatRepository.save(flat);
    }

    public List<Flat> getAllFlats(int pageNumber){
        logger.info("Getting all flats. Page" + pageNumber);
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        return flatRepository.findAll(pageRequest).getContent();
    }

    public List<Flat> getFlatsWithFilterAndPage(List<FilterCriteria> filterCriteriaList, int pageNumber){
        logger.info("Filtering the flats");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Specification<Flat> specification = FlatSpecification.filterByMultipleCriteria(filterCriteriaList, null);
        return flatRepository.findAll(specification, pageRequest);
    }

    public List<Flat> getFlatsWithFilterSortAndPage(List<FilterCriteria> filterCriteriaList, int pageNumber, String sortField, SortDirection sortDirection){
        logger.info("Filtering and sorting the flats");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Sort sort = pageRequest.getSort().and(Sort.by(sortField));
        if (sortDirection.equals(SortDirection.ASCENDING)){
            sort.ascending();
        }
        else{
            sort.descending();
        }
        Specification<Flat> specification = FlatSpecification.filterByMultipleCriteria(filterCriteriaList, sort);
        return flatRepository.findAll(specification, pageRequest);
    }

    public void deleteFlatById(int id) throws FlatNotFoundException {
        logger.info("Deleting flat by id =" + id);
        Flat flat = flatRepository.findById(id).orElseThrow(() -> new FlatNotFoundException("Flat with id = " + id + " does not exists"));
        flatRepository.delete(flat);
    }


    public void deleteFlatsByHouseId(String houseName) throws IncorrectParametersException, HouseNotFoundException {
        logger.info("Deleting flats by house id = " + houseName);
        if (houseName.isEmpty()) throw new IncorrectParametersException("House name can't be empty!");
        houseName = houseName.strip();
        String finalHouseName = houseName;
        House house = houseRepository.findById(houseName).orElseThrow(() -> new HouseNotFoundException("House with name " + finalHouseName + " was not found"));
        flatRepository.deleteByHouseEqualsAllIgnoreCase(house);
    }

    public Integer getFlatsWithSameHouse(String houseName) throws IncorrectParametersException, HouseNotFoundException {
        logger.info("Getting flats of the house =" +houseName);
        if (houseName.isEmpty()) throw new IncorrectParametersException("House name can't be empty");
        houseName = houseName.strip();
        String finalHouseName = houseName;
        House house = houseRepository.findById(houseName).orElseThrow(() -> new HouseNotFoundException("House with name " + finalHouseName + " was not found!"));
        return flatRepository.findByHouseEquals(house).size();

    }

    public Long getFlatsWithLessNumberOfRooms(Integer minRooms) {
        logger.info("Gettin flat count with rooms less than " + minRooms);
        return flatRepository.flatCountWithNumberOfRoomsLessThan(minRooms);
    }

    public Flat getCheapOrExpensiveFlatWithOrWithoutBalcony(String price, String balcony) {
        if (price.equals("cheap") && balcony.equals("yes")){
            return flatRepository.findFirstByHasBalconyTrueOrderByPriceAsc();
        }
        if (price.equals("expensive") && balcony.equals("yes")){
            return flatRepository.findFirstByHasBalconyTrueOrderByPriceDesc();
        }
        if (price.equals("cheap") && balcony.equals("no")){
            return flatRepository.findFirstByHasBalconyFalseOrderByPriceAsc();
        }
        return flatRepository.findFirstByHasBalconyFalseOrderByPriceDesc();
    }
}
