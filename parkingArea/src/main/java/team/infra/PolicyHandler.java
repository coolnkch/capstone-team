package team.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;

import team.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import team.domain.*;


@Service
public class PolicyHandler{
    @Autowired ParkAreaRepository parkAreaRepository;
    @Autowired ParkingZoneStatusRepository parkingZoneStatusRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEnterRequested_CarEnter(@Payload EnterRequested enterRequested){

        if(!enterRequested.validate()) return;
        EnterRequested event = enterRequested;
        System.out.println("\n\n##### listener CarEnter : " + enterRequested.toJson() + "\n\n");


        

        // Sample Logic //
        ParkArea.carEnter(event);
        

        

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_CarExit(@Payload Paid paid){

        if(!paid.validate()) return;
        Paid event = paid;
        System.out.println("\n\n##### listener CarExit : " + paid.toJson() + "\n\n");


        

        // Sample Logic //
        ParkArea.carExit(event);
        

        

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverEnterRequested_ParkStatusUpdate(@Payload EnterRequested enterRequested){

        if(!enterRequested.validate()) return;
        EnterRequested event = enterRequested;
        System.out.println("\n\n##### listener ParkStatusUpdate : " + enterRequested.toJson() + "\n\n");


        

        // Sample Logic //
        ParkingZoneStatus.parkStatusUpdate(event);
        

        

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverExitRequested_ParkStatusUpdate(@Payload ExitRequested exitRequested){

        if(!exitRequested.validate()) return;
        ExitRequested event = exitRequested;
        System.out.println("\n\n##### listener ParkStatusUpdate : " + exitRequested.toJson() + "\n\n");


        

        // Sample Logic //
        ParkingZoneStatus.parkStatusUpdate(event);
        

        

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverParkingAreaAdded_ParkStatusUpdate(@Payload ParkingAreaAdded parkingAreaAdded){

        if(!parkingAreaAdded.validate()) return;
        ParkingAreaAdded event = parkingAreaAdded;
        System.out.println("\n\n##### listener ParkStatusUpdate : " + parkingAreaAdded.toJson() + "\n\n");


        

        // Sample Logic //
        ParkingZoneStatus.parkStatusUpdate(event);
        

        

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverParkingAreaDeleted_ParkStatusUpdate(@Payload ParkingAreaDeleted parkingAreaDeleted){

        if(!parkingAreaDeleted.validate()) return;
        ParkingAreaDeleted event = parkingAreaDeleted;
        System.out.println("\n\n##### listener ParkStatusUpdate : " + parkingAreaDeleted.toJson() + "\n\n");


        

        // Sample Logic //
        ParkingZoneStatus.parkStatusUpdate(event);
        

        

    }


}


