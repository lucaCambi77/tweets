/**
 *
 */
package org.cambi.application;

import org.cambi.constant.Constant;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.TwitterServiceRunnable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * @author luca
 *
 */
@Configuration
@Profile({ "production" })
public class AppConfiguration extends Constant {

	@Bean
	public TwitterAuthenticator getTwitterAuthenticator() {
		return new TwitterAuthenticator(CONSUMER_KEY, CONSUMER_SECRET);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public TwitterServiceRunnable getTwitterServiceRunnable() {
		return new TwitterServiceRunnable();
	}
}
