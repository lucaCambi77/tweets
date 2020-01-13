/**
 * 
 */
package org.cambi.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.cambi.model.Run;

import com.google.api.client.http.HttpRequestFactory;

/**
 * @author luca
 *
 */
public interface ITwitterService {

	/**
	 * @param authorizedHttpRequestFactory
	 * @param concat
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	Run parseTweetsFromRequest(HttpRequestFactory authorizedHttpRequestFactory, String path)
			throws IOException, InterruptedException, ExecutionException;

	/**
	 * @param runDto
	 * @return
	 */
	Run createRun(Run runDto, Long elapse, String endPoint, String query);

}
