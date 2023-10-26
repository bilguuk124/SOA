package itmo.MainService.repository;

import itmo.MainService.entity.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, String> {
    @Query("select h from House h")
    List<House> getAll(Pageable pageable);

    List<House> findAll(Specification<House> specification, Pageable pageable);

}
