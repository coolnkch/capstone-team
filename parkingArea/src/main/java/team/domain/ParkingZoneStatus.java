package team.domain;

import team.ParkingAreaApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="ParkingZoneStatus_table")
@Data

public class ParkingZoneStatus  {
    
    @Id
    private String id;
    
    private Long numberOfCars;
    
    
    private Long totalParkingSpots;
    
    
    private String availableStatus;
    

    public static ParkingZoneStatusRepository repository(){
        ParkingZoneStatusRepository parkingZoneStatusRepository = ParkingAreaApplication.applicationContext.getBean(ParkingZoneStatusRepository.class);
        return parkingZoneStatusRepository;
    }


    public static void parkStatusUpdate(EnterRequested enterRequested){

        repository().findById("A").ifPresent(parkingZoneStatus->{

            parkingZoneStatus.numberOfCars ++;

            if(parkingZoneStatus.totalParkingSpots > parkingZoneStatus.numberOfCars) {
                parkingZoneStatus.availableStatus = "N";
            } else {
                parkingZoneStatus.availableStatus = "Y";
            }

            repository().save(parkingZoneStatus);
        });


    }
    public static void parkStatusUpdate(ExitRequested exitRequested){
        
        repository().findById("A").ifPresent(parkingZoneStatus->{

            parkingZoneStatus.numberOfCars--;

            if(parkingZoneStatus.totalParkingSpots > parkingZoneStatus.numberOfCars) {
                parkingZoneStatus.availableStatus = "N";
            } else {
                parkingZoneStatus.availableStatus = "Y";
            }

            repository().save(parkingZoneStatus);
        });

    }
    public static void parkStatusUpdate(ParkingAreaAdded parkingAreaAdded){
        repository().findById("A").ifPresent(parkingZoneStatus->{

            parkingZoneStatus.totalParkingSpots ++;

            if(parkingZoneStatus.totalParkingSpots > parkingZoneStatus.numberOfCars) {
                parkingZoneStatus.availableStatus = "N";
            } else {
                parkingZoneStatus.availableStatus = "Y";
            }

            repository().save(parkingZoneStatus);
        });

    }
    public static void parkStatusUpdate(ParkingAreaDeleted parkingAreaDeleted){
        repository().findById("A").ifPresent(parkingZoneStatus->{

            parkingZoneStatus.totalParkingSpots --;
            if(parkingZoneStatus.totalParkingSpots > parkingZoneStatus.numberOfCars) {
                parkingZoneStatus.availableStatus = "N";
            } else {
                parkingZoneStatus.availableStatus = "Y";
            }

            repository().save(parkingZoneStatus);
        });

    }


}
