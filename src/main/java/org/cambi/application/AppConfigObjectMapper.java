/**
 *
 */
package org.cambi.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.dto.ObjectMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luca
 *
 */
@Configuration
public class AppConfigObjectMapper {

	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapperFactory().getObjectMapper();
	}

}
