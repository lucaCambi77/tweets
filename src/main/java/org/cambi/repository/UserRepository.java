/**
 * 
 */
package org.cambi.repository;

import org.cambi.model.UserTweet;
import org.cambi.model.UserTweetId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author luca
 *
 */
@Component
public interface UserRepository extends JpaRepository<UserTweet, UserTweetId> {

}
