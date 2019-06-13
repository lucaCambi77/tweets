package org.cambi.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * UserTweetId
 */
@Embeddable
public class UserTweetId implements java.io.Serializable {

	private BigInteger userId;
	private BigInteger messageId;

	public UserTweetId() {
	}

	public UserTweetId(BigInteger userId, BigInteger messageId) {
		this.userId = userId;
		this.messageId = messageId;
	}

	@Column(name = "C_USE_ID", nullable = false, precision = 50, scale = 0)
	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger runId) {
		this.userId = runId;
	}

	@Column(name = "C_MES_ID", nullable = false, precision = 50, scale = 0)
	public BigInteger getMessageId() {
		return messageId;
	}

	public void setMessageId(BigInteger messageId) {
		this.messageId = messageId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserTweetId))
			return false;
		UserTweetId castOther = (UserTweetId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
				&& ((this.getMessageId() == castOther.getMessageId()) || (this.getMessageId() != null
						&& castOther.getMessageId() != null && this.getMessageId().equals(castOther.getMessageId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result + (getMessageId() == null ? 0 : this.getMessageId().hashCode());
		return result;
	}

}
