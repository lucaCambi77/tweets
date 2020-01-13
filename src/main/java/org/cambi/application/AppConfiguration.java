/**
 * 
 */
package org.cambi.application;

import org.cambi.constant.Constant;
import org.cambi.dto.ObjectMapperFactory;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.UserRepository;
import org.cambi.service.TwitterService;
import org.cambi.service.TwitterServiceRunnable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author luca
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableTransactionManagement
@EntityScan(basePackageClasses = UserTweet.class)
@ComponentScan(basePackageClasses = TwitterService.class)
@Profile({ "production" })
public class AppConfiguration extends Constant {

	@Bean
	public TwitterAuthenticator getTwitterAuthenticator() {
		return new TwitterAuthenticator(CONSUMER_KEY, CONSUMER_SECRET);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ObjectMapper getObjectMapper() {
		return new ObjectMapperFactory().getObjectMapper();
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public TwitterServiceRunnable getTwitterServiceRunnable() {
		return new TwitterServiceRunnable();
	}
}
