/**
 *
 */
package org.cambi.dao;

import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author luca
 *
 */
@Component
public interface UserTweetDao extends JpaRepository<UserTweet, UserTweetId> {

    @Query("from UserTweet ut inner join Run r on ut.run.runId = r.runId where r.runId = :runId")
    public Set<UserTweet> findByRun(@Param("runId") Long runId, Sort sort);
}
