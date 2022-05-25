package team.common;


import team.ParkingGateApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ParkingGateApplication.class })
public class CucumberSpingConfiguration {
    
}
