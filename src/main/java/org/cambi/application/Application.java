package org.cambi.application;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.repository.RunRepository;
import org.cambi.service.ITwitterService;
import org.cambi.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
@RestController
public class Application extends Constant {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private TwitterAuthenticator authenticator;

	@Autowired
	private ITwitterService twitterService;

	@Autowired
	private RunRepository runRepository;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 
	 * @param api
	 * @param query
	 * @return
	 * @throws IOException
	 * @throws TwitterAuthenticationException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping("/run")
	public String run(@RequestParam(name = "api", required = false, defaultValue = DEFAULT_API) String api,
			@RequestParam(name = "query", required = false, defaultValue = "?track=bieber") String query)
			throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

		Date start = new Date();

		Run response = twitterService.parseTweetsFromRequest(authenticator.getAuthorizedHttpRequestFactory(),
				api.concat(query));

		log.info(" **** ***   **   *** **** **** **** ");
		log.info("  **   *** **** ***  **   **    **  ");
		log.info("  **    ****  ****   **** ****  **  ");

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		log.info(objectMapper.writeValueAsString(response));

		log.info(" **** ***   **   *** **** **** **** ");
		log.info("  **   *** **** ***  **   **    **  ");
		log.info("  **    ****  ****   **** ****  **  ");

		twitterService.createRun(response, new Date().getTime() - start.getTime(), api, query);

		return objectMapper.writeValueAsString(Utils.sortTweets(response.getTweetRuns()));
	}

	@GetMapping("/run/list")
	public String runList() throws IOException {
		return objectMapper.writeValueAsString(runRepository.findAll(Sort.by(Sort.Direction.DESC, "runTime")));
	}

	@GetMapping("/")
	public String home() {
		return "Hello World";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
