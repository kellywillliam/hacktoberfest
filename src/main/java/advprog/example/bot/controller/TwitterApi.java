package advprog.example.bot.controller;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApi {

    ConfigurationBuilder config;
    Twitter twitter;

    public TwitterApi() {
        config = new ConfigurationBuilder();
        config.setDebugEnabled(true)
                .setOAuthConsumerKey("PJv91Il9VxZCQQWEqxuNVC3Vu")
                .setOAuthConsumerSecret("kqbd0d86y6QzQTTWPkdVyG3z0iNQagiwDiGDtEYuWJghzJXeUi")
                .setOAuthAccessToken("227635776-4cOQ2WinjBCXKHskeKsYyHaK71wuDMPxgckYODPe")
                .setOAuthAccessTokenSecret("XXD4YYpT7aWTAs8yUHQQjkb8ESaGaaO5A0Yj3hJFN5g13");
        TwitterFactory twitterFactory = new TwitterFactory(config.build());
        twitter = twitterFactory.getInstance();
    }

    public String getUserTimeLine(String user) {

        StringBuilder strings = new StringBuilder();

        try {

            List<Status> listStatus = twitter.getUserTimeline(user);
            System.out.println("Showing @" + user + "'s 5 recent tweets.");

            for (int i = 0; i < 5; i++) {
                if (i == listStatus.size())
                    break;
                strings.append("@" + listStatus.get(i).getUser().getScreenName() + " - " + listStatus.get(i).getText());
            }

        } catch (TwitterException te) {
            String msg = te.getErrorMessage();

            if (msg == null) {
                StringBuilder hasil = new StringBuilder();
                hasil.append("We can't retrieve tweets from @");
                hasil.append(user);
                hasil.append(". Please make sure His/Her profile is not protected.");
                strings.append(hasil.toString());
            }
        }
        return strings.toString();
    }
}
