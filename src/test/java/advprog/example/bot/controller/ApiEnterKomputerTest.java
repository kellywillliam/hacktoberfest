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
    void testSuccessGetDetailByCategoryAndName() {
        assertEquals("(1) iGame nVidia Geforce GTX 1080 8GB DDR5X - X-TOP-8G -"
                + " Triple Fan ( Garansi 3 Bln ) - 9900000", apiEnterKomputer
                .getDetailItemByCategoryAndName("vga","iGame Nvidia Geforce GTX 1080"));
        assertEquals("No items found!", apiEnterKomputer
                .getDetailItemByCategoryAndName("vga","barang tidak ada"));
    }

    @Test
    void testFailedGetDetailByCategoryAndName() {
        //test wrong category
        assertEquals("No such category!", apiEnterKomputer
                .getDetailItemByCategoryAndName("zzz","iGame Nvidia Geforce GTX 1080"));
    }
}