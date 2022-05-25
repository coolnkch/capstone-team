package team.domain;

import team.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="parkAreas", path="parkAreas")
public interface ParkAreaRepository extends PagingAndSortingRepository<ParkArea, Long>{

	void findAllByStatus(String string);


}
