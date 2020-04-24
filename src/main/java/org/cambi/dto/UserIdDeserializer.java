/**
 *
 */
package org.cambi.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.cambi.model.UserTweetId;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author luca
 *
 */
public class UserIdDeserializer extends StdDeserializer<UserTweetId> {

	public UserIdDeserializer() {
		this(null);
	}

	public UserIdDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public UserTweetId deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);

		return new UserTweetId(new BigInteger(node.asText()), null);
	}
}
