package advprog.toplaughers.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@LineMessageHandler
public class TopLaughersController {

    @Autowired
    private LineMessagingClient lineMessagingClient;
    private Map<String, Map<String, Integer>> laughers = new TreeMap<>();
    private Map<String, Integer> totalLaughter = new TreeMap<>();

    public static void main(String[] args) {
        SpringApplication.run(TopLaughersController.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws ExecutionException, InterruptedException {

        System.out.println("event: " + event);
        String senderName = getSenderName(event);
        String groomUserId = getGroomUserId(event);

        if (event.getMessage().getText().toLowerCase().contains("haha")) {
            putLaughter(senderName, groomUserId);
            //return new TextMessage("kamu abis ketawa garing yaa, " + senderName + "?");
        } else if (event.getMessage().getText().toLowerCase().contains("wkwk")) {
            putLaughter(senderName, groomUserId);
            //return new TextMessage("kamu abis ketawa ngakak yaa, " + senderName + "?");
        } else if (event.getMessage().getText().contains("/toplaughers")) {
            //if no one had ever laughed in the group
            if (!laughers.containsKey(groomUserId)) {
                return new TextMessage("1.        \n2.\n3.\n4.\n5.");
            }
            SortedSet<Map.Entry<String,Integer>> groomUser =
                    entriesSortedByValues(laughers.get(groomUserId));
            return new TextMessage(printLaughers(groomUser, groomUserId, totalLaughter));
        } else if (event.getMessage().getText().contains("/see")) {
            Map<String, Integer> groomUser = laughers.get(groomUserId);
            return new TextMessage(entriesSortedByValues(groomUser).toString());
        } else if (event.getMessage().getText().contains("/clearall")) {
            laughers.clear();
            totalLaughter.clear();
            return new TextMessage("All cleared!");
        } else if (event.getMessage().getText().contains("/clearthis")) {
            laughers.remove(groomUserId);
            totalLaughter.remove(groomUserId);
            return new TextMessage("This space cleared!");
        }
        //return new TextMessage(event.getMessage().getText() + " : " + senderName);
        return null;
    }

    public String getSenderName(MessageEvent<TextMessageContent> event)
            throws ExecutionException, InterruptedException {
        UserProfileResponse senderProfile;
        if (event.getSource() instanceof GroupSource) {
            String groupId = getGroomUserId(event);
            String senderId = event.getSource().getUserId();
            CompletableFuture<UserProfileResponse> senderProfileFuture =
                    lineMessagingClient.getGroupMemberProfile(groupId, senderId);
            senderProfile = senderProfileFuture.get();
        } else if (event.getSource() instanceof RoomSource) {
            String roomId = getGroomUserId(event);
            String senderId = event.getSource().getUserId();
            CompletableFuture<UserProfileResponse> senderProfileFuture =
                    lineMessagingClient.getRoomMemberProfile(roomId, senderId);
            senderProfile = senderProfileFuture.get();
        } else {
            String userId = getGroomUserId(event);
            CompletableFuture<UserProfileResponse> senderProfileFuture =
                    lineMessagingClient.getProfile(userId);
            senderProfile = senderProfileFuture.get();
        }
        return senderProfile.getDisplayName();
    }

    public String getGroomUserId(MessageEvent<TextMessageContent> event) {
        return event.getSource().getSenderId();
    }

    public void putLaughter(String senderName, String groomUserId) {
        //if he is the first one to laugh in the group
        if (!laughers.containsKey(groomUserId)) {
            Map<String, Integer> names = new TreeMap<>();
            names.put(senderName, 1);
            laughers.put(groomUserId, names);
            totalLaughter.put(groomUserId, 1);
        } else { //if someone had already laughed in the group before
            //if he laughed for the first time
            if (!laughers.get(groomUserId).containsKey(senderName)) {
                laughers.get(groomUserId).put(senderName, 1);
            } else { //if he had laughed before
                laughers.get(groomUserId).put(
                        senderName, laughers.get(groomUserId).get(senderName) + 1);
            }
            totalLaughter.put(groomUserId, totalLaughter.get(groomUserId) + 1);
        }
    }

    public String printLaughers(SortedSet<Map.Entry<String,Integer>> groomUserId, String id,
                                Map<String, Integer> totalLaughter) {
        String result = "";
        Iterator<Map.Entry<String, Integer>> it = groomUserId.iterator();
        int toBeUsed = 0;
        for (int i = 1; i < 6; i++) {
            int rank = i;
            if (it.hasNext()) {
                Map.Entry<String, Integer> next = it.next();
                String name = next.getKey();
                int laughter = next.getValue();
                int percentage = toPercentage(laughter, totalLaughter.get(id));
                if (i > 1 && toBeUsed == laughter) {
                    rank -= 1;
                }
                result += rank + ". " + name + " " + " (" + percentage + "%) \n";
                toBeUsed = laughter;
            } else {
                result += i + ". \n";
            }
        }
        return result;
    }

    public static <K,V extends Comparable<? super V>>
        SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public int toPercentage(int laughterTimes, int total) {
        return Math.round(((float)laughterTimes) / total * 100);
    }
}
