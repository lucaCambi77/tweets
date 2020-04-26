/**
 *
 */
package org.cambi.service;

import com.google.api.client.http.HttpRequestFactory;
import org.cambi.model.Run;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author luca
 *
 */
public interface ITwitterService {

    List<Run> findAllRun();

    Run createRun(HttpRequestFactory authorizedHttpRequestFactory, String api, String query) throws ExecutionException, InterruptedException;
}
