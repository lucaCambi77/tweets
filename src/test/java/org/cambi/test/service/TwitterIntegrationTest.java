package org.cambi.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.cambi.application.Application;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterRunService;
import org.cambi.service.UserTweetsService;
import org.cambi.test.config.ApplicationConfigurationTest;
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

@SpringBootTest(classes = {Application.class, ApplicationConfigurationTest.class})
@TestPropertySource(locations = "/test.properties")
@ActiveProfiles("test")
@Slf4j
public class TwitterIntegrationTest extends Constant {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TwitterRunService twitterRunService;

    @Autowired
    private UserTweetsService userTweetsService;

    @Autowired
    private TwitterAuthenticator authenticator;

    @Test
    @Transactional
    public void should_create_run() throws IOException, ExecutionException, InterruptedException {

        Run run = twitterRunService.createRun(DEFAULT_API, "query");
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        log.info(objectMapper.writeValueAsString(run));

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        log.info("We have a new Run");

        assertEquals(1, twitterRunService.findAllRun().size());
        Run persistedRun = twitterRunService.findAllRun().get(0);
        assertEquals(run, persistedRun);

        assertEquals(5, userTweetsService.findUserTweetsByRun(persistedRun.getRunId()).size());
    }

    @Test
    @Sql({"/sample.sql"})
    @Transactional(readOnly = true)
    public void should_match_database_status() throws Exception {
        List<Run> aRun = twitterRunService.findAllRun();

        assertEquals(1, aRun.size());
        assertEquals(1, aRun.get(0).getRunId());
        assertEquals("?track=trump", aRun.get(0).getApiQuery());
        assertEquals(1, aRun.get(0).getUserTweets().size());
    }
}
