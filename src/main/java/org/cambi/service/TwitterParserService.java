package org.cambi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.RunDto;
import org.cambi.dto.TweetDto;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class TwitterParserService {

    private TwitterParserExecutionService twitterParserExecutionService;

    public RunDto parseTweetsFrom(String path) {
        log.debug("I am looking for tweets...");

        RunDto run = new RunDto();

        try {
            Set<TweetDto> tweetDtos = twitterParserExecutionService.getTweetsFromExecution(path);
            run.setTweets(tweetDtos);
        } catch (Exception e) {
            run.setException(e.getMessage());
        }

        return run;
    }

}
