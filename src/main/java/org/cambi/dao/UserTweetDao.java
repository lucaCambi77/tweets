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


    default UserTweet saveUserTweet(TweetRun tweetRun, TweetRun savedTweet) {

        return
                save(UserTweet.builder()
                        .id(new UserTweetId(tweetRun.getUserTweet().getId().getUserId(), savedTweet.getId()))
                        .userName(tweetRun.getUserTweet().getUserName())
                        .userScreenName(tweetRun.getUserTweet().getUserScreenName())
                        .creationDate(tweetRun.getUserTweet().getCreationDate())
                        .build());

    }
}
