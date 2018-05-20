package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class DiceControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private DiceController diceController;

    @Test
    void testContextLoads() {
        assertNotNull(diceController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = diceController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());
    }

    @Test
    void testHandleTextMessageEventCoin() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/coin");

        TextMessage reply = diceController.handleTextMessageEvent(event);
        SortedSet<String> sample = new TreeSet<String>(Arrays.asList("head", "tail"));
        assertTrue(sample.contains(reply.getText()));
    }

    @Test
    void testHandleTextMessageEventDadu() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/roll 5d5");

        TextMessage reply = diceController.handleTextMessageEvent(event);
        //1. Result: == 1|| 2. 5d5 == 1|| 3. (x, x, x, x, x) == 5
        String[] format = reply.getText().split(" ");
        //Test Output
        assertTrue(format.length == 7);;

        List<String> rolledNumber = new ArrayList<String>();
        String[] rolledNumberStrRaw = Arrays.copyOfRange(format, 2, format.length-1);
        for (int i = 0; i < rolledNumberStrRaw.length; i++) {
            if (rolledNumberStrRaw[i].length() == 3) {
                rolledNumber.add(rolledNumberStrRaw[i].substring(1,2));
            } else {
                rolledNumber.add(rolledNumberStrRaw[i].substring(0,1));
            }
        }

        for (int num = 0; num < rolledNumber.size(); num++) {
            assertTrue(Integer.parseInt(rolledNumber.get(num)) > 0
                && Integer.parseInt(rolledNumber.get(num)) <= 5);
        }
    }

    @Test
    void testDiceOutputBuilder() {
        List<Integer> sample = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
        String expected = "Result: 5d5 (1, 2, 3, 4, 5)";
        assertEquals(expected, DiceController.diceOutputBuilder(sample, 5, 5));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        diceController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testDiceRollOutputRangeisCorrect() {
        List<Integer> set = diceController.diceRolling(3,6);
        for (Integer i : set) {
            assertTrue(i >= 1 && i <= 6);
        }
    }

    @Test
    void testCoinOutput() {
        int loop = 0;
        String[] possibilities = {"head", "tail"};
        int trialHead = 0;
        int trialTail = 1;
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            set.add(diceController.coinRolling());
        }
        assertEquals(set.size(), 2);
        for (String s : set) {
            assertEquals(s, possibilities[loop++]);
        }

    }

    @Test
    void testMultiRollOutput() {
        int attempt = 100;
        int xTimes = 10;
        int ySided = 6;
        List<Integer> testResult = diceController.multiRolling(100, 10, 6);
        List<Integer> sampleTrue = new ArrayList<Integer>();
        List<Integer> sampleFalse = new ArrayList<Integer>();
        sampleTrue.addAll(new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6)));
        sampleFalse.addAll(new ArrayList<Integer>(Arrays.asList(7,8,9,10)));

        //Test invalid subPossibilities
        testResult.removeAll(sampleFalse);
        assertEquals(100 * 10, testResult.size());

        //Test valid subPossibilities
        testResult.removeAll(sampleTrue);
        assertEquals(0, testResult.size());
    }

    @Test
    void testIsLuckyOutput() {
        int testLuckyNumber = 7;
        String output = diceController.isLuckyNumber(testLuckyNumber, 1000, 7);
        String[] format = output.split(" ");

        //Test 99% True
        String appear1 = format[format.length-1];
        assertEquals(output.substring(0, output.length()-appear1.length()-1), testLuckyNumber + " appears");

        //Test Possibilities of Lucky Number
        int appear2 = Integer.parseInt(format[format.length-1]);
        assertTrue(appear2 > 0 && appear2 <= 1000);

        //Test 0% True
        String output2 = diceController.isLuckyNumber(testLuckyNumber, 1, 10000);
        assertEquals(output2, "I'm not lucky");

    }

    @Test
    void testMultiRollOutputBuilder() {
        int attempt = 2;
        List<Integer> sampleOutput = new ArrayList<Integer>();
        sampleOutput.addAll(new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,5,4,3,2,1)));
        String output = diceController.multiRollOutputBuilder(attempt, 5, 5, sampleOutput);
        String lines[] = output.split("\\r?\\n");
        assertEquals(lines[0], "5d5 (1, 2, 3, 4, 5)");
        assertEquals(lines[1], "5d5 (5, 4, 3, 2, 1)");
    }

}