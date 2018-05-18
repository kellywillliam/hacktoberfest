package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Arrays;
import java.util.logging.Logger;

@LineMessageHandler
public class FakeNewsController {

    private static final Logger LOGGER = Logger.getLogger(FakeNewsController.class.getName());
	private static UrlDatabase newsDb = new UrlDatabase();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText().trim();
		String replyText = "";
		String[]inputs = contentText.split(" ");
		replyText = validateInput(inputs);
		if(replyText.equals("")){
			String cmd = inputs[0];
			String url = inputs[1];
			if(cmd.equals("/add_filter")){
				newsDb.addFakeNews(url, inputs[2]);
			} else {
				replyText = newsDb.checkUrl(cmd.replace("/is_", ""), url);
			}
		}
        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String validateInput(String[] inputs) {
        String[] cmds = {"/is_fake", "/is_satire", "/is_conspiracy"};
        String[] types = {"fake", "satire", "conspiracy", "political", "bias", "unreliable"};
        String wrong = "Wrong input, valid inputs are :\n"
                + "/is_fake URL\n" + "/is_satire URL\n"
                + "/is_conspiracy URL\n" + "/add_filter URL TYPE\n"
                + "Note: URL = news url using HTTP protocol\n"
                + "      TYPE = fake news type\n";

        if (inputs.length == 2) {
            boolean validCmd = Arrays.asList(cmds).contains(inputs[0]);
            if (validCmd && inputs[1].contains("http://")) {
				inputs[1] = inputs[1].replace("http://", "");
                return "";
            }
        } else if (inputs.length == 3) {
            if (inputs[0].equals("/add_filter") && inputs[1].contains("http://")) {
				inputs[1] = inputs[1].replace("http://", "");
                boolean validType = Arrays.asList(types).contains(inputs[2]);
                if (validType) {
                    return "";
                } else {
                    return "TYPE values : "
                            + Arrays.toString(types).replace("[", "").replace("]", "");
                }
            }
        }
        return wrong;
    }

}
