package advprog.sacredtext.controller;

/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;


@SpringBootApplication
@LineMessageHandler
public class SacredTextController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    private final static Logger LOGGER = Logger.getLogger(SacredTextController.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(SacredTextController.class, args);
    }

    @EventMapping
    public void handleTextMessageEvent(String replyToken, MessageEvent<TextMessageContent> event) {
        if (event.getMessage().getText().equalsIgnoreCase("/sacred_text")) {
            String imageUrl = createUri("/static/books.jpeg");
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn(imageUrl, "Chapter 1", "This is carousel for chapter 1", Arrays.asList(
                                    new PostbackAction("See chapter 1",
                                            "This is chapter 1!")
                            )),
                            new CarouselColumn(imageUrl, "Chapter 2", "This is carousel for chapter 2", Arrays.asList(
                                    new PostbackAction("See chapter 2",
                                            "This is chapter 2!")
                            )),
                            new CarouselColumn(imageUrl, "Chapter 3", "This is carousel for chapter 3", Arrays.asList(
                                    new PostbackAction("See chapter 3",
                                            "This is chapter 3!")
                            ))
                    ));
            TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
            this.reply(replyToken, templateMessage);
        }
        System.out.println("event: " + event);
//        return new TextMessage("Kamu bilang: " + event.getMessage().getText());
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
//            LOGGER.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
