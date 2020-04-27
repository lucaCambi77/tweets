package org.cambi.test.tweets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterDaoTest extends Constant {

    private static final Logger log = LoggerFactory.getLogger(TwitterDaoTest.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TweetDao twitterDao;

    @Mock
    private RunDao runDao;

    @Mock
    private UserTweetDao userDao;

    @BeforeEach
    public void setUp() throws IOException {

        String path = "src/test/resources/run.json";

        File file = new File(path);

        Run run = objectMapper.readValue(file, Run.class);

        Mockito.lenient().when(runDao.findAll()).thenReturn(Arrays.asList(run));
        Mockito.lenient().when(twitterDao.findAll()).thenReturn(run.getTweetRuns()
                .stream().collect(Collectors.toList()));
        Mockito.lenient().when(userDao.findAll()).thenReturn(run.getTweetRuns().stream()
                .map(t -> t.getUserTweet()).filter(u -> u != null).collect(Collectors.toList()));
    }

    @Test
    public void should_create_run() throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        List<Run> runs = runDao.findAll();
        assertEquals(1, runs.size());

        List<TweetRun> tweets = twitterDao.findAll();
        assertEquals(5, tweets.size());

        tweets.stream().filter(t -> t.getUserTweet() != null).forEach(t -> {
                    assertEquals(t.getId(),
                            t.getUserTweet().getId().getMessageId());
                }
        );

        List<UserTweet> users = userDao.findAll();
        assertEquals(3, users.size());

        users.forEach(u -> {
            assertNotNull(u.getId());
        });

    }

}
