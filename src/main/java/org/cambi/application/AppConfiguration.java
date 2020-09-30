/**
 *
 */
package org.cambi.application;

import org.cambi.constant.Constant;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "production" })
public class AppConfiguration extends Constant {

	@Bean
	public TwitterAuthenticator getTwitterAuthenticator() {
		return new TwitterAuthenticator(CONSUMER_KEY, CONSUMER_SECRET);
	}

}
