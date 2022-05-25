package team.external;

import lombok.Data;
import java.util.Date;
@Data
public class ParkingZoneStatus {

    private String id;
    private Long totalParkingSpotsnumberOfCars;
    private Long totalParkingSpots;
    private String availableStatus;

}
