package team.common;


import team.ParkingAreaApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ParkingAreaApplication.class })
public class CucumberSpingConfiguration {
    
}
