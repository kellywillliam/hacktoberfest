package advprog.example.bot.composer;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import javafx.print.Collation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class AnimeSeasonComposer {

    String genre;

    List<String> animeSeason = new ArrayList<>();
    List<String> animeYear = new ArrayList<>();

    public AnimeSeasonComposer() {
        animeSeason.add("winter");
        animeSeason.add("summer");
//        animeSeason.add("Fall");
        animeSeason.add("spring");

        animeYear.add("2018");
        animeYear.add("2017");
        animeYear.add("2016");
        animeYear.add("2015");
        animeYear.add("2014");

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getAnimeSeason() {
        return animeSeason;
    }

    public void setAnimeSeason(List<String> animeSeason) {
        this.animeSeason = animeSeason;
    }

    public List<String> getAnimeYear() {
        return animeYear;
    }

    public void setAnimeYear(List<String> animeYear) {
        this.animeYear = animeYear;
    }

    public CarouselTemplate carouselTemplate(List<CarouselColumn> carouselColumns) {
        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);
        return carouselTemplate;
    }

    public List<CarouselColumn> carouselColumn() {
        List<CarouselColumn> carouselColumns = new ArrayList<>();

        for (String year: animeYear) {
            List<Action> actions = new ArrayList<>();
            for (String season: animeSeason) {
                String data = year + "/" + season;
                actions.add(new PostbackAction(season, data));
            }
            CarouselColumn newCarousel = new CarouselColumn(null, year, "Season:", actions);
            carouselColumns.add(newCarousel);
        }
        return carouselColumns;
    }

    public List<TextMessage> getWebsite(String routing){
        String reply = "";

        try {
            String linkAnime = "https://myanimelist.net/anime/season/" + routing;

            //String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
            Document doc = Jsoup.connect(linkAnime).get();
            System.out.println("Ini url nya: " + linkAnime);
            return getDataWebsite(doc);

        }
        catch (IOException e) {
            reply = e.toString();
            return Collections.singletonList(new TextMessage("Cannot find resource"));
        }
    }

    public List<TextMessage> getDataWebsite(Document doc){
        Map<String, String> titleAndSynopsis = new TreeMap<>();

        Elements animeCard = doc.getElementsByClass("seasonal-anime js-seasonal-anime");
        for (int i = 0; i < 3; i++){
            Element anime = animeCard.get(i);
            Elements genres = anime.getElementsByClass("genre");
            String title = "";
            String synopsis = "";

            for(Element genre: genres){
                if(genre.text().equalsIgnoreCase(getGenre())){
                    title = anime.getElementsByClass("title-text").text();
                    synopsis = anime.getElementsByClass("preline").text();
                    titleAndSynopsis.put(title, synopsis);
                }
            }

            if (titleAndSynopsis.isEmpty()){
                return Collections.singletonList(new TextMessage("Cannot find anime(s) that suit your preferred genre"));
            }
        }
        return replyText(titleAndSynopsis);
    }

    public List<TextMessage> replyText(Map<String, String> titleAndSynopsis) {
        List<TextMessage> listOfAnimes = new ArrayList<>();

        listOfAnimes.add(new TextMessage("Here are anime(s) that matches with your genre (" + getGenre() + "):"));

        if (titleAndSynopsis.size() > 3) {
            for(int i = 0; i < 5; i++) {
                String key = titleAndSynopsis.keySet().toArray()[i].toString();
                String synopsis = titleAndSynopsis.get(key);

                String reply = key + "\n"
                            + "   " + synopsis;
                listOfAnimes.add(new TextMessage(reply));
            }
        }
        else {
            for(String key: titleAndSynopsis.keySet()) {
                String synopsis = titleAndSynopsis.get(key);
                String reply = key + "\n"
                            + "   " + synopsis;
                listOfAnimes.add(new TextMessage(reply));
            }
        }

        return listOfAnimes;
    }
}
