package team.domain;

import team.domain.Paid;
import team.PaymentApplication;
import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Payment_table")
@Data

public class Payment  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    private Long id;
    
    
    private Long parkingId;
    
    
    private String price;
    
    
    private Date inTime;
    
    
    private Date outTime;
    
    
    private String paymentStatus;
    
    
    private String parkAreaId;

    @PostPersist
    public void onPostPersist(){
        Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();

    }


    public static PaymentRepository repository(){
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }


    public static void paymentRequest(ExitRequested exitRequested){

        Payment payment = new Payment();
        
        payment.parkingId = exitRequested.getId();
        payment.inTime = exitRequested.getInTime();
        payment.outTime = exitRequested.getOutTime();
        payment.parkAreaId = exitRequested.getParkAreaId();
        payment.paymentStatus = "PAID";
        payment.price = Long.toString(payment.outTime.getTime()  - payment.inTime.getTime());

        repository().save(payment);
    }


}
