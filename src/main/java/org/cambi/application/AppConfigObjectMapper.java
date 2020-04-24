/**
 *
 */
package org.cambi.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.dto.ObjectMapperFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author luca
 *
 */
@Configuration
public class AppConfigObjectMapper {


	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ObjectMapper getObjectMapper() {
		return new ObjectMapperFactory().getObjectMapper();
	}

}
