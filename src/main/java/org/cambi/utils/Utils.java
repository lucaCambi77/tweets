/**
 *
 */
package org.cambi.utils;

import org.cambi.model.TweetRun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luca
 *
 */
public class Utils {

	/**
	 * @param tweets
	 * @return
	 */
	public static LinkedHashMap<Object, List<TweetRun>> sortTweets(Set<TweetRun> tweets) {

		LinkedHashMap<Object, List<TweetRun>> tweetByUser = tweets.stream().sorted(new Comparator<TweetRun>() {
			@Override
			public int compare(final TweetRun lhs, TweetRun rhs) {
				long dateleft = lhs.getUserTweet() == null ? 0 : lhs.getUserTweet().getCreationDate().getTime();
				long dateRight = rhs.getUserTweet() == null ? 0 : rhs.getUserTweet().getCreationDate().getTime();

				return Long.signum(dateleft - dateRight);
			}
		}).collect(Collectors.groupingBy(
				p -> Optional.ofNullable(p.getUserTweet() == null ? null : p.getUserTweet().getId().getUserId()),
				LinkedHashMap::new, Collectors.toList()));

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
