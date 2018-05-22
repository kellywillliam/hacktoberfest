package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.concurrent.ExecutionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.logging.Logger;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;

@LineMessageHandler
public class HangoutController {
    
    @Autowired
    private LineMessagingClient lineMessagingClient;
    private static final Logger LOGGER = Logger.getLogger(HangoutController.class.getName());
    private static int flag = 0;
    private double radius = 0;
    
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", 
                event.getTimestamp(),event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "aa";
        flag = 0; 
        if(contentText.equals("/hangout_kuy")){
            replyText = "Please send your location to me";
            flag = 1;
        }
        else if (contentText.equals("/random_hangout_kuy")){
            replyText = "Please send your location to me";
            flag = 2;
        }
        else if (contentText.startsWith("/nearby_hangout_kuy")){
            replyText = "Please send your location to me";
            flag = 3; 
            radius = Double.parseDouble(contentText.split(" ")[1]);
        }
        
        List<Message> messages = new ArrayList<Message>();
        messages.add(new TextMessage(replyText));
        this.reply(event.getReplyToken(),Arrays.asList(new TextMessage(replyText)) );
        
    }
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    private static double userLatitude;
    private static double userlongitude;
    @EventMapping
    public void handleLocationMessage(MessageEvent<LocationMessageContent> event){
        LocationMessageContent message = event.getMessage();
        userLatitude = message.getLatitude();
        userlongitude = message.getLongitude();
        List<Message> messages = new ArrayList<Message>();
        
        if(flag == 1){
            messages = nearestHangout(userLatitude,userlongitude);
            flag = 0;
        }
        else if (flag == 2){
            TemplateMessage template = carousel("a");
            messages = Collections.singletonList(template);
            flag=0;
        }
        else if (flag == 3){
            String[] place = getNearestPlace(userLatitude,userlongitude);
            double distance = Double.parseDouble(place[place.length-1]);
            
            if( distance > radius){
             messages.add(new TextMessage("There is no Hangoutplace near your location")) ;     
            }
            else{
                messages.add(new LocationMessage(place[1],place[2],
                            Double.parseDouble(place[4]) , Double.parseDouble(place[5])) );
                messages.add(new TextMessage(place[3]));
                messages.add(new TextMessage("Approximated distance from your location " 
                        + (int)distance+ " metres"));
            }
            flag = 0;
        }
        reply(event.getReplyToken(),messages);
     }
    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String chosen = "" ;
        String[] partial;
        
        if( event.getPostbackContent().getData().equals("1")){
             chosen = carouselList[0] ;  
             
        }
        else if( event.getPostbackContent().getData().equals("2")){
            chosen = carouselList[1] ;
        }
        else if( event.getPostbackContent().getData().equals("3")){
            chosen = carouselList[2] ;
        }
        
        partial = chosen.split(",");
        partial[2] = partial[2].replace("+", ",");
        partial[3] = partial[3].replace("+", ",");
        
        double lat = Double.parseDouble(partial[4]) ;
        double longit= Double.parseDouble(partial[5]) ; 
        
        this.reply(replyToken, Arrays.asList(
                new LocationMessage(partial[1],partial[2],
                        lat ,longit ),
                new TextMessage(partial[3]),
                new TextMessage("Approximated distance from your location "
                        + (int)getDistance(userLatitude,lat,userlongitude,longit) +" metres" )));
    }

    
    public List<Message> nearestHangout(double latitude,double longitude){
        
        String replyText = "";
        String[] reply = getNearestPlace(latitude,longitude);
        
        reply[2] = reply[2].replace("+", ",");
        reply[3] = reply[3].replace("+", ",");
        
        List<Message> messages = new ArrayList<Message>();
        
        messages.add(new LocationMessage(reply[1],"haha"
                ,Double.parseDouble(reply[4]),Double.parseDouble(reply[5])));
        
        messages.add(new TextMessage(reply[3]));
        
        replyText = "Approximated distance from your location " 
                + reply[reply.length-1] + " metres";
        messages.add(new TextMessage(replyText));
        
        return messages;
    }

    public String[] getNearestPlace(double lat1,double lon1){
        String csvFile = "hangouts.csv";
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
            return (Arrays.toString(partial).replace("]", "")+ ","+ minimum).split(",");
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
    private static String[] carouselList;
    private TemplateMessage carousel(String nama){
        carouselList = getListCarousel();
        String imageUrl = createUri("/static/buttons/1040.jpg");
        String[] c1 = carouselList[0].split(",");
        String[] c2 = carouselList[1].split(",");
        String[] c3 = carouselList[2].split(",");
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn(imageUrl, c1[1], "Hangoutplace", Arrays.asList(
                                new PostbackAction("INFO", "1")
                        )),
                        new CarouselColumn(imageUrl, c2[1], "Hangoutplace", Arrays.asList(
                                new PostbackAction("INFO",
                                                   "2")
                        )),
                        new CarouselColumn(imageUrl, c3[1], "Hangoutplace", Arrays.asList(
                                new PostbackAction("INFO",
                                                   "3")
                        ))
                ));
        
        TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
        return templateMessage;
    }
    
    public String[] getListCarousel(){
        String csvFile = "hangouts.csv";
        String line;
        List<Integer> list = getRandom();
        String[] carouselList = new String[3];
        int counter = 1;
        int index = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine(); //Skip the first line  
            
            while ((line = br.readLine()) != null) {
                if(index == 3) break;
                if(counter == list.get(0) || counter == list.get(1) || counter == list.get(2)){
                    carouselList[index] = line ;
                    index++;System.out.println("a");
                }
                counter++;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return carouselList;
    }
    public List<Integer> getRandom(){
        List<Integer> list = new ArrayList<Integer>();
        int  n = 0;
        while(list.size() < 3){
            Random rand = new Random();
            n = rand.nextInt(15) + 1;
            while(list.contains(n)){
                rand = new Random();
                n = rand.nextInt(15) + 1;
            }
           list.add(n);
        }
        return list;
    }
    
    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
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