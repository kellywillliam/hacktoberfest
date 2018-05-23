package advprog.example.bot.composer;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AnimeSeasonComposer {

    String genre;

    List<String> animeSeason = new ArrayList<>();
    List<String> animeYear = new ArrayList<>();

    public AnimeSeasonComposer() {
        animeSeason.add("winter");
        animeSeason.add("summer");
//        animeSeason.add("Fall");
        animeSeason.add("spring");

        animeYear.add("2019");
        animeYear.add("2018");
        animeYear.add("2017");
        animeYear.add("2016");
        animeYear.add("2015");
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
                String data = season + "-" + year;
                actions.add(new PostbackAction(season, data));
            }
            CarouselColumn newCarousel = new CarouselColumn(null, year, "Season:", actions);
            carouselColumns.add(newCarousel);
        }
        return carouselColumns;
    }

    public String getWebsite(String routing) throws IOException {
        String reply = "";

        try {
            String linkAnime = "https://www.livechart.me/" + routing + "/tv";

            String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";
            Document doc = Jsoup.connect(linkAnime).userAgent(ua).get();
            Elements getGenreAll = doc.getElementsByClass("anime-tags");
            getDataWebsite(getGenreAll);
            System.out.println("Ini url nya: " + linkAnime);
            return "Ini url nya: " + linkAnime;

        }
        catch (IOException e) {
            reply = e.toString();
            return reply;
        }
    }

    public String getDataWebsite(Elements getGenreAll){
        List<Element> animeWithPreferredGenre = new ArrayList<>();
        Map<String, String> titleAndSynopsis = new TreeMap<>();
        String reply = "";

        for (Element e: getGenreAll) {
            Elements getChildrenGenre = e.children();
            for (Element child: getChildrenGenre) {
                if (child.text().equals(genre)) {
                    Element animeTag = child.parent();
                    Element animeCard = animeTag.parent();
                    Element article = animeCard.parent();
                    animeWithPreferredGenre.add(article);
                }
            }
        }

        for (Element anime: animeWithPreferredGenre) {
            String title = anime.attr("data-romaji");
            String synopsis = ((anime.child(1)).child(6)).child(3).text();
            titleAndSynopsis.put(title, synopsis);
        }

        for (int i = 0; i < titleAndSynopsis.size(); i++) {
            String title = titleAndSynopsis.keySet().toArray()[i].toString();
            String synopsis = titleAndSynopsis.get(title);

            reply = reply + "Here are anime(s) that matches with your genre:\n"
                    + title + "\n"
                    + synopsis;
        }
        return reply;
    }
}
