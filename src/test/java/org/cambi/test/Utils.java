package org.cambi.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.cambi.dto.TweetDto;

import java.io.IOException;
import java.util.Set;

public class Utils {

    public static HttpRequestFactory getFakeRequestFactory(Set<TweetDto> tweets) {

        ObjectMapper objectMapper = new ObjectMapper();

        return new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {

                        try {

                            StringBuilder sb = new StringBuilder();

                            for (TweetDto tweet : tweets) {
                                sb.append(objectMapper.writeValueAsString(tweet)).append("\n");
                            }

                            MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                            response.setStatusCode(200);
                            response.setContentType(Json.MEDIA_TYPE);
                            response.setContent(sb.toString());
                            return response;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        }.createRequestFactory();
    }
}
