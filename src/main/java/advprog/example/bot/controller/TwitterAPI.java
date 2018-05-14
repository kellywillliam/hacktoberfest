package advprog.example.bot.controller;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.ArrayList;

public class TwitterAPI {

    ConfigurationBuilder config;
    Twitter twitter;

    public TwitterAPI(){
        config = new ConfigurationBuilder();
        config.setDebugEnabled(true)
                .setOAuthConsumerKey("PJv91Il9VxZCQQWEqxuNVC3Vu")
                .setOAuthConsumerSecret("kqbd0d86y6QzQTTWPkdVyG3z0iNQagiwDiGDtEYuWJghzJXeUi")
                .setOAuthAccessToken("227635776-4cOQ2WinjBCXKHskeKsYyHaK71wuDMPxgckYODPe")
                .setOAuthAccessTokenSecret("XXD4YYpT7aWTAs8yUHQQjkb8ESaGaaO5A0Yj3hJFN5g13");
        TwitterFactory twitterFactory = new TwitterFactory(config.build());
        twitter = twitterFactory.getInstance();
    }

    public List<String> getTimeLine(String user) {

        List<String> strings = new ArrayList<String>();

        try {

            List<Status> listStatus = twitter.getUserTimeline(user);
            System.out.println("Showing @" + user + "'s 5 recent tweets.");
            for(Status status : listStatus){
                strings.add("@" + status.getUser().getScreenName()+ " - " + status.getText());
            }

        } catch (TwitterException te) {
            String msg = te.getErrorMessage();

            if(msg == null)
                strings.add("We can't retrieve tweets from @" + user + ". Please make sure His/Her profile is not protected.");
            else
                strings.add("Failed to retrieve tweets caused by " + msg);
        }

        return strings.subList(0,5);
    }

    public String getUserTimeLine(String user){
        List<String> listStatus = getTimeLine(user);
        String result = "";
        for(String status : listStatus){
            result += (status + "\n");
        }

        return result;

    }
}
