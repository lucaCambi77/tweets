/**
 *
 */
package org.cambi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import org.cambi.dto.TweetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TwitterServiceRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TwitterServiceRunnable.class);

    @Autowired
    private ObjectMapper objectMapper;

    private HttpRequestFactory authenticator;

    private String path;

    private Set<TweetDto> tweets;
    private String exception;

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
        tweets = new HashSet<TweetDto>();

        try {
            InputStream in = getAuthenticator().buildGetRequest(new GenericUrl(getPath())).execute().getContent();

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

                log.info("Number of Tweets: " + (++countTweets));
            }

        } catch (Exception e) {

            throw new RuntimeException((e));
        }

    }

    public String getPath() {
        return path;
    }

    public TwitterServiceRunnable setPath(String path) {
        this.path = path;

        return this;
    }

    public HttpRequestFactory getAuthenticator() {
        return authenticator;
    }

    public TwitterServiceRunnable setAuthenticator(HttpRequestFactory authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public Set<TweetDto> getTweets() {
        return tweets;
    }

    public String getException() {
        return exception;
    }

}
