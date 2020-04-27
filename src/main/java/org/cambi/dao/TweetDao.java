/**
 *
 */
package org.cambi.dao;

import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author luca
 *
 */
@Component
public interface TweetDao extends JpaRepository<TweetRun, Long> {

    @Query(value = "SELECT t FROM TweetRun t WHERE t.run.runId = ?1")
    public List<TweetRun> getByRun(Long runId);

    default TweetRun saveTweets(Date creationDate, String messageText, Run savedRun) {

        return save(
                TweetRun.builder()
                        .creationDate(creationDate)
                        .messageText(messageText)
                        .run(savedRun)
                        .build());
    }
}

