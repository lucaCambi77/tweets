package org.cambi.controller;

import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.UserTweet;
import org.cambi.oauth.twitter.TwitterAuthenticationException;
import org.cambi.oauth.twitter.TwitterAuthenticator;
import org.cambi.service.ITwitterService;
import org.cambi.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class AppController extends Constant {

    @Autowired
    private TwitterAuthenticator authenticator;

    @Autowired
    private ITwitterService twitterService;

    @GetMapping("/run")
    public LinkedHashMap<BigInteger, List<UserTweet>> run(@RequestParam(name = "api", required = false, defaultValue = DEFAULT_API) String api,
                                                          @RequestParam(name = "query", required = false, defaultValue = "?track=bieber") String query)
            throws IOException, TwitterAuthenticationException, InterruptedException, ExecutionException {

        Run run = twitterService.createRun(authenticator.getAuthorizedHttpRequestFactory(),
                api, query);

        List<UserTweet> userTweets = twitterService.findByRun(run.getRunId());

        return Utils.groupByUserTweets(userTweets);
    }

    @GetMapping("/run/list")
    public List<Run> runList() throws IOException {
        return twitterService.findAllRun();
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

}
