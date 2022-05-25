package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.Date;
import lombok.Data;

@Data
public class Paid extends AbstractEvent {

    private Long id;
    private Long parkingId;
    private String price;
    private String paymentStatus;

    public Paid(){
        super();
    }
}
