package org.cambi.test.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.RunRepository;
import org.cambi.repository.TweetRepository;
import org.cambi.repository.UserRepository;
import org.cambi.service.TwitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterServiceUnitTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterServiceUnitTest.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TweetRepository twitterRepository;

    @Mock
    private RunRepository runRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TwitterService twitterService;

    @Mock
    private TwitterAuthenticator authenticator;

    private static Date elapse = new Date();

    private static String search = "?track=bieber";

    @BeforeEach
    public void setUp() throws IOException, ExecutionException, InterruptedException {

        String path = "src/test/resources/run.json";

        File file = new File(path);

        Run run = objectMapper.readValue(file, Run.class);

        when(twitterService.parseTweetsFromRequest(any(), anyString()))
                .thenReturn(objectMapper.readValue(file, Run.class));

        when(twitterService.createRun(run, elapse.getTime(), DEFAULT_API, search)).thenReturn(run);

        when(runRepository.findAll()).thenReturn(Arrays.asList(run));
        when(twitterRepository.findAll()).thenReturn(run.getTweetRuns()
                .stream().collect(Collectors.toList()));
        when(userRepository.findAll()).thenReturn(run.getTweetRuns().stream()
                .map(t -> t.getUserTweets()).filter(u -> u != null).collect(Collectors.toList()));
    }

    @Test
    public void should_parse_tweet_from_request() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run response = twitterService.parseTweetsFromRequest(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API.concat(search));

        assertTrue(response.getTweetRuns().size() == 5);

    }

    @Test
    public void should_create_run() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run response = twitterService.parseTweetsFromRequest(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API.concat(search));

        Run run = twitterService.createRun(response, elapse.getTime(), DEFAULT_API, search);

        assertEquals(response.getRunId(), run.getRunId());

        List<Run> runs = runRepository.findAll();
        assertEquals(1, runs.size());

        List<TweetRun> tweets = twitterRepository.findAll();
        assertEquals(5, tweets.size());

        List<UserTweet> users = userRepository.findAll();
        assertEquals(3, users.size());

    }

}
