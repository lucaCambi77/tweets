package org.cambi.test.dao;

import org.cambi.constant.Constant;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterDaoUnitTest extends Constant {

    @Mock
    private TweetDao twitterDao;

    @Mock
    private RunDao runDao;

    @Mock
    private UserTweetDao userDao;

    private static String search = "?track=bieber";

    private static Run run;
    private static UserTweet userTweets;
    private static TweetRun tweets;

    static {

        tweets = TweetRun.builder()
                .messageId(new BigInteger("1"))
                .creationDate(new Date())
                .messageText("This is a tweet")
                .build();

        run =
                Run.builder()
                        .runId(1L)
                        .api(DEFAULT_API).
                        apiQuery(search).
                        ins(new Date()).
                        numTweet(1)
                        .runTime(10)
                        .build();

        userTweets = UserTweet.builder()
                .id(new UserTweetId(new BigInteger("1"), tweets))
                .userName("Name")
                .userScreenName("ScreenShot")
                .run(run).build();
    }


    @BeforeEach
    public void setUp() {

        Mockito.lenient().when(runDao.findAll()).thenReturn(Arrays.asList(run));
        Mockito.lenient().when(twitterDao.findAll()).thenReturn(Arrays.asList(tweets));
        Mockito.lenient().when(userDao.findAll()).thenReturn(Arrays.asList(userTweets));

    }

    @Test
    public void should_match_created_run() {

        List<Run> runs = runDao.findAll();
        assertEquals(1, runs.size());
        assertNotNull(runs.get(0).getRunId());

        List<TweetRun> tweets = twitterDao.findAll();
        assertEquals(1, tweets.size());
        assertNotNull(tweets.get(0).getMessageId());

        List<UserTweet> users = userDao.findAll();
        assertEquals(1, users.size());
        assertNotNull(users.get(0).getId().getUserId());
        assertNotNull(users.get(0).getId().getMessageId());
    }

}
