package org.cambi.test.service;


import org.cambi.dto.RunDto;
import org.cambi.dto.TweetDto;
import org.cambi.dto.UserTweetDto;
import org.cambi.service.TwitterParserExecutionService;
import org.cambi.service.TwitterParserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterParseTweetsTest {

    @InjectMocks
    private TwitterParserService twitterParserService;

    @Mock
    private TwitterParserExecutionService twitterParserRunnableService;

    private static RunDto runDto;

    static {
        runDto = RunDto.builder()
                .tweets(new HashSet<>(Arrays.asList(
                        TweetDto.builder()
                                .creationDate(new Date())
                                .id(new BigInteger("1"))
                                .messageText("Message1")
                                .userTweet(UserTweetDto.builder()
                                        .userName("Name")
                                        .userScreenName("ScreenName")
                                        .id(new BigInteger("1")).build())
                                .build())))
                .numTweet(1)
                .build();
    }

    @Test
    public void shouldMatchRunDto() throws Exception {
        Mockito.lenient().when(twitterParserRunnableService.getTweetsFromExecution("query"))
                .thenAnswer((Answer<Set<TweetDto>>) invocation -> new HashSet<>(Arrays.asList(
                        TweetDto.builder()
                                .creationDate(new Date())
                                .id(new BigInteger("1"))
                                .messageText("Message1")
                                .userTweet(UserTweetDto.builder()
                                        .userName("Name")
                                        .userScreenName("ScreenName")
                                        .id(new BigInteger("1")).build())
                                .build())));

        RunDto run = twitterParserService.parseTweetsFrom("query");
        assertEquals(runDto.getNumTweet(), run.getTweets().size());
        assertEquals(runDto.getTweets().size(), run.getTweets().size());
        assertEquals(runDto.getTweets().iterator().next(), run.getTweets().iterator().next());
        assertEquals(null, run.getException());

        Mockito.verify(twitterParserRunnableService
                , Mockito.times(1)).getTweetsFromExecution("query");
    }

    @Test
    public void shouldCatchRunTimeException() throws Exception {

        doThrow(new RuntimeException("RunTimeException")).when(twitterParserRunnableService).getTweetsFromExecution("query");
        RunDto run = twitterParserService.parseTweetsFrom("query");
        assertEquals("RunTimeException", run.getException());
    }
}
