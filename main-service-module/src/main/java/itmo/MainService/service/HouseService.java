package itmo.MainService.service;

import itmo.MainService.entity.House;
import itmo.MainService.exception.HouseExistsException;
import itmo.MainService.exception.HouseNotFoundException;
import itmo.MainService.repository.HouseRepository;
import itmo.MainService.utility.FilterCriteria;
import itmo.MainService.utility.HouseSpecification;
import itmo.MainService.utility.SortDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final int PAGE_SIZE = 10;
    private final Logger logger = LogManager.getLogger(HouseService.class);

    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }


    public House getHouseByName(String name) throws HouseNotFoundException {
        logger.info("Getting house by name "+ name);
       return houseRepository.findById(name).orElseThrow(() -> new HouseNotFoundException("House with id = " + name + " does not exists"));

    }

    public House createNewHouse(String name, Integer year, long numberOfFloors) throws HouseExistsException {
        logger.info("Creating a new house with name " + name);
        if (houseRepository.existsById(name)){
            logger.error("A house with this name " + name + " already exists");
            throw new HouseExistsException("id= " + name + " is taken");
        }
        return houseRepository.save(new House(name, year, numberOfFloors));
    }

    public List<House> getAllHouses(int pageNumber){
        logger.info("Getting all houses paged");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        return houseRepository.getAll(pageRequest);
    }

    public House updateHouseByName(String name, Integer year, long numberOfFloors) throws HouseNotFoundException, HouseExistsException {
        logger.info("Updating house with name =" + name);
        House house = houseRepository.findById(name).orElseThrow(() -> new HouseNotFoundException("House with id = " + name + " does not exists"));
        house.setYear(year);
        house.setNumberOfFloors(numberOfFloors);
        return houseRepository.save(house);
    }

    public void deleteHouse(String name) throws HouseNotFoundException {
        logger.info("Deleting a house with name =" +name);
        houseRepository.delete(houseRepository.findById(name).orElseThrow(() -> new HouseNotFoundException("House with id = " + name + " does not exists")));
    }
    public List<House> getHousesWithFilterAndPage(List<FilterCriteria> filterCriteriaList, int pageNumber){
        logger.info("Getting houses with filter and page");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Specification<House> specification = HouseSpecification.filterByMultipleCriteria(filterCriteriaList, null);
        return houseRepository.findAll(specification, pageRequest);
    }

    public List<House> getHousesWithFilterSortAndPage(List<FilterCriteria> filterCriteriaList, int pageNumber, String sortField, SortDirection sortDirection){
        logger.info("Getting houses with filter and sort");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Sort sort = pageRequest.getSort().and(Sort.by(sortField));
        if (sortDirection.equals(SortDirection.ASCENDING)){
            logger.info("Ascending SORT");
            sort = sort.ascending();
        }
        else{
            logger.info("Descending SORT");
            sort = sort.descending();
        }
        Specification<House> specification = HouseSpecification.filterByMultipleCriteria(filterCriteriaList, sort);
        return houseRepository.findAll(specification, pageRequest);
    }
}
