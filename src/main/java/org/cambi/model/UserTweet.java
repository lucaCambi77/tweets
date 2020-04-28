package org.cambi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Serialize class for readed tweets.
 *
 * @author luca
 */
@Entity
@Table(name = "USER_TWEET_RUN")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserTweet implements java.io.Serializable {

    private UserTweetId id;
    private Date creationDate;
    private String userName;
    private String userScreenName;
    private Set<TweetRun> tweetRuns;
    private Run run;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, precision = 50, scale = 0)),
            @AttributeOverride(name = "messageId", column = @Column(name = "messageId", nullable = false, precision = 50, scale = 0)),
            @AttributeOverride(name = "runId", column = @Column(name = "runId", nullable = false, precision = 50, scale = 0))})
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
    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    @Column(nullable = false, length = 7)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "run", targetEntity = TweetRun.class)
    public Set<TweetRun> getTweetRuns() {
        return tweetRuns;
    }

    public void setTweetRuns(Set<TweetRun> tweetRuns) {
        this.tweetRuns = tweetRuns;
    }

    @Override
    public String toString() {
        return "Tweet: [ messageId : " + getId() + ", created at: " + creationDate + ", text : " + userName + "]";
    }
}
