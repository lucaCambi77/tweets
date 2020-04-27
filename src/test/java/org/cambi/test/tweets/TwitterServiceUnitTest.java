package org.cambi.test.tweets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterServiceUnitTest extends Constant {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TwitterService twitterService;

    @InjectMocks
    private TwitterService twitterServiceInject;

    @Mock
    private TwitterAuthenticator authenticator;

    @Mock
    private TweetDao twitterDao;

    @Mock
    private RunDao runDao;

    @Mock
    private UserTweetDao userDao;

    private static String search = "?track=bieber";

    private static Run run;

    static {
        try {
            run = objectMapper.readValue(new File("src/test/resources/run.json"), Run.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() throws IOException, ExecutionException, InterruptedException, TwitterAuthenticationException {

        Mockito.lenient().when(twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, search)).thenCallRealMethod();

        Mockito.lenient().when(twitterService.parseTweetsFrom(any(), anyString()))
                .thenReturn(run);

        Mockito.lenient().when(runDao.saveRun(any(), anyLong(), anyString(), anyString(), anyInt())).thenReturn(run);
        Mockito.lenient().when(twitterDao.saveTweets(any(), any())).thenReturn(new TweetRun());
        Mockito.lenient().when(userDao.saveUserTweet(any(), any())).thenReturn(new UserTweet());

    }

    @Test
    public void should_parse_tweet_from_request() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run response = twitterService.parseTweetsFrom(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API.concat(search));

        assertEquals(response.getRunId(), run.getRunId());
        assertTrue(response.getTweetRuns().size() == 5);

    }

    @Test
    public void should_invoke_methods_while_create_run() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, search);

        Mockito.verify(twitterService
                , Mockito.times(1)).parseTweetsFrom(authenticator.getAuthorizedHttpRequestFactory(), DEFAULT_API.concat(search));

        Mockito.verify(twitterService
                , Mockito.times(1)).createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, search);

    }

    @Test
    public void should_create_run() {

        Run output = twitterServiceInject.createRun(run, new Date().getTime(), DEFAULT_API, search);

        assertNotNull(output.getRunId());

        Mockito.verify(runDao, Mockito.times(1)).saveRun(any(), any(), anyString(), anyString(), anyInt());
        Mockito.verify(twitterDao, Mockito.times(5)).saveTweets(any(), any());
        Mockito.verify(userDao, Mockito.times(3)).saveUserTweet(any(), any());

    }

}
