package org.cambi.test.config;

import java.util.List;

import org.cambi.constant.Constant;
import org.cambi.model.TweetRun;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Tweets wrapper for tests
 *
 * @author luca
 */
public class StatusRun extends Constant {

	@JsonProperty("statuses")
	private List<TweetRun> tweets;

	public List<TweetRun> getTweets() {
		return tweets;
	}
}
