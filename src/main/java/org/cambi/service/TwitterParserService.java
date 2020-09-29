package org.cambi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cambi.dto.RunDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@AllArgsConstructor
@Slf4j
public class TwitterParserService {

    private TwitterServiceRunnable runnable;

    public RunDto parseTweetsFrom(String path)
            throws InterruptedException, ExecutionException {
        log.info("I am looking for tweets...");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        runnable.setPath(path);
        Future<?> future = executor.submit(runnable);

        RunDto run = new RunDto();
        try {
            future.get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (RuntimeException e) {
            future.cancel(true);
            run.setException(e.getMessage());
        } finally {
            run.setTweets(runnable.getTweets());
        }

        executor.shutdownNow();
        return run;
    }

}
