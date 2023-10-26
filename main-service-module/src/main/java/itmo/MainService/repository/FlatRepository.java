package itmo.MainService.repository;

import itmo.MainService.entity.Flat;
import itmo.MainService.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Integer> {
    Flat findFirstByHasBalconyFalseOrderByPriceDesc();
    Flat findFirstByHasBalconyFalseOrderByPriceAsc();
    Flat findFirstByHasBalconyTrueOrderByPriceAsc();
    Flat findFirstByHasBalconyTrueOrderByPriceDesc();
    @Query("select count(f) from Flat f where f.numberOfRooms < ?1")
    long flatCountWithNumberOfRoomsLessThan(@NonNull long numberOfRooms);
    List<Flat> findByHouseEquals(House house);
    long deleteByHouseEqualsAllIgnoreCase(@NonNull House house);
   List<Flat> findAll(Specification<Flat> specification, Pageable pageable);
}
