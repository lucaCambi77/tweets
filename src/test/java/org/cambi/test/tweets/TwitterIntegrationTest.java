package org.cambi.test.tweets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.cambi.application.Application;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.ITwitterService;
import org.cambi.test.config.ApplicationConfigurationTest;
import org.cambi.test.config.dbunit.JsonDataSetLoader;
import org.cambi.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, ApplicationConfigurationTest.class})
@TestPropertySource(locations = "/test.properties")
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = {"dataSource"}, dataSetLoader = JsonDataSetLoader.class)
// TODO Dbunit schema handling, @DatabaseTearDown not working
public class TwitterIntegrationTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterIntegrationTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ITwitterService twitterService;

    @Autowired
    private TwitterAuthenticator authenticator;

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL)
    @Transactional()
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

        assertNotNull(run.getRunId());
    }

    @Test
    @DatabaseSetups({
            @DatabaseSetup(type = DatabaseOperation.DELETE_ALL),
            @DatabaseSetup(value = "classpath:sample.json", connection = "dataSource", type = DatabaseOperation.INSERT)

    })
    @Transactional(readOnly = true)
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
