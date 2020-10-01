package org.cambi.test.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.dto.TweetDto;
import org.cambi.exception.TwitterParserRunnableException;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterParserRunnableService;
import org.cambi.test.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterParserRunnableTest extends Constant {

    @InjectMocks
    private TwitterParserRunnableService twitterParserRunnableService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TwitterAuthenticator twitterAuthenticator;

    @Test
    public void should_return_tweets() throws IOException, TwitterAuthenticationException {
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(TweetDto.builder().id(new BigInteger("1")).build());
        when(twitterAuthenticator.getAuthorizedHttpRequestFactory())
                .thenReturn(Utils.getFakeRequestFactory(new HashSet<>(Arrays.asList(TweetDto.builder().id(new BigInteger("1")).build()))));

        ReflectionTestUtils.setField(twitterParserRunnableService, "path", DEFAULT_API);
        ReflectionTestUtils.setField(twitterParserRunnableService, "MAX_NUM_TWEETS", 10000);
        twitterParserRunnableService.run();

        assertEquals(1, twitterParserRunnableService.getTweets().size());
    }

    @Test
    public void should_exit_when_max_num_reached() throws IOException, TwitterAuthenticationException {

        when(twitterAuthenticator.getAuthorizedHttpRequestFactory())
                .thenReturn(Utils.getFakeRequestFactory(new HashSet<>(Arrays.asList(TweetDto.builder().id(new BigInteger("1")).build()))));

        ReflectionTestUtils.setField(twitterParserRunnableService, "path", DEFAULT_API);
        ReflectionTestUtils.setField(twitterParserRunnableService, "MAX_NUM_TWEETS", 0);
        twitterParserRunnableService.run();

        assertEquals(0, twitterParserRunnableService.getTweets().size());
    }

    @Test
    public void should_throws_when_authorization_fails() throws IOException, TwitterAuthenticationException {

        when(twitterAuthenticator.getAuthorizedHttpRequestFactory())
                .thenThrow(new TwitterParserRunnableException(new Throwable("error")));

        assertThrows(
                TwitterParserRunnableException.class,
                () -> twitterParserRunnableService.run(),
                "Expected run() to throw TwitterParserRunnableException, but it didn't"
        );
    }

    @Test
    public void should_throws_when_parse_fails() throws IOException, TwitterAuthenticationException {

        when(objectMapper.readValue(anyString(), any(Class.class))).thenThrow(new TwitterParserRunnableException(new Throwable("error")));
        when(twitterAuthenticator.getAuthorizedHttpRequestFactory())
                .thenReturn(Utils.getFakeRequestFactory(new HashSet<>(Arrays.asList(TweetDto.builder().id(new BigInteger("1")).build()))));

        ReflectionTestUtils.setField(twitterParserRunnableService, "path", DEFAULT_API);
        ReflectionTestUtils.setField(twitterParserRunnableService, "MAX_NUM_TWEETS", 10000);

        assertThrows(
                TwitterParserRunnableException.class,
                () -> twitterParserRunnableService.run(),
                "Expected run() to throw TwitterParserRunnableException, but it didn't"
        );
    }
}
