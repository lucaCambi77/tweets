/**
 *
 */
package org.cambi.dao;

import org.cambi.model.TweetRun;
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


    default UserTweet saveUserTweet(UserTweet userTweet, TweetRun savedTweet) {

        return
                save(UserTweet.builder()
                        .id(new UserTweetId(userTweet.getId().getUserId(), savedTweet.getId()))
                        .userName(userTweet.getUserName())
                        .userScreenName(userTweet.getUserScreenName())
                        .creationDate(userTweet.getCreationDate())
                        .build());

    }
}
