package org.cambi.test.service;

import org.cambi.constant.Constant;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
import org.cambi.dto.RunDto;
import org.cambi.dto.TweetDto;
import org.cambi.dto.UserTweetDto;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterParserService;
import org.cambi.service.TwitterService;
import org.cambi.service.TwitterServiceRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterServiceUnitTest extends Constant {

    @InjectMocks
    private TwitterService twitterService;

    @Mock
    private TwitterAuthenticator authenticator;

    @Mock
    private TweetDao twitterDao;

    @Mock
    private RunDao runDao;

    @Mock
    private UserTweetDao userDao;

    @Mock
    private TwitterParserService twitterParserService;

    @Mock
    private TwitterServiceRunnable twitterServiceRunnable;

    private static String search = "?track=bieber";

    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {

        Mockito.lenient().when(twitterServiceRunnable.getTweets())
                .thenAnswer((Answer<Set<TweetDto>>) invocation -> new HashSet<>(
                        Arrays.asList(TweetDto.builder().build())
                ));

        Mockito.lenient().when(twitterParserService.parseTweetsFrom(anyString()))
                .thenAnswer((Answer<RunDto>) invocation -> RunDto.builder()
                        .tweets(new HashSet<>(Arrays.asList(
                                TweetDto.builder()
                                        .creationDate(new Date())
                                        .id(new BigInteger("1"))
                                        .messageText("Message1")
                                        .userTweet(UserTweetDto.builder()
                                                .userName("Name")
                                                .userScreenName("ScreenName")
                                                .id(new BigInteger("1")).build())
                                        .build(),
                                TweetDto.builder()
                                        .creationDate(new Date())
                                        .id(new BigInteger("2"))
                                        .messageText("Message2")
                                        .userTweet(UserTweetDto.builder()
                                                .userName("Name1")
                                                .userScreenName("ScreenName1")
                                                .id(new BigInteger("2")).build())
                                        .build())))
                        .numTweet(2).build());

        Mockito.lenient().when(runDao.save(any())).thenReturn(new Run());
        Mockito.lenient().when(twitterDao.save(any())).thenReturn(new TweetRun());
        Mockito.lenient().when(userDao.save(any())).thenReturn(new UserTweet());

    }

    @Test
    public void should_parse_tweets_while_creating_run() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        twitterService.createRun(DEFAULT_API, search);

        Mockito.verify(twitterParserService
                , Mockito.times(1)).parseTweetsFrom(DEFAULT_API.concat(search));
    }

    @Test
    public void should_create_run() throws ExecutionException, InterruptedException {

        twitterService.createRun(DEFAULT_API, search);

        Mockito.verify(runDao, Mockito.times(1)).save(any());

        Mockito.verify(runDao, Mockito.times(1)).save(any());

        Mockito.verify(twitterDao, Mockito.times(2)).save(any());

        Mockito.verify(userDao, Mockito.times(2)).save(any());
    }

}
