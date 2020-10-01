package org.cambi.controller;

import org.cambi.constant.Constant;
import org.cambi.model.Run;
import org.cambi.model.UserTweet;
import org.cambi.service.TwitterRunService;
import org.cambi.service.UserTweetsService;
import org.cambi.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class AppController extends Constant {

    @Autowired
    private TwitterRunService twitterRunService;

    @Autowired
    private UserTweetsService userTweetsService;

    @GetMapping("/run")
    public Run run(@RequestParam(name = "api", required = false, defaultValue = DEFAULT_API) String api,
                   @RequestParam(name = "query", required = false, defaultValue = "?track=bieber") String query) {

        return twitterRunService.createRun(api, query);
    }

    @GetMapping("/userTweets")
    public LinkedHashMap<BigInteger, List<UserTweet>> userTweetsByRun(@RequestParam(name = "runId") Long runId) {

        List<UserTweet> userTweets = userTweetsService.findUserTweetsByRun(runId);

        return Utils.groupByUserTweets(userTweets);
    }

    @GetMapping("/run/list")
    public List<Run> runList() {
        return twitterRunService.findAllRun();
    }

    @GetMapping("/health")
    public String health() {
        return "I am running";
    }

}
