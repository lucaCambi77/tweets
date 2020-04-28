/**
 *
 */
package org.cambi.dto;

import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luca
 *
 */
public class RunDto {

    private Run run;

    private Map<Optional<UserTweet>, List<TweetRun>> userTweets;

}
