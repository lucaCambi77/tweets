/**
 *
 */
package org.cambi.dao;

import org.cambi.model.TweetRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component
public interface TweetDao extends JpaRepository<TweetRun, Long> {

}

