package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class EnterRequested extends AbstractEvent {

    private Long id;
    private String carNo;
    private Date inTime;

}

