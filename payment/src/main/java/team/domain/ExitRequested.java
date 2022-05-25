package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class ExitRequested extends AbstractEvent {

    private Long id;
    private String carNo;
    private String parkAreaId;
    private Date inTime;
    private Date outTime;

}

