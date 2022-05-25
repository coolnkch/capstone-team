package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.Data;
import java.util.Date;
@Data
public class Paid extends AbstractEvent {

    private Long id;
    private String parkingId;
    private String price;
    private String paymentStatus;

}

