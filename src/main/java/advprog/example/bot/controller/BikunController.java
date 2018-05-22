package advprog.example.bot.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.LocationMessage;
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

		if (event.getSource() instanceof GroupSource) {
            if (songsToPreviewUrl.containsKey(contentText)) {
                return new TextMessage("We have that song, chat me "
                        + "and add to your songs to listen!");

		
		else if (contentText.equalsIgnoreCase("/bikun")) {
//			String replyText = contentText.replace("/bikun", "");
			String replyText = "Please send your location";
			return new TextMessage(replyText);
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

	private static double userLatitude;
	private static double userLongitude;
	
	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LocationMessageContent locationMessage = event.getMessage();
		userLatitude = locationMessage.getLatitude();
		userLongitude = locationMessage.getLongitude();
		String replyText = "";
		String[] reply = getNearestBusStop(userLatitude, userLongitude);
		
		reply[2] = reply[2].replace("+", ",");
		
		List<Message> messages = new ArrayList<Message>();
		
		messages.add(new LocationMessage(reply[1], "Click to view location",
                Double.parseDouble(reply[3]), Double.parseDouble(reply[4])));


		messages.add(new TextMessage(reply[2]));
		
		replyText = "Approximate distance from your location "
                + (int) Double.parseDouble(reply[reply.length - 1]) + " meters";
        messages.add(new TextMessage(replyText));
		
//		replyText(event.getReplyToken(),
//				"latitude: " + locationMessage.getLatitude() + ", longitude: " + locationMessage.getLongitude());
		// System.out.println(locationMessage.getLatitude());
		// System.out.println(locationMessage.getLongitude());
		
		reply(event.getReplyToken(), messages);
	}

	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "";
		}
		this.reply(replyToken, new TextMessage(message));
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	private void reply(@NonNull String replyToken, @NonNull Message locationMessage) {
		reply(replyToken, Collections.singletonList(locationMessage));
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
	
	public String[] getNearestBusStop(double userLatitude, double userLongitude) {
        String csvFile = "Bikun.csv";
        String line;
        String[] csvString = new String[3];

        int index = 0;
        int minIndex = 0;

        double distance;
        double busstopLatitude;
        double busstopLongitude;
        double minimum = Double.MAX_VALUE;
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine(); // Skip the first line
            while ((line = br.readLine()) != null) {
                csvString[index] = line;
                String[] place = line.split(",");
                busstopLatitude = Double.parseDouble(place[3]);
                busstopLongitude = Double.parseDouble(place[4]);
                distance = getDistance(userLatitude, busstopLatitude, userLongitude, busstopLongitude);
                if (distance < minimum) {
                    minimum = distance;
                    minIndex = index;
                }
                index++;
            }
            br.close();
            String[] partial = csvString[minIndex].split(",");
            return (Arrays.toString(partial).replace("]", "") + "," + minimum).split(",");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


	public double getDistance(double userLatitude, double busstopLatitude, double userLongitude, double busstopLongitude) {
		final int earthRadius = 6371; // Radius of the earth
		double latDistance = Math.toRadians(userLatitude - busstopLatitude);
		double lonDistance = Math.toRadians(userLongitude - busstopLongitude);
		double x = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(userLatitude))
				* Math.cos(Math.toRadians(busstopLatitude)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double y = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
		double distance = earthRadius * y * 1000; // convert to meters
		return distance;
	}

}