package org.cambi.service;

import com.google.api.client.http.HttpRequestFactory;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.cambi.repository.RunRepository;
import org.cambi.repository.TweetRepository;
import org.cambi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class TwitterService extends Constant implements ITwitterService {

	private static final Logger log = LoggerFactory.getLogger(TwitterService.class);

	@Autowired
	private TwitterServiceRunnable runnable;

	@Autowired
	private TweetRepository twitterRepository;

	@Autowired
	private RunRepository runRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Parse Tweet method to return a Run to persist
	 *
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Override
	public Run parseTweetsFromRequest(HttpRequestFactory httpRequestFactory, String path)
			throws IOException, InterruptedException, ExecutionException {
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
	public Run createRun(Run runDto, Long elapse, String endPoint, String query) {

		Set<TweetRun> tweetDto = runDto.getTweetRuns();
		/**
		 * New Run
		 */
		Run newRun = new Run();

		newRun.setApi(endPoint);
		newRun.setApiQuery(query);
		newRun.setIns(new Date());
		newRun.setNumTweet(tweetDto.size());
		newRun.setRunTime(elapse);
		newRun.setException(runDto.getException());

		runRepository.save(newRun);

		/**
		 * Tweets
		 */
		tweetDto.forEach(aTweet -> {
			TweetRun tweetRun = new TweetRun();
			tweetRun.setCreationDate(aTweet.getCreationDate());
			tweetRun.setMessageText(aTweet.getMessageText());

			tweetRun.setRun(newRun);
			twitterRepository.save(tweetRun);

			/**
			 * Users
			 */
			if (null != aTweet.getUserTweets()) {

				UserTweet user = new UserTweet();
				user.setCreationDate(aTweet.getUserTweets().getCreationDate());
				user.setTweetRuns(tweetRun);
				user.setUserName(aTweet.getUserTweets().getUserName());
				user.setUserSreenName(aTweet.getUserTweets().getUserSreenName());
				user.setId(new UserTweetId(aTweet.getUserTweets().getId().getUserId(), tweetRun.getId()));

				userRepository.save(user);

			}
		});

		return newRun;
	}
}
