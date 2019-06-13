/**
 * 
 */
package org.cambi.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.cambi.constant.Constant;
import org.cambi.model.TweetRun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author luca
 *
 */
public class RunDto extends Constant {

	@JsonIgnore
	private List<TweetRun> tweets;

	@JsonProperty(TWEETS)
	private Map<BigInteger, List<TweetRun>> tweetsByUser;

	@JsonProperty(EXCEPTION)
	private String exception = "";

	public Map<BigInteger, List<TweetRun>> getTweetsByUser() {
		return tweetsByUser;
	}

	public void setTweetsByUser(Map<BigInteger, List<TweetRun>> tweets) {
		this.tweetsByUser = tweets;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public List<TweetRun> getTweets() {
		return tweets;
	}

	public void setTweets(List<TweetRun> tweets) {
		this.tweets = tweets;
	}

}
