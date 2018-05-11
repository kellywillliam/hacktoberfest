package advprog.example.bot.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Artist;
import org.musicbrainz.model.entity.ArtistWs2;
import org.musicbrainz.model.entity.ReleaseGroupWs2;
import org.musicbrainz.model.searchresult.ArtistResultWs2;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class MusicBrainzController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws MBWS2Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
//        TextMessageContent content = event.getMessage();
//        String contentText = content.getText();
//        String replyText = contentText.replace("/echo", "");
//        return new TextMessage(replyText.substring(1));
        
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
//        String[] splitContentText = contentText.split(" ");
        String artistName = contentText.replace("/10albums ", "");
        return new TextMessage(albumSearch(artistName.toLowerCase()));
    }

    public String albumSearch(String artistName) throws MBWS2Exception {
    	String res = "";

    	Artist artistSearch = new Artist();
    	artistSearch.search(artistName);

    	List<ArtistResultWs2> result = artistSearch.getFullSearchResultList();
    	ArtistWs2 artist = new ArtistWs2();

    	for (ArtistResultWs2 x : result) {
    		if (x.getArtist().getName().toLowerCase().equals(artistName)) {
    			artist = x.getArtist();
    		}
    	}

    	artistSearch = new Artist();
    	artistSearch.lookUp(artist);

    	List<ReleaseGroupWs2> release_groups = artistSearch.getFullReleaseGroupList();
    	List<ReleaseGroupWs2> albums = new ArrayList<>();

    	for (ReleaseGroupWs2 x : release_groups) {
    
    		if (x.getTypeString().equals("Album")) {
    			albums.add(x);
    			if (albums.size() == 10) {
    				break;
    			}
    		}
    	}
    	
    	Collections.sort(albums, new Comparator<ReleaseGroupWs2>() {
    		
    		@Override
    		public int compare(ReleaseGroupWs2 o1, ReleaseGroupWs2 o2) {
    			return o2.getYear().compareTo(o1.getYear());
    		}
    	});
    	
    	for (ReleaseGroupWs2 x : albums) {
    		res+= artistName + "-" + x.getTitle() + "-" + x.getYear() + "\n";
    	}
    	
    	if (res.length() == 0) {
    		return "Sorry, we cannot find " + artistName + "'s album";
    	}
    	return res.substring(0, res.length() - 1);
    }
    
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
