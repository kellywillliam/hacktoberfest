package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@LineMessageHandler
public class OriconController {
	private static final Logger LOGGER = Logger.getLogger(OriconController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.startsWith("/oricon comic ")) {
			String replyText = contentText.replace("/oricon comic ", "");
			String reply = showComics(replyText);
			if (reply.equals("")) {
				return new TextMessage("Please Input Monday date");
			} else {
				return new TextMessage(reply);
			}
		} else {
			return new TextMessage("Wrong Input, Format Input '/oricon comic DD/MM/YYYY'");
		}

	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String showComics(String date) {
		Elements element = screenScrapeGetComics(makeGetCall(date));
		String comics = "";
		int num = 1;
		for (Element e : element) {

			String title = e.getElementsByClass("title").text();
			String author = e.getElementsByClass("name").text();

			comics += "(" + num + ")" + title + " - " + author + "\n";
			num++;
		}
		return comics;
	}

	public static Document makeGetCall(String date) {
		Document document;
		try {
			String url = "https://www.oricon.co.jp/rank/obc/w/";
			url += date;
			document = Jsoup.connect(url).get();
			return document;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Elements screenScrapeGetComics(Document doc) {
		Elements content = doc.select(".wrap-text");
		return content;
	}
}
