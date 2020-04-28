package org.cambi.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigInteger;

/**
 * UserTweetId
 */
@Embeddable
public class UserTweetId implements java.io.Serializable {

    private BigInteger userId;
    private BigInteger messageId;
    private Long runId;

    public UserTweetId() {
    }

    public UserTweetId(BigInteger userId, BigInteger messageId, Long runId) {
        this.userId = userId;
        this.messageId = messageId;
        this.runId = runId;
    }

    @Column(nullable = false, precision = 50, scale = 0)
    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger runId) {
        this.userId = runId;
    }

    @Column(nullable = false, precision = 50, scale = 0)
    public BigInteger getMessageId() {
        return messageId;
    }

    @Column(nullable = false, precision = 50, scale = 0)
    public Long getRunId() {
        return runId;
    }

    public void setMessageId(BigInteger messageId) {
        this.messageId = messageId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
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
