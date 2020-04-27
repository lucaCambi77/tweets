package org.cambi.service;

import com.google.api.client.http.HttpRequestFactory;
import org.cambi.constant.Constant;
import org.cambi.dao.RunDao;
import org.cambi.dao.TweetDao;
import org.cambi.dao.UserTweetDao;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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

    public Run parseTweetsFrom(HttpRequestFactory httpRequestFactory, String path)
            throws InterruptedException, ExecutionException {
        log.info("I am looking for tweets...");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(runnable.setPath(path).setAuthenticator(httpRequestFactory));

        Run run = new Run();
        try {
            future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } finally {

            Set<TweetRun> tweets = runnable.getTweets();

            run.setTweetRuns(tweets);
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
    public Run createRun(HttpRequestFactory authorizedHttpRequestFactory, String api, String query) throws ExecutionException, InterruptedException {
        Date start = new Date();

        Run response = this.parseTweetsFrom(authorizedHttpRequestFactory,
                api.concat(query));

        return this.createRun(response, new Date().getTime() - start.getTime(), api, query);

    }

    @Transactional
    public Run createRun(Run run, Long elapse, String endPoint, String query) {

        Set<TweetRun> tweetDto = run.getTweetRuns();

        Run savedRun = runDao.saveRun(run, elapse, endPoint, query, tweetDto.size());

        for (TweetRun tweetRun : tweetDto) {
            TweetRun savedTweet = tweetDao.saveTweets(tweetRun, savedRun);

            if (null != tweetRun.getUserTweet())
                userDao.saveUserTweet(tweetRun, savedTweet);
        }

        return savedRun;
    }


}
