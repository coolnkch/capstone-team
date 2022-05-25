package team.domain;

import team.domain.EnterRequested;
import team.domain.ExitRequested;
import team.ParkingGateApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;
import team.external.ParkingZoneStatus;
import team.external.ParkingZoneStatusService;


@Entity
@Table(name="Parking_table")
@Data
public class Parking  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    private Long id;
    
    
    private String carNo;
    
    
    private Date inTime;
    
    
    private Date outTime;
    
    
    private String carStatus;
    
    
    private String parkAreaId;

    @PostPersist
    public void onPostPersist(){
        // Get request from ParkingZoneStatus
        team.external.ParkingZoneStatus parkingZoneStatus =
            Application.applicationContext.getBean(team.external.ParkingZoneStatusService.class)
            .getParkingZoneStatus("A");

        if(!"Y".equals(parkingZoneStatus.getAvailableStatus())){
            throw new RuntimeException("NO Paring Zone");
        }

        EnterRequested enterRequested = new EnterRequested();
        this.carStatus = "TAKEN";
        this.inTime = new Date();
        BeanUtils.copyProperties(this, enterRequested);
        enterRequested.publishAfterCommit();

    }
    @PostUpdate
    public void onPostUpdate(){
        ExitRequested exitRequested = new ExitRequested();
        this.outTime = new Date();
        this.carStatus = "VACANT";
        // repository().findById(this.id).ifPresent(parking-> {
        //     this.inTime = myParkingInfoOptional.getInTime();
        //     this.carNo = myParkingInfoOptional.getCarNo();
        //     this.parkAreaId = myParkingInfoOptional.getCarNo();
        //     this.outTime = new Date();
        //     this.carStatus = "VACANT";
        // });
        BeanUtils.copyProperties(this, exitRequested);
        exitRequested.publishAfterCommit();

    }


    public static ParkingRepository repository(){
        ParkingRepository parkingRepository = ParkingGateApplication.applicationContext.getBean(ParkingRepository.class);
        return parkingRepository;
    }




}
