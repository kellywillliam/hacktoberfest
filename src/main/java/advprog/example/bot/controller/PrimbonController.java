package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@LineMessageHandler
public class PrimbonController {
    private static final Logger LOGGER = Logger.getLogger(PrimbonController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.startsWith("/echo")) {
            return new TextMessage("echo dari primbon");
        }

        String replyText = contentText.replace("/primbon", "");
        
        String[] arrInput = replyText.substring(1).split("-");
        String tahun = arrInput[0];
        String bulan = arrInput[1];
        String tanggal = arrInput[2];
        
        return new TextMessage(makePostCall(tanggal, bulan, tahun));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    public String makePostCall(String tgl, String bln, String thn) { 
        try {
            String url = "http://www.primbon.com/ramalan_weton.php";
            final HttpClient client = HttpClientBuilder.create().build();
            final HttpPost post = new HttpPost(url);

            // add header
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("tgl", tgl));
            urlParameters.add(new BasicNameValuePair("bln", bln));
            urlParameters.add(new BasicNameValuePair("thn", thn));
            urlParameters.add(new BasicNameValuePair("submit", "Submit!"));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new 
                    InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
            return screenScrapeGetNamaWeton(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String screenScrapeGetNamaWeton(String html) {
        Document doc = Jsoup.parse(html);
        Elements newsHeadlines = doc.select("#body");
        System.out.println(newsHeadlines.size());
        System.out.println(newsHeadlines.text());

        String hari = newsHeadlines.text();
        if (hari.indexOf("Hari Lahir: ") == -1) {
            return "Wrong input, please retry";
        }
        String hasil = hari.substring(hari.indexOf(
               "Hari Lahir: ") + 12, hari.indexOf(", Tgl. "));
        return hasil;
    }
}
