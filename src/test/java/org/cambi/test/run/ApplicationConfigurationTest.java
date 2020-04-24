/**
 *
 */
package org.cambi.test.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.cambi.model.TweetRun;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.test.config.StatusRun;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.*;

import static org.mockito.Mockito.when;

/**
 * @author luca
 *
 */
@Configuration
@Profile("test")
public class ApplicationConfigurationTest
{
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
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {

						InputStream is = new FileInputStream("src/test/resources/tweets.json");
						BufferedReader buf = new BufferedReader(new InputStreamReader(is));

						String line = buf.readLine();
						StringBuilder sb = new StringBuilder();

						while (line != null) {
							sb.append(line).append("\n");
							line = buf.readLine();
						}
						buf.close();

						StatusRun status = objectMapper.readValue(sb.toString(), StatusRun.class);
						sb = new StringBuilder();

						for (TweetRun tweet : status.getTweets()) {
							sb.append(objectMapper.writeValueAsString(tweet)).append("\n");
						}

						MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
						response.setStatusCode(200);
						response.setContentType(Json.MEDIA_TYPE);
						response.setContent(sb.toString());
						return response;
					}
				};
			}
		};

		when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(transport.createRequestFactory());
		return authenticator;
	}

}
