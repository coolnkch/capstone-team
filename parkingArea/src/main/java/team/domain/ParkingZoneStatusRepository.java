package team.domain;

import team.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="parkingZoneStatuses", path="parkingZoneStatuses")
public interface ParkingZoneStatusRepository extends PagingAndSortingRepository<ParkingZoneStatus, String>{


}
