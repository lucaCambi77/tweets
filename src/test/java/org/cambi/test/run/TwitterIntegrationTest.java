package org.cambi.test.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.cambi.application.Application;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.ITwitterService;
import org.cambi.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {Application.class, ApplicationConfigurationTest.class})
@TestPropertySource(locations = "/test.properties")
@ActiveProfiles("test")
@Slf4j
public class TwitterIntegrationTest extends Constant {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ITwitterService twitterService;

    @Autowired
    private TwitterAuthenticator authenticator;

    @Test
    @Transactional
    public void should_create_run_from_tweet_request()
            throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run run = twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API, "?track=bieber");

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(objectMapper.writeValueAsString(run));

        log.info(objectMapper.writeValueAsString(Utils.sortTweets(run.getTweetRuns())));

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        log.info("We have a new Run");

        assertEquals(1, twitterService.findAllRun().size());
    }

    @Test
    @Sql({"/sample.sql"})
    @Transactional
    public void should_match_database_status() throws Exception {
        List<Run> aRun = twitterService.findAllRun();

        assertEquals(1, aRun.size());
        assertEquals(1, aRun.get(0).getRunId());
        assertEquals("?track=trump", aRun.get(0).getApiQuery());
        assertEquals(1, aRun.get(0).getTweetRuns().size());
        assertNotNull(aRun.get(0).getTweetRuns().iterator().next().getUserTweet().getId());

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(objectMapper.writeValueAsString(aRun.get(0)));
    }
}
