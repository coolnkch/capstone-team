package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class ParkingHateoasProcessor implements RepresentationModelProcessor<EntityModel<Parking>>  {

    @Override
    public EntityModel<Parking> process(EntityModel<Parking> model) {
        
        return model;
    }
    
}

