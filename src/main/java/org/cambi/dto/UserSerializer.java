/**
 * 
 */
package org.cambi.dto;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.cambi.model.UserTweet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author luca
 *
 */
public class UserSerializer extends StdSerializer<UserTweet> {

	private DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

	public UserSerializer() {
		this(null);
	}

	public UserSerializer(Class<UserTweet> t) {
		super(t);
	}

	@Override
	public void serialize(UserTweet value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId().getUserId().intValue());
		jgen.writeStringField("name", value.getUserName());
		jgen.writeStringField("screen_name", value.getUserSreenName());
		jgen.writeStringField("created_at", dateFormat.format(value.getCreationDate()));

		jgen.writeEndObject();
	}
}
