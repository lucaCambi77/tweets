package org.cambi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.TweetDto;
import org.cambi.exception.TwitterParserRunnableException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

@Service
@AllArgsConstructor
@Slf4j
public class TwitterParserExecutionService {

    private TwitterParserRunnableService runnable;
    private final long maxPollTime = 30;
    private ExecutorService executor;

    public Set<TweetDto> getTweetsFromExecution(String path) throws Exception {
        log.debug("I am looking for tweets...");

        runnable.setPath(path);
        Future<?> future = executor.submit(runnable);

        Exception exception = null;

        try {
            future.get(maxPollTime, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (TwitterParserRunnableException | InterruptedException | ExecutionException e) {
            exception = e;
        } finally {
            executor.shutdownNow();
        }

        if (Objects.nonNull(exception))
            throw exception;

        return runnable.getTweets();
    }

}
