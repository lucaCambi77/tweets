/**
 *
 */
package org.cambi.dao;

import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component
public interface UserTweetDao extends JpaRepository<UserTweet, UserTweetId> {

}
