/**
 *
 */
package org.cambi.test.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.dto.TweetDto;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.test.Utils;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author luca
 *
 */
@Configuration
@Profile("test")
public class ApplicationConfigurationTest {
    @Mock
    private TwitterAuthenticator authenticator;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public TwitterAuthenticator getTwitterAuthenticator() throws TwitterAuthenticationException, IOException {
        MockitoAnnotations.initMocks(this);

        List<TweetDto> tweets = objectMapper
                .readValue(new File("src/test/resources/tweets.json"), new TypeReference<List<TweetDto>>() {
                });

        when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(Utils.getFakeRequestFactory(new HashSet<>(tweets)));
        return authenticator;
    }

}
