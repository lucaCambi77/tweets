/**
 *
 */
package org.cambi.utils;

import org.cambi.dto.TweetDto;
import org.cambi.dto.UserTweetDto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luca
 *
 */
public class Utils {

    public static Map<UserTweetDto, List<TweetDto>> sortTweets(Set<TweetDto> tweets) {

        Map<UserTweetDto, List<TweetDto>> tweetByUser = tweets.stream().sorted(new Comparator<TweetDto>() {
            @Override
            public int compare(final TweetDto lhs, TweetDto rhs) {
                long dateleft = lhs.getUserTweet() == null ? 0 : lhs.getUserTweet().getCreationDate().getTime();
                long dateRight = rhs.getUserTweet() == null ? 0 : rhs.getUserTweet().getCreationDate().getTime();

                return Long.signum(dateleft - dateRight);
            }
        }).collect(Collectors.groupingBy(TweetDto::getUserTweet));

        for (List<TweetDto> userTweetList : tweetByUser.values()) {
            userTweetList.sort(new Comparator<TweetDto>() {
                @Override
                public int compare(final TweetDto lhs, TweetDto rhs) {
                    return Long.signum(lhs.getCreationDate().getTime() - rhs.getCreationDate().getTime());
                }
            });
        }

        return tweetByUser;
    }

}
