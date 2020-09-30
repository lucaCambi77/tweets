package org.cambi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.TweetDto;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class TwitterParserExecutionService {

    private TwitterParserRunnableService runnable;
    private final long maxPollTime = 30;
    private ExecutorService executor;

    public Set<TweetDto> getTweetsFromExecution(String path) {
        log.debug("I am looking for tweets...");

        runnable.setPath(path);
        Future<?> future = executor.submit(runnable);

        try {
            future.get(maxPollTime, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (RuntimeException e) {
            future.cancel(true);
            throw new RuntimeException(e);
        } finally {
            executor.shutdownNow();
            return runnable.getTweets();
        }
    }

}
