package org.cambi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.dao.RunDao;
import org.cambi.service.ITwitterService;
import org.cambi.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class AppController extends Constant {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private TwitterAuthenticator authenticator;

    @Autowired
    private ITwitterService twitterService;

    @Autowired
    private RunDao runRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
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

        Run run = twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                api, query);

        return objectMapper.writeValueAsString(Utils.sortTweets(run.getTweetRuns()));
    }

    @GetMapping("/run/list")
    public String runList() throws IOException {
        return objectMapper.writeValueAsString(twitterService.findAllRun());
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

}
