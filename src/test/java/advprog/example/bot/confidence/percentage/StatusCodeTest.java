package advprog.example.bot.confidence.percentage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class StatusCodeTest {

    @Test
    public void getStatusTest() {
        assertEquals(StatusCode.UNKNOWN_HOST, StatusCode.getStatus(1));
        assertEquals(StatusCode.SUCCESS, StatusCode.getStatus(200));
        assertEquals(StatusCode.BAD_REQUEST, StatusCode.getStatus(400));
        assertEquals(StatusCode.UNAUTHORIZED, StatusCode.getStatus(401));
        assertEquals(StatusCode.FORBIDDEN, StatusCode.getStatus(403));
        assertEquals(StatusCode.NOT_FOUND, StatusCode.getStatus(404));
        assertEquals(StatusCode.FILE_TOO_BIG, StatusCode.getStatus(413));
        assertEquals(StatusCode.UPLOAD_LIMITED, StatusCode.getStatus(429));
        assertEquals(StatusCode.INTERNAL_SERVER_ERROR, StatusCode.getStatus(500));
        assertEquals(StatusCode.SERVICE_UNAVAILABLE, StatusCode.getStatus(502));
        assertEquals(StatusCode.UNKNOWN_ERROR, StatusCode.getStatus(-1));
    }

    @Test
    public void getDescriptionTest() {
        assertEquals("Couldn't find api.imgur.com, "
                        + "are you connected to the internet?",
                StatusCode.UNKNOWN_HOST.getDescription());
    }

    @Test
    public void toStringTest() {
        assertEquals("StatusCode - Name: UNKNOWN_HOST - "
                        + "HttpCode: 1 - Description: Couldn't find api.imgur.com, "
                        + "are you connected to the internet?",
                StatusCode.UNKNOWN_HOST.toString());
    }


}