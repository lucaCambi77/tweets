package org.cambi.model;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Serialize class for readed tweets.
 *
 * @author luca
 */
@Entity
//@Table(name = "USER_TWEET_RUN", schema = "TWEET")
public class UserTweet implements java.io.Serializable {

	private UserTweetId id;
	private Date creationDate;
	private String userName;
	private String userSreenName;
	private TweetRun tweetRuns;

	public UserTweet() {
	}

	public UserTweet(UserTweetId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, precision = 50, scale = 0)),
			@AttributeOverride(name = "messageId", column = @Column(name = "messageId", nullable = false, precision = 50, scale = 0)) })
	public UserTweetId getId() {
		return id;
	}

	public void setId(UserTweetId id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(nullable = false)
	public String getUserSreenName() {
		return userSreenName;
	}

	public void setUserSreenName(String userSreenName) {
		this.userSreenName = userSreenName;
	}

	@Column(nullable = false, length = 7)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "messageId", insertable = false, updatable = false)
	public TweetRun getTweetRuns() {
		return tweetRuns;
	}

	public void setTweetRuns(TweetRun tweetRuns) {
		this.tweetRuns = tweetRuns;
	}

	@Override
	public String toString() {
		return "Tweet: [ messageId : " + getId() + ", created at: " + creationDate + ", text : " + userName + "]";
	}
}
