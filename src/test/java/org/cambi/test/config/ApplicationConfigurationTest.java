/**
 *
 */
package org.cambi.test.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.cambi.dto.TweetDto;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
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

        /**
         * We read tweets from a file to create a fake request
         *
         * Mocking google Transport
         */
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {

                        try {

                            List<TweetDto> tweets = objectMapper
                                    .readValue(new File("src/test/resources/tweets.json"), new TypeReference<List<TweetDto>>() {
                                    });

                            StringBuilder sb = new StringBuilder();

                            for (TweetDto tweet : tweets) {
                                sb.append(objectMapper.writeValueAsString(tweet)).append("\n");
                            }

                            MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                            response.setStatusCode(200);
                            response.setContentType(Json.MEDIA_TYPE);
                            response.setContent(sb.toString());
                            return response;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };

        when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(transport.createRequestFactory());
        return authenticator;
    }

}
