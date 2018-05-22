package advprog.example.bot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class BikunController {

	private static final Logger LOGGER = Logger.getLogger(BikunController.class.getName());

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.startsWith("/echo")) {
			return new TextMessage("echo from bikun");
		}

		else if (contentText.equalsIgnoreCase("/bikun")) {
			String replyText = contentText.replace("/bikun", "");
			return new TextMessage(replyText.substring(1));
		}

		else if (contentText.equalsIgnoreCase("/bikun_stop")) {
			String replyText = contentText.replace("/bikun_stop", "");
			String replyToken = event.getReplyToken();
			CarouselTemplate carouselTemplate = new CarouselTemplate(Arrays.asList(
					new CarouselColumn("‭https://image.ibb.co/hYHEO8/DSC_1000.jpg‬", "Halte FH", "Fakultas Hukum",
							Collections.singletonList(new PostbackAction("Pilih", "0"))),
					new CarouselColumn("‭https://image.ibb.co/gBWDGT/DSC_1001.jpg‬", "Halte MUI",
							"Masjid Ukhuwah Islamiyah", Collections.singletonList(new PostbackAction("Pilih", "1"))),
					new CarouselColumn("‭https://image.ibb.co/j0jpqo/DSC_1002.jpg‬", "Halte Pocin", "Pondok Cina",
							Collections.singletonList(new PostbackAction("Pilih", "2")))));

			TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
			this.reply(replyToken, templateMessage);
		}

		return new TextMessage("Wrong input, use '/bikun' or '/bikun_stop'");

	}
	
	@EventMapping
	 public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        System.out.println(locationMessage.getLatitude());
        System.out.println(locationMessage.getLongitude());
	}


	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		String replyToken = event.getReplyToken();
		String jawaban = "";
		if (event.getPostbackContent().getData().equals("0")) {
			
		} else if (event.getPostbackContent().getData().equals("1")) {

		} else if (event.getPostbackContent().getData().equals("2")) {

		}
		this.reply(replyToken, new TextMessage(jawaban));
	}

}