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
        UserTweet user = new UserTweet();
        user.setTweetRuns(savedTweet);
        user.setCreationDate(tweetRun.getUserTweet().getCreationDate());
        user.setUserName(tweetRun.getUserTweet().getUserName());
        user.setUserScreenName(tweetRun.getUserTweet().getUserScreenName());
        user.setId(new UserTweetId(tweetRun.getUserTweet().getId().getUserId(), savedTweet.getId()));
        save(user);

        return user;
    }
}
