package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jsoup.nodes.Document;

import advprog.example.bot.EventTestUtil;
import advprog.example.bot.controller.OriconController;
import junit.framework.TestCase;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.jsoup.select.Elements;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class OriconControllerTest extends TestCase {

	@Autowired
	// private OriconController = oriconController;
	OriconController oriconController = new OriconController();

	@Test
	public void testContextLoads() {
		assertNotNull(oriconController);
	}

	@Test
	public void testHandleTextMessageEvent() {

		MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage("/oricon comic 2018-05-14");
		TextMessage reply = oriconController.handleTextMessageEvent(event);

		event = EventTestUtil.createDummyTextMessage("/oricon comic 2018-05-17");
		reply = oriconController.handleTextMessageEvent(event);
		assertEquals("Ranking does not available at this date, Please Input a date on Monday", reply.getText());

		event = EventTestUtil.createDummyTextMessage("wrong input");
		reply = oriconController.handleTextMessageEvent(event);
		assertEquals("Wrong Input,Please Input with correct format '/oricon comic YYYY/MM/DD'", reply.getText());
	}

	@Test
	public void testHandleDefaultMessage() {
		Event event = mock(Event.class);
		oriconController.handleDefaultMessage(event);

		verify(event, atLeastOnce()).getSource();
		verify(event, atLeastOnce()).getTimestamp();
	}

	@Test
	public void testMakeGetCall() {
		assertNotNull(oriconController.makeGetCall("2018-05-14"));
		assertEquals(null, oriconController.makeGetCall("2018-05-15"));
	}

	@Test
	public void testshowComics() {
		oriconController.showComics("2018-05-14");
	}

	@Test
	public void testscreenScrapeGetComics() {
		Document doc = oriconController.makeGetCall("2018-05-14");
		Elements elements = oriconController.screenScrapeGetComics(doc);

		doc = oriconController.makeGetCall("2018-05-15");
		elements = oriconController.screenScrapeGetComics(doc);
		assertEquals(null, elements);
	}
}
