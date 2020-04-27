/**
 *
 */
package org.cambi.dao;

import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luca
 *
 */
@Component
public interface TweetDao extends JpaRepository<TweetRun, Long> {

    @Query(value = "SELECT t FROM TweetRun t WHERE t.run.runId = ?1")
    public List<TweetRun> getByRun(Long runId);

    default TweetRun saveTweets(TweetRun aTweet, Run savedRun) {

        TweetRun tweetRun = new TweetRun();
        tweetRun.setCreationDate(aTweet.getCreationDate());
        tweetRun.setMessageText(aTweet.getMessageText());
        tweetRun.setRun(savedRun);

        return save(tweetRun);
    }
}

