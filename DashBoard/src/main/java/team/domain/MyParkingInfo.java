package team.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name="MyParkingInfo_table")
@Data
public class MyParkingInfo {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String carNo;
        private Date inTime;
        private Date outTime;
        private String carStatus;
        private String price;
        private String paymentStatus;
        private String parkingAreaId;
        private String parkingZoneNo;


}