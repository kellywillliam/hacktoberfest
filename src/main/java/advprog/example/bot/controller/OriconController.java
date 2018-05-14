package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;

import java.util.ArrayList;
import java.util.logging.Logger;

public class OriconController {
	private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		String replyText = contentText.replace("/oricon", "");
		return new TextMessage(replyText.substring(1));
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String makeGetCall() {
		return null;
	}

	public static ArrayList<String> screenScrapeGetComics(String html) {
		return null;
	}
}
