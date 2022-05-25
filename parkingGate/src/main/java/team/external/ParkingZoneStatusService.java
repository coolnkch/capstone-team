package team.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="parkingArea", url="${api.path.parkingArea }")
public interface ParkingZoneStatusService {
    
    @RequestMapping(method= RequestMethod.GET, path="/parkingZoneStatuses/{id}")
    public ParkingZoneStatus getParkingZoneStatus(@PathVariable("id") String id);

}