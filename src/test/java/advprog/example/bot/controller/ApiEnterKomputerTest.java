package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ApiEnterKomputerTest {

    ApiEnterKomputer apiEnterKomputer = new ApiEnterKomputer();

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }


    @Test
    void testContextLoads() {
        assertNotNull(apiEnterKomputer);
    }

    @Test
    void testGetDetailByCategoryAndName() {
        assertEquals("detail item by category and name", apiEnterKomputer
                .getDetailItemByCategoryAndName("category","name"));
    }
}