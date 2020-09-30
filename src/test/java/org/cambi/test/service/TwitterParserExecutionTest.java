package org.cambi.test.service;

import org.cambi.service.TwitterParserExecutionService;
import org.cambi.service.TwitterParserRunnableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class TwitterParserExecutionTest {

    @InjectMocks
    private TwitterParserExecutionService twitterParserExecutionService;

    @Mock
    private TwitterParserRunnableService twitterParserRunnableService;

    @Mock
    private ExecutorService executor;

    @Mock
    private Future future;

    @Test
    public void shouldThrowRuntimeException() throws InterruptedException, ExecutionException, TimeoutException {

        Mockito.lenient().when(executor.submit(twitterParserRunnableService)).thenReturn(future);
        Mockito.lenient().when(future.get(anyLong(), any())).thenThrow(new RuntimeException("Error"));

        assertThrows(
                RuntimeException.class,
                () -> twitterParserExecutionService.getTweetsFromExecution("path"),
                "Expected getTweetsFromExecution() to throw RuntimeException, but it didn't"
        );
    }

    @Test
    public void shouldThrowInterruptedExceptionException() throws InterruptedException, ExecutionException, TimeoutException {

        Mockito.lenient().when(executor.submit(twitterParserRunnableService)).thenReturn(future);
        Mockito.lenient().when(future.get(anyLong(), any())).thenThrow(new InterruptedException("Error"));

        assertThrows(
                InterruptedException.class,
                () -> twitterParserExecutionService.getTweetsFromExecution("path"),
                "Expected getTweetsFromExecution() to throw InterruptedException, but it didn't"
        );
    }

    @Test
    public void shouldThrowExecutionExceptionException() throws InterruptedException, ExecutionException, TimeoutException {

        Mockito.lenient().when(executor.submit(twitterParserRunnableService)).thenReturn(future);
        Mockito.lenient().when(future.get(anyLong(), any())).thenThrow(new ExecutionException(new Throwable()));

        assertThrows(
                ExecutionException.class,
                () -> twitterParserExecutionService.getTweetsFromExecution("path"),
                "Expected getTweetsFromExecution() to throw InterruptedException, but it didn't"
        );
    }

    @Test
    public void shouldThrowTimeoutExceptionException() throws InterruptedException, ExecutionException, TimeoutException {

        Mockito.lenient().when(executor.submit(twitterParserRunnableService)).thenReturn(future);
        Mockito.lenient().when(future.get(anyLong(), any())).thenThrow(new TimeoutException("timeout"));

        assertDoesNotThrow(
                () -> twitterParserExecutionService.getTweetsFromExecution("path"),
                "When runnable times out should not throws exception"
        );
    }

}
