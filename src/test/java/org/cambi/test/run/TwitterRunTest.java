package org.cambi.test.run;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.cambi.application.AppConfiguration;
import org.cambi.application.Application;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.TweetRun;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.RunRepository;
import org.cambi.repository.TweetRepository;
import org.cambi.repository.UserRepository;
import org.cambi.service.ITwitterService;
import org.cambi.test.config.StatusRun;
import org.cambi.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { Application.class, AppConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(OrderAnnotation.class)
public class TwitterRunTest extends Constant {

	private static final Logger log = LoggerFactory.getLogger(TwitterRunTest.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TweetRepository twitterRepository;

	@Autowired
	private RunRepository runRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ITwitterService twitterService;

	@Mock
	private TwitterAuthenticator authenticator;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static Run run;

	private boolean init = false;

	/**
	 * Mock of a list of tweets from file. Mock of {@link TwitterAuthenticator}}
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		if (!init) {
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
			init = true;
		}
	}

	@Test
	@Order(1)
	public void mockHttpRequest()
			throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

		Date start = new Date();

		Run response = twitterService.parseTweetsFromRequest(authenticator.getAuthorizedHttpRequestFactory(),
				DEFAULT_API.concat("?track=bieber"));

		assertTrue(response.getTweetRuns().size() == 5);

		log.info(" **** ***   **   *** **** **** **** ");
		log.info("  **   *** **** ***  **   **    **  ");
		log.info("  **    ****  ****   **** ****  **  ");

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		log.info(objectMapper.writeValueAsString(Utils.sortTweets(response.getTweetRuns())));

		log.info(" **** ***   **   *** **** **** **** ");
		log.info("  **   *** **** ***  **   **    **  ");
		log.info("  **    ****  ****   **** ****  **  ");

		run = twitterService.createRun(response, new Date().getTime() - start.getTime(), DEFAULT_API, "?track=bieber");

		log.info("We have a new Run");
		/**
		 * We have a new Run
		 */
		List<Run> runs = runRepository.findAll();
		assertTrue(runs.size() == 1);

		log.info("We have new Tweets");

		/**
		 * We have 5 tweets
		 */
		List<TweetRun> tweets = twitterRepository.findAll();
		assertTrue(tweets.size() == 5);

		/**
		 * We have only 3 users plus 2 empty
		 */
		List<UserTweet> users = userRepository.findAll();
		assertTrue(users.size() == 3);

	}

	@Test
	@Order(2)
	public void testGreeting() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + this.port + "/", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	/**
	 * This is a test for real api, not for integration tests
	 * 
	 * @throws Exception
	 */
	@Test
	@Disabled
	public void testRun() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + this.port + "/run",
				String.class);
		log.info(entity.getBody());

		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	@Order(3)
	public void testRunList() throws Exception {
		ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:" + this.port + "/run/list",
				String.class);
		List<Run> aRun = objectMapper.readValue(entity.getBody(), new TypeReference<List<Run>>() {
		});

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		log.info(objectMapper.writeValueAsString(aRun.get(0)));

		assertEquals(aRun.get(0).getRunId(), run.getRunId());
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
}
