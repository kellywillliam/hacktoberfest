package advprog.example.bot.controller;

import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class OriconController {

	private static final Logger LOGGER = Logger.getLogger(OriconController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.startsWith("/echo")) {
			return new TextMessage("echo from oricon");
		}
		if (!contentText.startsWith("/oricon books weekly")) {
			return new TextMessage(
					"Wrong input, use /oricon books weekly " + "<date(YYYY-MM-DD)> to access the feature");
		}

		String replyText = contentText.replace("/oricon books weekly", "");
		return new TextMessage(getBook(replyText.substring(1)));
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String getBook(String date) throws Exception {
		Elements elements = screenScrapeGetBooks(makeGetCall(date));
		String result = "";
		if (elements==null) {
			result += "weekly ranking on that date is not exist, please input a date that has monday as the day.";
		} else {
			for (Element e : elements) {
				String chartPosition = e.getElementsByClass("num").text();
				String title = e.getElementsByClass("title").text();
				String author = e.getElementsByClass("name").text();
				Elements list = e.getElementsByClass("list").get(0).getElementsByTag("li");
				String releaseMonth = list.get(1).text();
				releaseMonth = releaseMonth.substring(4, 11).replace("年", "-");
				String estimatedSales = list.get(3).text();
				estimatedSales = estimatedSales.substring(7).replace("部", "").replace(",", "");
				result += "(" + chartPosition + ") " + title + " - " + author + " - " + releaseMonth + " - "
						+ estimatedSales + "\n";
			}
		}
		return result;
	}

	public static String makeGetCall(String date) throws Exception {
		String url = "https://www.oricon.co.jp/rank/ob/w/";
		url += date + "/";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return "Invalid Parameter";
		}
		Document d = Jsoup.connect(url).get();
		return d.html();
	}

	public static Elements screenScrapeGetBooks(String html) {
		if (html == "Invalid Parameter") {
			return null;
		}
		Document doc = Jsoup.parse(html);
		Elements content = doc.select(".box-rank-entry");
		return content;
	}
}
