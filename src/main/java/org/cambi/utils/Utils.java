/**
 *
 */
package org.cambi.utils;

import org.cambi.dto.TweetDto;
import org.cambi.dto.UserTweetDto;
import org.cambi.model.UserTweet;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luca
 *
 */
public class Utils {

    public static Map<UserTweetDto, List<TweetDto>> tweetsToUserTweet(Set<TweetDto> tweets) {
        return tweets.stream()
                .collect(Collectors.groupingBy(TweetDto::getUserTweet));
    }

    public static LinkedHashMap<BigInteger, List<UserTweet>> groupByUserTweets(Set<UserTweet> userTweets) {
        return userTweets.stream()
                .collect(Collectors.groupingBy(u -> u.getId().getUserId(), LinkedHashMap::new, Collectors.toList()));

    }
}
