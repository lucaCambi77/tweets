/**
 *
 */
package org.cambi.service;

import com.google.api.client.http.HttpRequestFactory;
import org.cambi.model.Run;
import org.cambi.model.UserTweet;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author luca
 *
 */
public interface ITwitterService {

    List<Run> findAllRun();

    Run createRun(HttpRequestFactory authorizedHttpRequestFactory, String api, String query) throws ExecutionException, InterruptedException;

    Set<UserTweet> findByRun(Long runId);

}
