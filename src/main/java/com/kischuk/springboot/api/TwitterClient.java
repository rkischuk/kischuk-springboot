package com.kischuk.springboot.api;

import com.kischuk.springboot.util.SecretManager;

import java.io.IOException;
import java.util.HashMap;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterClient {

    public static final String ACCESS_TOKEN = "twitter_access_token";
    public static final String ACCESS_TOKEN_SECRET = "twitter_access_token_secret";
    public static final String CONSUMER_KEY = "twitter_consumer_key";
    public static final String CONSUMER_SECRET = "twitter_consumer_secret";
    public static final String SECRET_NAME = "Twitter-API-tokens";

    public static ResponseList<Status> getUserTimeline( String handle ) throws TwitterException, IOException {

        SecretManager secretManager = new SecretManager();
        HashMap<String, String> secrets = secretManager.getSecretValueMap(TwitterClient.SECRET_NAME);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(secrets.get(TwitterClient.CONSUMER_KEY))
          .setOAuthConsumerSecret(secrets.get(TwitterClient.CONSUMER_SECRET))
          .setOAuthAccessToken(secrets.get(TwitterClient.ACCESS_TOKEN))
          .setOAuthAccessTokenSecret(secrets.get(TwitterClient.ACCESS_TOKEN_SECRET));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        return twitter.getUserTimeline(handle);
    }
}
