package advprog.TopLaughers.controller;

import static advprog.TopLaughers.EventTestUtil.whenCall;
import static java.util.Collections.singletonList;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import org.apache.catalina.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import advprog.TopLaughers.EventTestUtil;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
class TopLaughersControllerTest {

    @Mock
    private LineMessagingClient lineMessagingClient;
    @InjectMocks
    private TopLaughersController underTest;

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TopLaughersController topLaughersController;


    @Test
    void testContextLoads() {
        assertNotNull(topLaughersController);
    }

    @Test
    void handleTextMessageEvent() throws ExecutionException, InterruptedException{
//        final UserProfileResponse mockUserProfileResponse =
//                new UserProfileResponse("displayName", "userId", "pictureUrl", "statusMessage");
//
//        MessageEvent<TextMessageContent> event =
//                EventTestUtil.createDummyTextMessage("/toplaughers");
//
//        when(underTest.handleTextMessageEvent(event)).getMock();
//
//        // mock line bot api client response
        when(lineMessagingClient.replyMessage(new ReplyMessage(
                "replyToken", new TextMessage("1.        \n2.\n3.\n4.\n5.")
        ))).thenReturn(CompletableFuture.completedFuture(
                new BotApiResponse("ok", Collections.emptyList())
        ));
        CompletableFuture<UserProfileResponse> userMock = CompletableFuture.completedFuture(new UserProfileResponse("Endrawan",
                "userId",
                "picture", "status"));
//
//        underTest.handleTextMessageEvent(event);
//
//        // confirm replyMessage is called with following parameter
//        verify(lineMessagingClient).replyMessage(new ReplyMessage(
//                "replyToken", singletonList(new TextMessage("1.        \n2.\n3.\n4.\n5."))
//        ));
    }


    @Test
    void getSenderName() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyGroupMessage("/toplaughers", "userId");

    }

    @Test
    void getGroomUserId() {
    }

    @Test
    void putLaughter() {
    }

    @Test
    void printLaughers() {
        Map<String, Integer> totalLaughter = new TreeMap<>();
        totalLaughter.put("id", 30);
        Map<String, Integer> map = new TreeMap<>();
        map.put("A", 5);
        map.put("B", 10);
        map.put("C", 15);
        SortedSet<Map.Entry<String,Integer>> result = topLaughersController.entriesSortedByValues(map);
        assertEquals("1. C  (50%) \n2. B  (33%) \n3. A  (17%) \n4. \n5. \n", topLaughersController.printLaughers(result, "id", totalLaughter));
    }

    @Test
    void entriesSortedByValues() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("A", 5);
        map.put("B", 10);
        map.put("C", 15);
        SortedSet<Map.Entry<String,Integer>> result = topLaughersController.entriesSortedByValues(map);
        assertEquals("[C=15, B=10, A=5]", result.toString());
    }

    @Test
    void toPercentage() {
        int myLaughter = 5;
        int totalLaughter = 10;
        assertEquals(50, topLaughersController.toPercentage(myLaughter, totalLaughter));
    }


}