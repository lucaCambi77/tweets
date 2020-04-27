/**
 *
 */
package org.cambi.utils;

import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luca
 *
 */
public class Utils {

    public static Map<Optional<UserTweet>, List<TweetRun>> sortTweets(Set<TweetRun> tweets) {

        Map<Optional<UserTweet>, List<TweetRun>> tweetByUser = tweets.stream().sorted(new Comparator<TweetRun>() {
            @Override
            public int compare(final TweetRun lhs, TweetRun rhs) {
                long dateleft = lhs.getUserTweet() == null ? 0 : lhs.getUserTweet().getCreationDate().getTime();
                long dateRight = rhs.getUserTweet() == null ? 0 : rhs.getUserTweet().getCreationDate().getTime();

                return Long.signum(dateleft - dateRight);
            }
        }).collect(Collectors.groupingBy(p -> Optional.ofNullable(p.getUserTweet())));

        for (List<TweetRun> userTweetList : tweetByUser.values()) {
            userTweetList.sort(new Comparator<TweetRun>() {
                @Override
                public int compare(final TweetRun lhs, TweetRun rhs) {
                    return Long.signum(lhs.getCreationDate().getTime() - rhs.getCreationDate().getTime());
                }
            });
        }

        return tweetByUser;
    }

}
