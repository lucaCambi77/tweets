package org.cambi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Serialize class for readed tweets.
 *
 * @author luca
 */
@Entity
@Table(name = "TWEET_RUN")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TweetRun implements java.io.Serializable {

    private BigInteger messageId;
    private Date creationDate;
    private String messageText;
    private Set<UserTweet> userTweets = new HashSet<>(0);

    @Id
    @Column(nullable = false, precision = 50, scale = 0)
    public BigInteger getMessageId() {
        return messageId;
    }

    public void setMessageId(BigInteger messageId) {
        this.messageId = messageId;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(nullable = false, length = 7)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Column(nullable = true, length = 4200)
    public String getMessageText() {
        return messageText;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.messageId")
    public Set<UserTweet> getUserTweets() {
        return userTweets;
    }

    public void setUserTweets(Set<UserTweet> userTweets) {
        this.userTweets = userTweets;
    }

    @Override
    public String toString() {
        return "Tweet: [ messageId : " + getMessageId() + ",created at : " + creationDate + ", text : " + messageText + "]";
    }
}
