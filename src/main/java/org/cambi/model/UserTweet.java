package org.cambi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
    private Run run;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false, precision = 50, scale = 0)),
            @AttributeOverride(name = "messageId", column = @Column(name = "messageId", nullable = false, precision = 50, scale = 0))})
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

    public void setRun(Run run) {
        this.run = run;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Run.class)
    @JsonIgnore
    public Run getRun() {
        return run;
    }

    @Override
    public String toString() {
        return "Tweet: [ messageId : " + getId() + ", created at: " + creationDate + ", text : " + userName + "]";
    }
}
