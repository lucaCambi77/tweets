/**
 *
 */
package org.cambi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.TweetDto;
import org.cambi.exception.TwitterParserRunnableException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TwitterParserRunnableService implements Runnable {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TwitterAuthenticator authenticator;

    private String path;

    private Set<TweetDto> tweets;

    public TwitterParserRunnableService() {

    }

    private static int MAX_NUM_TWEETS = 100;

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

                if (countTweets == MAX_NUM_TWEETS)
                    break;

                tweets.add(objectMapper.readValue(line, TweetDto.class));

                line = reader.readLine();
                ++countTweets;

                log.info("Number of Tweets: " + countTweets);
            }

        } catch (Exception e) {

            throw new TwitterParserRunnableException((e));
        }

    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<TweetDto> getTweets() {
        return tweets;
    }
}
