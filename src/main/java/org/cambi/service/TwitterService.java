package org.cambi.service;

import com.google.api.client.http.HttpRequestFactory;
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
import org.cambi.model.UserTweetId;
import org.cambi.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class TwitterService extends Constant implements ITwitterService {

    private static final Logger log = LoggerFactory.getLogger(TwitterService.class);

    @Autowired
    private TwitterServiceRunnable runnable;

    @Autowired
    private TweetDao tweetDao;

    @Autowired
    private RunDao runDao;

    @Autowired
    private UserTweetDao userDao;

    public RunDto parseTweetsFrom(HttpRequestFactory httpRequestFactory, String path)
            throws InterruptedException, ExecutionException {
        log.info("I am looking for tweets...");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(runnable.setPath(path).setAuthenticator(httpRequestFactory));

        RunDto run = new RunDto();
        try {
            future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } finally {

            run.setTweets(runnable.getTweets());
            run.setException(runnable.getException());
        }

        executor.shutdownNow();
        return run;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Run> findAllRun() {
        return runDao.findAll(Sort.by(Sort.Direction.DESC, "runTime"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTweet> findUserTweetsByRun(Long runId) {
        return userDao.findByRun(runId, Sort.by(Sort.Order.asc("creationDate")
                , Sort.Order.asc("id.messageId")));
    }

    @Override
    public Run createRun(HttpRequestFactory authorizedHttpRequestFactory, String api, String query) throws ExecutionException, InterruptedException {
        Date start = new Date();

        RunDto response = this.parseTweetsFrom(authorizedHttpRequestFactory,
                api.concat(query));

        return this.createRun(response.getTweets(), new Date().getTime() - start.getTime(), api, query);
    }

    @Transactional
    public Run createRun(Set<TweetDto> tweetDto, Long elapse, String endPoint, String query) {

        Map<UserTweetDto, List<TweetDto>> tweets = Utils.tweetsToUserTweet(tweetDto);

        Run savedRun = runDao.save(
                Run.builder()
                        .api(endPoint).
                        apiQuery(query).
                        ins(new Date()).
                        numTweet(tweetDto.size())
                        .runTime(elapse)
                        .build());

        for (Map.Entry<UserTweetDto, List<TweetDto>> listByUser : tweets.entrySet()) {

            UserTweet user = UserTweet.builder()
                    .userName(listByUser.getKey().getUserName())
                    .userScreenName(listByUser.getKey().getUserScreenName())
                    .creationDate(listByUser.getKey().getCreationDate())
                    .run(savedRun)
                    .build();

            for (TweetDto tweet : listByUser.getValue()) {

                TweetRun tweetPost = TweetRun.builder()
                        .creationDate(tweet.getCreationDate())
                        .messageText(tweet.getMessageText())
                        .messageId(tweet.getId())
                        .build();

                tweetDao.save(tweetPost);

                userDao.save(
                        user.toBuilder()
                                .id(new UserTweetId(listByUser.getKey().getId(), tweetPost)).build());

            }

        }

        return savedRun;
    }

}
