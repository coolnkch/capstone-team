package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class ParkingAreaAdded extends AbstractEvent {

    private Long id;
    private String status;
    private String parkingZoneNo;
    private Long parkingId;

    public ParkingAreaAdded(){
        super();
    }
}
