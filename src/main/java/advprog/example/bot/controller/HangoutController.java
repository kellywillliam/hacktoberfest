package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.concurrent.ExecutionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.logging.Logger;

import org.springframework.lang.NonNull;

import java.util.*;

@LineMessageHandler
public class HangoutController {

    private static final Logger LOGGER = Logger.getLogger(HangoutController.class.getName());
    private LineMessagingClient lineMessagingClient;
    
    private int flag = 0;
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", 
                event.getTimestamp(),event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = contentText.replace("/Hangout", "");
        flag = 0; 
        if(contentText.equals("/hangout_kuy")){
            replyText = "Please send your location to me";
            flag = 1;
        }
        else if (contentText.equals("/random_hangout_kuy")){
            replyText = "Please send your location to me";
            flag = 2;
        }
        
        List<Message> messages = new ArrayList<Message>();
        messages.add(new TextMessage(replyText));
        reply(event.getReplyToken(),messages);
    }
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    @EventMapping
    public void handleLocationMessage(MessageEvent<LocationMessageContent> event){
        LocationMessageContent message = event.getMessage();
        double latitude = message.getLatitude();
        double longitude = message.getLongitude();
        List<Message> messages = new ArrayList<Message>();;
        messages.add(new TextMessage("a"));
        if(flag == 1){
            messages = nearestHangout(latitude,longitude);
            flag = 0;
        }
        else if (flag == 2){
            messages = new ArrayList<Message>();
            messages.add(new TextMessage("b"));
            flag=0;
        }
        reply(event.getReplyToken(),messages);
     }
    
    private List<Message> nearestHangout(double latitude,double longitude){
        String replyText = "Nearest Hangout Place /n";
        String[] reply = getNearestPlace(latitude,longitude);
        for(int i = 1 ; i < 4 ;i++){
            replyText += reply[i]+"/n";
        }
        List<Message> messages = new ArrayList<Message>();
        messages.add(new TextMessage(replyText));
        replyText = "approximated distance from your location " + reply[reply.length-1];
        messages.add(new TextMessage(replyText));
        return messages;
    }

    private String[] getNearestPlace(double lat1,double lon1){
        String csvFile = "hangout.csv";
        String line;
        String[] csvString = new String[16]; 
        
        int index = 0;
        int minIndex = 0;
        
        double distance;
        double lat2;
        double lon2;
        double minimum = Double.MAX_VALUE;
        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine(); //Skip the first line  
            while ((line = br.readLine()) != null) {
                csvString[index] = line;
                String[] place = line.split(",");
                lat2 = Double.parseDouble(place[4]);
                lon2 = Double.parseDouble(place[5]);
                distance = getDistance(lat1,lat2,lon1,lon2);
                if(distance < minimum){
                    minimum = distance; 
                    minIndex = index;
                }
                index ++;
            }
            br.close();
            
            String[] partial = csvString[minIndex].split(",");
            partial[2] = partial[2].replaceAll("+", ",");
            partial[3] = partial[3].replaceAll("+", ",");
            return (Arrays.toString(partial)+ ","+ minimum).split(",");
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    private double getDistance(double lat1, double lat2, double lon1, double lon2){
        final int earthRadius = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c * 1000; // convert to meters
        return distance;
    }
    
    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            System.out.printf("Sent messages: " + apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}