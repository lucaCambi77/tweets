/**
 * 
 */
package org.cambi.repository;

import java.util.List;

import org.cambi.model.TweetRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component
public interface TweetRepository extends JpaRepository<TweetRun, Long> {

	@Query(value = "SELECT t FROM TweetRun t WHERE t.run.runId = ?1")
	public List<TweetRun> getByRun(Long runId);
}
