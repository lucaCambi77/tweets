package org.cambi.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Serialize class for readed tweets.
 *
 * @author luca
 */
@Entity
@Table(name = "TWEET_RUN")
public class TweetRun implements java.io.Serializable {

    private BigInteger id;
    private Run run;
    private Date creationDate;
    private String messageText;
    private UserTweet userTweet;

    public TweetRun() {
    }

    public TweetRun(BigInteger id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "messagegenerator")
    @Column(nullable = false, precision = 50, scale = 0)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, insertable = true, updatable = false, name = "runId")
    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
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

    @OneToOne(mappedBy = "tweetRuns", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    public UserTweet getUserTweet() {
        return userTweet;
    }

    public void setUserTweet(UserTweet userTweets) {
        this.userTweet = userTweets;
    }

    @Override
    public String toString() {
        return "Tweet: [ messageId : " + getId() + ",created at : " + creationDate + ", text : " + messageText + "]";
    }
}
