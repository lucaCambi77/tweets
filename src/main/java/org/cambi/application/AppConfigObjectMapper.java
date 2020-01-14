/**
 * 
 */
package org.cambi.application;

import org.cambi.dto.ObjectMapperFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author luca
 *
 */
@Configuration
@Profile({ "production", "test" })
public class AppConfigObjectMapper {


	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ObjectMapper getObjectMapper() {
		return new ObjectMapperFactory().getObjectMapper();
	}

}
