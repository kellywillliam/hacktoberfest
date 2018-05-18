package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class WeatherControllerTest {

    WeatherController control = new WeatherController();

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(WeatherController.class);
    }

    @Test
    void tempConverterTest() {
        assertEquals(control.tempConverter("Kelvin", 20), 0);
    }

    @Test
    void windConverter() {
        assertEquals(control.windConverter("mph", 20), 0);
    }

    @Test
    void weatherConditionTest() {
        assertEquals(control.weatherCondition(), null);
    }

    @Test
    void findLocationTest() {
        assertEquals(control.findLocation(), null);
    }

}
