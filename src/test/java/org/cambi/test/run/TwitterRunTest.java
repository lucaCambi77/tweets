package org.cambi.test.run;

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
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.RunRepository;
import org.cambi.repository.TweetRepository;
import org.cambi.repository.UserRepository;
import org.cambi.service.ITwitterService;
import org.cambi.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, ApplicationConfigurationTest.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/test.properties")
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = {"dataSource"}, dataSetLoader = JsonDataSetLoader.class)
// TODO Dbunit schema handling, @DatabaseTearDown not working
public class TwitterRunTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterRunTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TweetRepository twitterRepository;

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ITwitterService twitterService;

    @Autowired
    private TwitterAuthenticator authenticator;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL)
    @Transactional()
    public void should_create_run_from_tweet_request()
            throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {
        Date start = new Date();

        Run response = twitterService.parseTweetsFromRequest(authenticator.getAuthorizedHttpRequestFactory(),
                DEFAULT_API.concat("?track=bieber"));

        assertTrue(response.getTweetRuns().size() == 5);

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(objectMapper.writeValueAsString(response));

        log.info(objectMapper.writeValueAsString(Utils.sortTweets(response.getTweetRuns())));

        log.info(" **** ***   **   *** **** **** **** ");
        log.info("  **   *** **** ***  **   **    **  ");
        log.info("  **    ****  ****   **** ****  **  ");

        twitterService.createRun(response, new Date().getTime() - start.getTime(), DEFAULT_API, "?track=bieber");

        log.info("We have a new Run");
        /**
         * We have a new Run
         */
        List<Run> runs = twitterService.findAllRun();
        assertEquals(1, runs.size());

        log.info("We have new Tweets");

        /**
         * We have 5 tweets
         */
        Set<TweetRun> tweets = runs.get(0).getTweetRuns();
        assertEquals(5, tweets.size());

        /**
         * We have only 3 users plus 2 empty
         */
        List<UserTweet> users = tweets.stream()
                .map(t -> t.getUserTweets()).filter(u -> u != null).collect(Collectors.toList());

        assertEquals(3, users.size());

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

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        log.info(objectMapper.writeValueAsString(aRun.get(0)));
    }
}
