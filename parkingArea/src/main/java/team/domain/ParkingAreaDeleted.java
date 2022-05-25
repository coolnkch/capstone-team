package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class ParkingAreaDeleted extends AbstractEvent {

    private Long id;
    private String status;
    private String parkingId;

    public ParkingAreaDeleted(){
        super();
    }
}
