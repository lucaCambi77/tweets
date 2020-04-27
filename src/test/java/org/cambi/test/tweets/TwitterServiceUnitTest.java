package org.cambi.test.tweets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterServiceUnitTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterServiceUnitTest.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TwitterService twitterService;

    @Mock
    private TwitterAuthenticator authenticator;

    private static String search = "?track=bieber";

    private static
    Run run;

    static {
        try {
            run = objectMapper.readValue(new File("src/test/resources/run.json"), Run.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setUp() throws IOException, ExecutionException, InterruptedException, TwitterAuthenticationException {

        Mockito.lenient().when(twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, search)).thenCallRealMethod();

        Mockito.lenient().when(twitterService.parseTweetsFrom(any(), anyString()))
                .thenReturn(run);

        Mockito.lenient().when(twitterService.createRun(any(), anyLong(), anyString(), anyString())).thenReturn(run);

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
    public void should_create_run() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run run = twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, search);

        assertNotNull(run.getRunId());

    }

}
