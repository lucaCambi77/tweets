/**
 * 
 */
package org.cambi.utils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.cambi.model.TweetRun;

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
				long dateleft = lhs.getUserTweets() == null ? 0 : lhs.getUserTweets().getCreationDate().getTime();
				long dateRight = rhs.getUserTweets() == null ? 0 : rhs.getUserTweets().getCreationDate().getTime();

				return Long.signum(dateleft - dateRight);
			}
		}).collect(Collectors.groupingBy(
				p -> Optional.ofNullable(p.getUserTweets() == null ? null : p.getUserTweets().getId().getUserId()),
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
