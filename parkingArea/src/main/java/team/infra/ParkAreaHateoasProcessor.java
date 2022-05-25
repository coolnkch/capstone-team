package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class ParkAreaHateoasProcessor implements RepresentationModelProcessor<EntityModel<ParkArea>>  {

    @Override
    public EntityModel<ParkArea> process(EntityModel<ParkArea> model) {
        
        return model;
    }
    
}

