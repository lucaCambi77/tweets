/**
 *
 */
package org.cambi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.TweetDto;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luca
 *
 */
@Service
@Slf4j
public class TwitterServiceRunnable implements Runnable {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TwitterAuthenticator authenticator;

    private String path;

    private Set<TweetDto> tweets;

    /**
     *
     */
    public TwitterServiceRunnable() {

    }

    /**
     * Max number of tweets
     */
    private static int MAX_TWEET_SIZE = 100;

    @Override
    public void run() {
        tweets = new HashSet<>();

        try {
            InputStream in = authenticator.getAuthorizedHttpRequestFactory()
                    .buildGetRequest(new GenericUrl(path)).execute().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();

            int countTweets = 0;

            /**
             * We wait for X seconds or up to Y messages
             */
            while (line != null && !Thread.interrupted()) {

                if (countTweets == MAX_TWEET_SIZE)
                    break;

                tweets.add(objectMapper.readValue(line, TweetDto.class));

                line = reader.readLine();
                ++countTweets;

                log.info("Number of Tweets: " + countTweets);
            }

        } catch (Exception e) {

            throw new RuntimeException((e));
        }

    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<TweetDto> getTweets() {
        return tweets;
    }
}
