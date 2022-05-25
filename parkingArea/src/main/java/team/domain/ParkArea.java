package team.domain;

import team.domain.ParkingAreaAdded;
import team.domain.ParkingAreaDeleted;
import team.ParkingAreaApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="ParkArea_table")
@Data

public class ParkArea  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    
    private String status;
    
    
    private String parkingZoneNo;
    
    
    private Long parkingId;

    @PostPersist
    public void onPostPersist(){
        ParkingAreaAdded parkingAreaAdded = new ParkingAreaAdded();
        BeanUtils.copyProperties(this, parkingAreaAdded);
        parkingAreaAdded.publishAfterCommit();

    }
    @PostUpdate
    public void onPostUpdate(){
        ParkingAreaDeleted parkingAreaDeleted = new ParkingAreaDeleted();
        BeanUtils.copyProperties(this, parkingAreaDeleted);
        parkingAreaDeleted.publishAfterCommit();

    }


    public static ParkAreaRepository repository(){
        ParkAreaRepository parkAreaRepository = ParkingAreaApplication.applicationContext.getBean(ParkAreaRepository.class);
        return parkAreaRepository;
    }


    public static void carEnter(EnterRequested enterRequested){

        for(ParkArea parkArea : repository().findAll()) {
            if(parkArea.getStatus() == null || "VACANT".equals(parkArea.getStatus()) {
                parkArea.status ="TAKEN";
                parkArea.parkingId = enterRequested.getId();
                repository().save(parkArea);
                break;
            }
        }

    }
    public static void carExit(Paid paid){

        repository().findById(paid.getParkingId()).ifPresent(parkArea -> {
            parkArea.setStatus("VACANT");
            repository().save(parkArea);
        });

    }


}
