package org.cambi.test.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cambi.constant.Constant;
import org.cambi.dto.TweetDto;

import java.util.List;

/**
 * Tweets wrapper for tests
 *
 * @author luca
 */
public class StatusRun extends Constant {

	@JsonProperty("statuses")
	private List<TweetDto> tweets;

	public List<TweetDto> getTweets() {
		return tweets;
	}
}
