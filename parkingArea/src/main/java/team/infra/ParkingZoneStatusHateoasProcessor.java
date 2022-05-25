package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class ParkingZoneStatusHateoasProcessor implements RepresentationModelProcessor<EntityModel<ParkingZoneStatus>>  {

    @Override
    public EntityModel<ParkingZoneStatus> process(EntityModel<ParkingZoneStatus> model) {
        
        return model;
    }
    
}

