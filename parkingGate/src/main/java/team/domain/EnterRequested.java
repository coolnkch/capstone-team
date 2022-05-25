package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class EnterRequested extends AbstractEvent {

    private Long id;
    private String carNo;
    private Date inTime;

    public EnterRequested(){
        super();
    }
}
