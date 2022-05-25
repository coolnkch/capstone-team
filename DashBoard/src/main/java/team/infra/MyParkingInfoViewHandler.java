package team.infra;

import team.domain.*;
import team.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyParkingInfoViewHandler {


    @Autowired
    private MyParkingInfoRepository myParkingInfoRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenEnterRequested_then_CREATE_1 (@Payload EnterRequested enterRequested) {
        try {

            if (!enterRequested.validate()) return;

            // view 객체 생성
            MyParkingInfo myParkingInfo = new MyParkingInfo();
            // view 객체에 이벤트의 Value 를 set 함
            myParkingInfo.setId(enterRequested.getId());
            myParkingInfo.setCarNo(enterRequested.getCarNo());
            myParkingInfo.setInTime(enterRequested.getInTime());
            myParkingInfo.setCarStatus("TAKEN");
            // view 레파지 토리에 save
            myParkingInfoRepository.save(myParkingInfo);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // @StreamListener(KafkaProcessor.INPUT)
    // public void whenParkingAreaAdded_then_UPDATE_1(@Payload ParkingAreaAdded parkingAreaAdded) {
    //     try {
    //         if (!parkingAreaAdded.validate()) return;
    //             // view 객체 조회
    //         Optional<MyParkingInfo> myParkingInfoOptional = myParkingInfoRepository.findById(parkingAreaAdded.getParkingId());

    //         if( myParkingInfoOptional.isPresent()) {
    //              MyParkingInfo myParkingInfo = myParkingInfoOptional.get();
    //         // view 객체에 이벤트의 eventDirectValue 를 set 함
    //              myParkingInfo.setParkingZoneNo(parkingAreaAdded.getParkingZoneNo());
    //             // view 레파지 토리에 save
    //              myParkingInfoRepository.save(myParkingInfo);
    //             }


    //     }catch (Exception e){
    //         e.printStackTrace();
    //     }
    // }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenExitRequested_then_UPDATE_2(@Payload ExitRequested exitRequested) {
        try {
            if (!exitRequested.validate()) return;
                // view 객체 조회
            Optional<MyParkingInfo> myParkingInfoOptional = myParkingInfoRepository.findById(exitRequested.getId());

            if( myParkingInfoOptional.isPresent()) {
                 MyParkingInfo myParkingInfo = myParkingInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 myParkingInfo.setOutTime(exitRequested.getOutTime());
                 myParkingInfo.setCarStatus("VACANT");
                // view 레파지 토리에 save
                 myParkingInfoRepository.save(myParkingInfo);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaid_then_UPDATE_3(@Payload Paid paid) {
        try {
            if (!paid.validate()) return;
                // view 객체 조회
            Optional<MyParkingInfo> myParkingInfoOptional = myParkingInfoRepository.findById(paid.getId());

            if( myParkingInfoOptional.isPresent()) {
                 MyParkingInfo myParkingInfo = myParkingInfoOptional.get();
            // view 객체에 이벤트의 eventDirectValue 를 set 함
                 myParkingInfo.setPrice(paid.getPrice());
                 myParkingInfo.setPaymentStatus("PAID");
                // view 레파지 토리에 save
                 myParkingInfoRepository.save(myParkingInfo);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // @StreamListener(KafkaProcessor.INPUT)
    // public void whenParkingAreaDeleted_then_UPDATE_4(@Payload ParkingAreaDeleted parkingAreaDeleted) {
    //     try {
    //         if (!parkingAreaDeleted.validate()) return;
    //             // view 객체 조회
    //         Optional<MyParkingInfo> myParkingInfoOptional = myParkingInfoRepository.findById(parkingAreaDeleted.getParkingId());

    //         if( myParkingInfoOptional.isPresent()) {
    //              MyParkingInfo myParkingInfo = myParkingInfoOptional.get();
    //         // view 객체에 이벤트의 eventDirectValue 를 set 함
    //              myParkingInfo.setCarStatus("EXIT");
    //             // view 레파지 토리에 save
    //              myParkingInfoRepository.save(myParkingInfo);
    //             }


    //     }catch (Exception e){
    //         e.printStackTrace();
    //     }
    // }

}

